package net.rose.steamporium.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.rose.steamporium.api.util.SoundUtil;
import net.rose.steamporium.common.init.ModEntityComponents;
import net.rose.steamporium.common.init.ModSounds;

import java.util.function.Predicate;

public class EnduriumMaceItem extends ToolItem {
    private static final int ATTACK_DAMAGE_MODIFIER_VALUE = 5;
    private static final float ATTACK_SPEED_MODIFIER_VALUE = -3.4F;
    private static final float HEAVY_SMASH_SOUND_FALL_DISTANCE_THRESHOLD = 5.0F;
    private static final float NORMAL_DAMAGE_HEIGHT_THRESHOLD = 3.0F;
    private static final float HEAVY_DAMAGE_HEIGHT_THRESHOLD = 8.0F;
    public static final float KNOCK_BACK_RANGE = 3.5F;
    private static final float KNOCK_BACK_POWER = 0.7F;

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public EnduriumMaceItem(Item.Settings settings) {
        super(ToolMaterials.IRON, settings);

        final var builder = ImmutableMultimap.<EntityAttribute, EntityAttributeModifier>builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(
                        ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier",
                        ATTACK_DAMAGE_MODIFIER_VALUE, EntityAttributeModifier.Operation.ADDITION
                )
        );

        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(
                        ATTACK_SPEED_MODIFIER_ID, "Weapon modifier",
                        ATTACK_SPEED_MODIFIER_VALUE, EntityAttributeModifier.Operation.ADDITION
                )
        );
        this.attributeModifiers = builder.build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (shouldDealAdditionalDamage(attacker)) {
            var serverWorld = (ServerWorld) attacker.getWorld();
            attacker.setVelocity(attacker.getVelocity().withAxis(Direction.Axis.Y, 0.01));
            attacker.velocityModified = true;

            if (target.isOnGround()) {
                var soundEvent = attacker.fallDistance > HEAVY_SMASH_SOUND_FALL_DISTANCE_THRESHOLD
                        ? ModSounds.MACE_SMASH_GROUND_HEAVY : ModSounds.MACE_SMASH_GROUND;
                SoundUtil.playSound(target.getWorld(), attacker.getPos(), soundEvent, SoundCategory.PLAYERS);
            } else {
                SoundUtil.playSound(target.getWorld(), attacker.getPos(), ModSounds.MACE_SMASH_AIR, SoundCategory.PLAYERS);
            }

            knockBackNearbyEntities(serverWorld, attacker, target);

            target.timeUntilRegen = 0;
            final var damageSource = getDamageSource(attacker);
            final var extraDamage = getBonusAttackDamage((float) attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE), damageSource);

            if (extraDamage > 0 && attacker instanceof PlayerEntity player) {
                ModEntityComponents.MACE.get(player).maceHit();
            }

            target.damage(damageSource, extraDamage);
        }

        return super.postHit(stack, target, attacker);
    }

    public float getBonusAttackDamage(float baseAttackDamage, DamageSource damageSource) {
        if (damageSource.getSource() instanceof LivingEntity livingEntity) {
            if (!shouldDealAdditionalDamage(livingEntity)) return 0.0F;

            float effectiveDamage = getMaceDamage(baseAttackDamage, livingEntity);

            if (livingEntity.getWorld() instanceof ServerWorld) return Math.max(0F, effectiveDamage);
            return effectiveDamage;
        }

        return 0F;
    }

    private static float getMaceDamage(float baseAttackDamage, LivingEntity livingEntity) {
        final var fallingDistance = livingEntity.fallDistance;
        float effectiveDamage;

        if (fallingDistance <= NORMAL_DAMAGE_HEIGHT_THRESHOLD) {
            effectiveDamage = 4.0F * fallingDistance;
        } else if (fallingDistance <= HEAVY_DAMAGE_HEIGHT_THRESHOLD) {
            effectiveDamage = baseAttackDamage + 2.0F * (fallingDistance - NORMAL_DAMAGE_HEIGHT_THRESHOLD);
        } else {
            effectiveDamage = baseAttackDamage + 1.4F * (fallingDistance - HEAVY_DAMAGE_HEIGHT_THRESHOLD);
        }

        return effectiveDamage;
    }

    private static void knockBackNearbyEntities(World world, Entity attacker, Entity attacked) {
        final var entities = world.getEntitiesByClass(LivingEntity.class,
                attacked.getBoundingBox().expand(KNOCK_BACK_RANGE),
                getKnockbackPredicate(attacker, attacked));
        for (var knockedBackEntity : entities) {
            final var direction = knockedBackEntity.getPos().subtract(attacked.getPos());
            final var knockBackAmount = getKnockBack(attacker, knockedBackEntity, direction);
            if (knockBackAmount > 0.0) {
                final var knockBack = direction.normalize().multiply(knockBackAmount);
                knockedBackEntity.addVelocity(knockBack.x, KNOCK_BACK_POWER, knockBack.z);
                if (knockedBackEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
                }
            }
        }
    }

    // Too fucking complex for me.
    private static Predicate<LivingEntity> getKnockbackPredicate(Entity attacker, Entity attacked) {
        return (entity) -> {
            boolean bl;
            boolean bl2;
            boolean bl3;
            boolean var10000;
            label64:
            {
                bl = !entity.isSpectator();
                bl2 = entity != attacker && entity != attacked;
                bl3 = !attacker.isTeammate(entity);
                if (entity instanceof TameableEntity tameableEntity) {
                    if (attacked instanceof LivingEntity livingEntity) {
                        if (tameableEntity.isTamed() && tameableEntity.isOwner(livingEntity)) {
                            var10000 = true;
                            break label64;
                        }
                    }
                }

                var10000 = false;
            }

            boolean bl4;
            label56:
            {
                bl4 = !var10000;
                if (entity instanceof ArmorStandEntity armorStandEntity) {
                    if (armorStandEntity.isMarker()) {
                        break label56;
                    }
                }

                var10000 = true;
            }

            var bl5 = var10000;
            var bl6 = attacked.squaredDistanceTo(entity) <= Math.pow(3.5, 2.0);
            return bl && bl2 && bl3 && bl4 && bl5 && bl6;
        };
    }

    private static double getKnockBack(Entity attacker, LivingEntity attacked, Vec3d distance) {
        return (KNOCK_BACK_RANGE - distance.length()) * KNOCK_BACK_POWER * (double) (attacker.fallDistance > 5.0 ? 2 : 1) * (1.0 - attacked.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
    }

    public static boolean shouldDealAdditionalDamage(LivingEntity attacker) {
        return attacker.fallDistance > 1.5;
    }

    public DamageSource getDamageSource(LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            return user.getDamageSources().playerAttack(player);
        }

        return user.getDamageSources().mobAttack(user);
    }
}
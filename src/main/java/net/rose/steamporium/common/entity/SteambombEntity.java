package net.rose.steamporium.common.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ModStatus;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.rose.steamporium.api.util.ParticleUtil;
import net.rose.steamporium.api.util.SoundUtil;
import net.rose.steamporium.common.init.*;
import net.rose.steamporium.common.util.EntityAttachedSoundInstance;

public class SteambombEntity extends ThrownItemEntity {
    public static final double EXPLOSION_KNOCK_BACK = 1.2;
    public static final double DETECTION_RADIUS = 3;
    public static final double RADIUS = 7;

    private float drag = 1;
    private EntityAttachedSoundInstance fuseLoop;

    public SteambombEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.STEAMBOMB, owner, world);
    }

    public SteambombEntity(EntityType<? extends SteambombEntity> entityType, World world) {
        super(entityType, world);
    }

    private Box getDetectionBox(double radius) {
        return Box.of(this.getPos(), radius, radius, radius);
    }

    @Environment(EnvType.CLIENT)
    private void startPlayingFuseSound() {
        this.fuseLoop = new EntityAttachedSoundInstance(this, ModSounds.STEAMBOMB_FUSE_LOOP, this.getSoundCategory());
        MinecraftClient.getInstance().getSoundManager().play(this.fuseLoop);
    }

    @Environment(EnvType.CLIENT)
    private void stopPlayingFuseSound() {
        if (this.fuseLoop == null) return;
        MinecraftClient.getInstance().getSoundManager().stop(this.fuseLoop);
    }

    @Override
    public void onRemoved() {
        if (this.getWorld() != null && this.getWorld().isClient) {
            stopPlayingFuseSound();
        }

        super.onRemoved();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld() != null && this.getWorld().isClient && this.fuseLoop == null) {
            startPlayingFuseSound();
        }

        // Air friction.
        this.setVelocity(this.getVelocity().multiply(0.98));

        final var world = this.getWorld();
        if (world == null) return;
        ParticleUtil.spawnParticles(world, ParticleTypes.SMOKE, this.getPos(), Vec3d.ZERO, 1, 0.02d);

        // Explode if entities around.
        if (world.getEntitiesByClass(
                LivingEntity.class,
                this.getDetectionBox(DETECTION_RADIUS),
                e -> !e.isInvulnerable() && e != this.getOwner()
        ).isEmpty()) return;

        explode();
        discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        final var direction = blockHitResult.getSide();
        this.setPosition(this.getPos().subtract(this.getVelocity()));
        switch (direction.getAxis()) {
            case X -> this.setVelocity(this.getVelocity()
                    .multiply(-1.0F, 1.0F, 1.0F)
                    .multiply(this.drag));
            case Y -> this.setVelocity(this.getVelocity()
                    .multiply(1.0F, -1.0F, 1.0F)
                    .multiply(this.drag * this.drag));
            case Z -> this.setVelocity(this.getVelocity()
                    .multiply(1.0F, 1.0F, -1.0F)
                    .multiply(this.drag));
        }

        SoundUtil.playSound(
                this.getWorld(), this.getPos(), ModSounds.STEAMBOMB_BOUNCE, this.getSoundCategory(),
                0.5F, MathHelper.nextFloat(this.random, 0.9F, 1.25F)
        );

        this.drag *= 0.8F;
        if (this.drag < 0.5F) {
            explode();
            discard();
        }
    }

    private void explode() {
        final var world = this.getWorld();
        if (world.isClient) return;

        final var particleRadius = Math.sqrt(RADIUS);
        final var particleSpread = new Vec3d(particleRadius, particleRadius, particleRadius);
        ParticleUtil.spawnParticles(
                world, ParticleTypes.CLOUD,
                this.getPos(), Vec3d.ZERO,
                10, 1.2
        );
        ParticleUtil.spawnParticles(
                world, ModParticles.STEAM_EXPLOSION,
                this.getPos(), particleSpread,
                20, 0
        );
        SoundUtil.playSound(
                world, this.getPos(),
                ModSounds.STEAMBOMB_EXPLOSION, SoundCategory.PLAYERS,
                0.75F, MathHelper.nextFloat(this.random, 0.9F, 1.1F)
        );

        final var entities = world.getEntitiesByClass(
                LivingEntity.class,
                this.getDetectionBox(RADIUS),
                e -> !e.isInvulnerable()
        );
        final var damageSource = this.getDamageSources().explosion(this.getOwner(), this);
        for (var entity : entities) {
            var raycast = world.raycast(new RaycastContext(
                    this.getPos(), entity.getPos(),
                    RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE,
                    this
            ));
            if (raycast.getType() != HitResult.Type.MISS) continue;

            entity.damage(damageSource, 15);
            entity.addStatusEffect(new StatusEffectInstance(ModEffects.STEAMY, 200, 0), this.getOwner());

            final var knockback = getKnockBack(this.getPos(), entity.getPos(), getKnockBackResistance(entity));
            entity.setVelocity(knockback);
            entity.velocityModified = true;
        }
    }

    private double getKnockBackResistance(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (!livingEntity.getAttributes().hasAttribute(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) return 0d;
            return livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        }

        return 0d;
    }

    private Vec3d getKnockBack(Vec3d epicenter, Vec3d entityPosition, double knockbackResistance) {
        final var dir = entityPosition.subtract(epicenter).normalize();
        return dir.multiply(EXPLOSION_KNOCK_BACK / (1d + knockbackResistance));
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ENDURIUM_STEAMBOMB;
    }
}

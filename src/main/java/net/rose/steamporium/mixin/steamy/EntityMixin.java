package net.rose.steamporium.mixin.steamy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.rose.steamporium.api.util.ParticleUtil;
import net.rose.steamporium.common.init.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "baseTick",
            at = @At("HEAD")
    )
    private void baseTick$steamporium(CallbackInfo ci) {
        // Doubles the damage.
        final var entity = (Entity) (Object) this;
        if (entity.isFireImmune()) return;
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModEffects.STEAMY)
                && livingEntity.isOnFire() && !livingEntity.isInLava() && livingEntity.getFireTicks() % 10 == 0) {
            livingEntity.damage(livingEntity.getDamageSources().onFire(), 1.0f);
        }
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void tick$steamporium(CallbackInfo ci) {
        final var entity = (Entity) (Object) this;
        if (entity.isFireImmune()) return;
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModEffects.STEAMY) && Math.random() < 0.2f) {
            ParticleUtil.spawnParticles(entity.getWorld(), ParticleTypes.CLOUD, livingEntity.getEyePos(), Vec3d.ZERO, 2, 0.01d);
        }
    }
}
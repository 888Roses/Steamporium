package net.rose.steamporium.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.rose.steamporium.api.util.ParticleUtil;
import net.rose.steamporium.common.init.ModEntityTypes;
import net.rose.steamporium.common.init.ModItems;

public class SteambombEntity extends ThrownItemEntity {
    public static final double EXPLOSION_KNOCK_BACK = 1.2;
    public static final double DETECTION_RADIUS = 3;
    public static final double RADIUS = 7;

    private float drag = 1;

    public SteambombEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.STEAMBOMB, owner, world);
    }

    public SteambombEntity(EntityType<? extends SteambombEntity> entityType, World world) {
        super(entityType, world);
    }

    private Box getDetectionBox(double radius) {
        return Box.of(this.getPos(), radius, radius, radius);
    }

    @Override
    public void tick() {
        super.tick();
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

        this.drag *= 0.8F;
        if (this.drag < 0.5F) {
            explode();
            discard();
        }
    }

    private void explode() {
        final var world = this.getWorld();
        if (world.isClient) return;

        ParticleUtil.spawnParticles(world, ParticleTypes.EXPLOSION_EMITTER, this.getPos(), new Vec3d(0.5, 0, 0.5), 1,
                0);
        ParticleUtil.spawnParticles(world, ParticleTypes.EXPLOSION, this.getPos(), new Vec3d(0.5, 0, 0.5), 1, 0);
        ParticleUtil.spawnParticles(world, ParticleTypes.SWEEP_ATTACK, this.getPos(), new Vec3d(RADIUS / 4d,
                RADIUS / 4d, RADIUS / 4d), 40, 0);

        final var entities = world.getEntitiesByClass(
                LivingEntity.class,
                this.getDetectionBox(RADIUS),
                e -> !e.isInvulnerable()
        );
        final var damageSource = this.getDamageSources().explosion(this.getOwner(), this);
        for (var entity : entities) {
            entity.damage(damageSource, 6);

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

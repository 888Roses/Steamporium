package net.rose.steamporium.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.rose.steamporium.common.init.ModEntityTypes;
import net.rose.steamporium.common.init.ModItems;

public class SteambombEntity extends ThrownItemEntity {
    public float bounceGroundDragMultiplier = 0.1F;
    private float groundDrag = 1;

    public SteambombEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.STEAMBOMB, owner, world);
    }

    public SteambombEntity(EntityType<? extends SteambombEntity> entityType, World world) {
        super(entityType, world);
    }

    private boolean selectExplosionSubjects(Entity entity) {
        return entity instanceof LivingEntity livingEntity && !livingEntity.isInvulnerable() && entity != this.getOwner();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        Direction direction = blockHitResult.getSide();
        this.setPosition(this.getPos().subtract(this.getVelocity()));
        switch (direction.getAxis()) {
            case X -> this.setVelocity(this.getVelocity().multiply(-1.0F, 1.0F, 1.0F).multiply(this.groundDrag));
            case Y -> this.setVelocity(this.getVelocity().multiply(1.0F, -1.0F, 1.0F).multiply(this.groundDrag));
            case Z -> this.setVelocity(this.getVelocity().multiply(1.0F, 1.0F, -1.0F).multiply(this.groundDrag));
        }

        this.groundDrag *= this.bounceGroundDragMultiplier;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        explode();
    }

    @Override
    public void tick() {
        super.tick();

        final var box = this.getBoundingBox().expand(2F);
        final var entities = this.getWorld().getOtherEntities(this, box, this::selectExplosionSubjects);
        if (!entities.isEmpty()) {
            explode();
        }
    }

    private void explode() {
        final var box = this.getBoundingBox().expand(3F);
        final var entities = this.getWorld().getOtherEntities(this, box, this::selectExplosionSubjects);

        if (entities.isEmpty()) {
            return;
        }

        final var damageSource = this.getDamageSources().explosion(this.getOwner(), this);
        for (var entity : entities) {
            entity.damage(damageSource, 4);

            entity.setVelocity(getKnockBack(this.getPos(), entity.getPos(), getKnockBackResistance(entity)));
            entity.velocityModified = true;
        }

        discard();
    }

    private double getKnockBackResistance(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        }

        return 0d;
    }

    private Vec3d getKnockBack(Vec3d epicenter, Vec3d entityPosition, double knockbackResistance) {
        final double EXPLOSION_KNOCK_BACK = 2d;

        final var dir = epicenter.subtract(entityPosition).normalize();
        return dir.multiply(EXPLOSION_KNOCK_BACK / knockbackResistance);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ENDURIUM_STEAMBOMB;
    }
}

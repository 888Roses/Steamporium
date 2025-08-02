package net.rose.steamporium.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.rose.steamporium.common.Steamporium;
import net.rose.steamporium.common.entity.SteambombEntity;

public class ModEntityTypes {
    public static final EntityType<SteambombEntity> STEAMBOMB = of(
            "steambomb", EntityType.Builder
                    .<SteambombEntity>create(SteambombEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.25F, 0.25F)
                    .maxTrackingRange(8).trackingTickInterval(4)
    );

    public static <T extends Entity> EntityType<T> of(String path, EntityType.Builder<T> entity) {
        return Registry.register(
                Registries.ENTITY_TYPE, Steamporium.id(path),
                entity.build(path)
        );
    }

    public static void init() {}
}

package net.rose.steamporium.common.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.rose.steamporium.api.cardinal.ExtendedEntityComponentInitializer;
import net.rose.steamporium.common.component.MaceComponent;

public class ModEntityComponents extends ExtendedEntityComponentInitializer {
    public static final ComponentKey<MaceComponent> MACE = of("mace",MaceComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PlayerEntity.class, MACE, MaceComponent::new);
    }
}

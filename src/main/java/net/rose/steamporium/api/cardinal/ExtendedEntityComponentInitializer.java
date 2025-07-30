package net.rose.steamporium.api.cardinal;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.rose.steamporium.common.Steamporium;

public abstract class ExtendedEntityComponentInitializer implements EntityComponentInitializer {
    protected static <C extends Component> ComponentKey<C> of(String componentId, Class<C> componentClass) {
        return ComponentRegistry.getOrCreate(Steamporium.id(componentId), componentClass);
    }
}
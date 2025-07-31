package net.rose.steamporium.api.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.rose.steamporium.api.networking.NetworkMessage;

public interface ExtendedComponent<T extends Component> {
    ComponentKey<T> getComponent();

    /**
     * Only supports server -> client synchronization and not the other way around.
     * For reverse synchronization, please use {@link NetworkMessage}.
     * @param target The object to sync.
     */
    default void sync(Object target) {
        getComponent().sync(target);
    }
}

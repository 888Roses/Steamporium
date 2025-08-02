package net.rose.steamporium.client.init;

import net.rose.steamporium.client.event.MaceFlashOverlayEvent;

public class ModEventRegisterer {
    public static void init() {
        MaceFlashOverlayEvent.EVENT.register(new MaceFlashOverlayEvent());
    }
}

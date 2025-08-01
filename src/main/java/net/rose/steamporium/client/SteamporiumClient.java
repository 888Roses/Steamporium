package net.rose.steamporium.client;

import net.fabricmc.api.ClientModInitializer;
import net.rose.steamporium.client.event.MaceFlashOverlayEvent;
import net.rose.steamporium.client.init.ModItemModels;

public class SteamporiumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModItemModels.init();

        MaceFlashOverlayEvent.EVENT.register(new MaceFlashOverlayEvent());
    }
}

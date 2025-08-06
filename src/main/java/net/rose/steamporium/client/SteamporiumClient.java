package net.rose.steamporium.client;

import net.fabricmc.api.ClientModInitializer;
import net.rose.steamporium.client.event.MaceFlashOverlayEvent;
import net.rose.steamporium.client.init.ModEntityRenderers;
import net.rose.steamporium.client.init.ModEventRegisterer;
import net.rose.steamporium.client.init.ModItemModels;
import net.rose.steamporium.client.init.ModParticleFactories;

public class SteamporiumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModItemModels.init();
        ModEventRegisterer.init();
        ModEntityRenderers.init();
        ModParticleFactories.init();
    }
}

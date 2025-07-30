package net.rose.steamporium.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.rose.steamporium.common.init.ModEffects;
import net.rose.steamporium.common.init.ModItemGroups;
import net.rose.steamporium.common.init.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Steamporium implements ModInitializer {
    public static final String MOD_ID = "steamporium";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModItems.init();
        ModItemGroups.init();
        ModEffects.init();
    }
}

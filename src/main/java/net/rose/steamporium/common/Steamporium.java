package net.rose.steamporium.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.rose.steamporium.common.init.*;
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
        ModToolMaterials.init();
        ModItems.init();
        ModItemGroups.init();
        ModEffects.init();
        ModSounds.init();
        ModEntityTypes.init();
    }

    public static ModelIdentifier modelId(String path, String variant) {
        return new ModelIdentifier(MOD_ID, path, variant);
    }
}

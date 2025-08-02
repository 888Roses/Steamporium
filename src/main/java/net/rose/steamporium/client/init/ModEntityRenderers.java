package net.rose.steamporium.client.init;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.rose.steamporium.common.init.ModEntityTypes;

public class ModEntityRenderers {
    public static void init() {
        EntityRendererRegistry.register(ModEntityTypes.STEAMBOMB, FlyingItemEntityRenderer::new);
    }
}

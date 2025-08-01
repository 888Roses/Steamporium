package net.rose.steamporium.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.rose.steamporium.common.init.ModEntityComponents;

public class MaceFlashOverlayEvent implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if (MinecraftClient.getInstance().player != null) {
            final var time = ModEntityComponents.MACE.get(MinecraftClient.getInstance().player).getMaceHitTime();
            if (time > 0) {
                final var alpha = MathHelper.clamp(Math.pow((float)time,1.3F),0F,4F)/4F;
                drawContext.fill(0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight(), ColorHelper.Abgr.getAbgr((int) (alpha * 150), 255, 255, 255));
            }
        }
    }
}

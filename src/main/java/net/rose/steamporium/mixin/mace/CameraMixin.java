package net.rose.steamporium.mixin.mace;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.rose.steamporium.common.init.ModEntityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(at = @At("HEAD"), method = "setRotation", cancellable = true)
    private void setRotation$steamporium(CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null) {
            if (ModEntityComponents.MACE.get(MinecraftClient.getInstance().player).getMaceHitTime() > 0) {
                ci.cancel();
            }
        }
    }
}

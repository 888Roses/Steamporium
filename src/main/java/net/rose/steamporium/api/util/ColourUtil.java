package net.rose.steamporium.api.util;

import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ColourUtil {
    public static int lerp(float delta, int start, int end) {
        delta = MathHelper.clamp(delta, 0, 1);
        final var startRGB = new Color(start);
        final var endRGB = new Color(end);
        final var r = MathHelper.lerp(delta, startRGB.getRed(), endRGB.getRed());
        final var g = MathHelper.lerp(delta, startRGB.getGreen(), endRGB.getGreen());
        final var b = MathHelper.lerp(delta, startRGB.getBlue(), endRGB.getBlue());
        return new Color(r, g, b, 255).getRGB();
    }
}

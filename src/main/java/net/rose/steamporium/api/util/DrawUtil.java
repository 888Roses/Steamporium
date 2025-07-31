package net.rose.steamporium.api.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class DrawUtil {
    public static void renderWithBoldShadow(DrawContext drawContext, TextRenderer renderer, Text text, int x, int y, int borderColour) {
        drawContext.drawText(renderer, text.getString(), x + 1, y, borderColour, false);
        drawContext.drawText(renderer, text.getString(), x - 1, y, borderColour, false);
        drawContext.drawText(renderer, text.getString(), x, y + 1, borderColour, false);
        drawContext.drawText(renderer, text.getString(), x, y - 1, borderColour, false);
        drawContext.drawText(renderer, text, x, y, 0xFFFFFFFF, false);
    }
}

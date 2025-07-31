package net.rose.steamporium.api.item.tint;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface TintedItem {
    TintData getColour(ItemStack stack, int layer);
}
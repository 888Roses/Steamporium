package net.rose.steamporium.common.item;

import net.minecraft.item.Item;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class EnduriumSteambombItem extends Item {
    public EnduriumSteambombItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName() {
        return ((MutableText) super.getName()).styled(style -> style.withColor(0xFEEEBA));
    }
}

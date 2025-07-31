package net.rose.steamporium.common.item;

import net.minecraft.text.MutableText;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.Text;

public class EnduriumAspisItem extends ShieldItem {
    public EnduriumAspisItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName() {
        return ((MutableText)super.getName()).styled(style -> style.withColor(0xFEEEBA));
    }
}
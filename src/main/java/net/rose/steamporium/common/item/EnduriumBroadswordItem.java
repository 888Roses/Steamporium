package net.rose.steamporium.common.item;

import net.minecraft.item.SwordItem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.rose.steamporium.api.util.ToolUtil;
import net.rose.steamporium.common.init.ModToolMaterials;

public class EnduriumBroadswordItem extends SwordItem {
    public EnduriumBroadswordItem(Settings settings) {
        super(
                ModToolMaterials.ENDURIUM,
                ToolUtil.getAttackDamage(8, ModToolMaterials.ENDURIUM),
                ToolUtil.getEffectiveAttackSpeed(1.6f),
                settings
        );
    }

    @Override
    public Text getName() {
        return ((MutableText) super.getName()).styled(style -> style.withColor(0xFEEEBA));
    }
}

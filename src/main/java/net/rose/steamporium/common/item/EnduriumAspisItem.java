package net.rose.steamporium.common.item;

import net.fabric_extras.shield_api.item.CustomShieldItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.rose.steamporium.common.init.ModItems;

import java.util.List;

public class EnduriumAspisItem extends CustomShieldItem {
    public EnduriumAspisItem(Settings settings) {
        super(null, () -> Ingredient.ofItems(ModItems.ENDURIUM_ALLOY), List.of(), settings);
    }

    @Override
    public Text getName() {
        return ((MutableText) super.getName()).styled(style -> style.withColor(0xFEEEBA));
    }
}
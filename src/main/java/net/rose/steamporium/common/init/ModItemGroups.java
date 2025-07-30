package net.rose.steamporium.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.rose.steamporium.common.Steamporium;

public class ModItemGroups {
    public static final ItemGroup MAIN = Registry.register(
            Registries.ITEM_GROUP, Steamporium.id(Steamporium.MOD_ID),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup." + Steamporium.MOD_ID))
                    .icon(() -> new ItemStack(ModItems.ENDURIUM_ALLOY))
                    .entries(ModItemGroups::populateMain)
                    .build()
    );

    private static void populateMain(ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries) {
        entries.add(ModItems.ENDURIUM_ALLOY);
        entries.add(ModItems.ENDURIUM_SCRAP);
        entries.add(ModItems.ENDURIUM_BROADSWORD);
        entries.add(ModItems.ENDURIUM_ASPIS);
        entries.add(ModItems.ENDURIUM_STEAMBOMB);
    }

    public static void init() {}
}
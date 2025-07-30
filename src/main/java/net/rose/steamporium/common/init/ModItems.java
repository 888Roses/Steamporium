package net.rose.steamporium.common.init;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.rose.steamporium.common.Steamporium;

public class ModItems {
    public static final Item ENDURIUM_ALLOY = of("endurium_alloy");
    public static final Item ENDURIUM_SCRAP = of("endurium_scrap");
    public static final Item ENDURIUM_STEAMBOMB = of("endurium_steambomb");
    public static final Item ENDURIUM_ASPIS = of("endurium_aspis");
    public static final Item ENDURIUM_BROADSWORD = of("endurium_broadsword");

    private static Item of(String name, Item item) {
        return Registry.register(
                Registries.ITEM,
                Steamporium.id(name),
                item
        );
    }

    private static Item of(String name, Item.Settings settings) {
        return of(name, new Item(settings));
    }

    private static Item of(String name) {
        return of(name, new Item.Settings());
    }

    private static Item createBlockItem(String path, Block block) {
        return of(path, new BlockItem(block, new Item.Settings()));
    }

    public static void init() {}
}
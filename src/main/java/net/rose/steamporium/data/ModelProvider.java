package net.rose.steamporium.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.rose.steamporium.common.init.ModItems;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ENDURIUM_BROADSWORD, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDURIUM_ALLOY, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDURIUM_SCRAP, Models.GENERATED);
    }
}

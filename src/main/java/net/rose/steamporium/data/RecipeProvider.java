package net.rose.steamporium.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.rose.steamporium.common.init.ModItems;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offer2x2CompactingRecipe(exporter, RecipeCategory.MISC, ModItems.ENDURIUM_ALLOY, ModItems.ENDURIUM_SCRAP);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ENDURIUM_ALLOY)
                .input(Items.IRON_INGOT).input(Items.GOLD_INGOT)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter, "iron_ingot_gold_ingot_to_endurium_alloy");
        offerShapelessRecipe(exporter, ModItems.ENDURIUM_SCRAP, ModItems.ENDURIUM_ALLOY, "ingredients", 4);
        SmithingTransformRecipeJsonBuilder.create(Ingredient.EMPTY, Ingredient.ofItems(Items.NETHERITE_SWORD), Ingredient.ofItems(ModItems.ENDURIUM_ALLOY), RecipeCategory.COMBAT, ModItems.ENDURIUM_BROADSWORD)
                .criterion(hasItem(ModItems.ENDURIUM_ALLOY), conditionsFromItem(ModItems.ENDURIUM_ALLOY))
                .criterion(hasItem(Items.NETHERITE_SWORD), conditionsFromItem(Items.NETHERITE_SWORD))
                .offerTo(exporter, "netherite_sword_to_endurium_broadsword");
        SmithingTransformRecipeJsonBuilder.create(Ingredient.EMPTY, Ingredient.ofItems(Items.SHIELD), Ingredient.ofItems(ModItems.ENDURIUM_ALLOY), RecipeCategory.COMBAT, ModItems.ENDURIUM_ASPIS)
                .criterion(hasItem(ModItems.ENDURIUM_ALLOY), conditionsFromItem(ModItems.ENDURIUM_ALLOY))
                .criterion(hasItem(Items.SHIELD), conditionsFromItem(Items.SHIELD))
                .offerTo(exporter, "shield_to_endurium_aspis");
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.ENDURIUM_STEAMBOMB)
                .pattern(" E ").pattern("ECE").pattern(" W ")
                .input('E', ModItems.ENDURIUM_SCRAP).input('C', Items.COAL).input('W', Items.REDSTONE)
                .criterion(hasItem(Items.COAL), conditionsFromItem(Items.COAL))
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(exporter, "endurium_steambomb");
    }
}

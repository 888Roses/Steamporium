package net.rose.steamporium.common.init;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;

public class ModToolMaterials {
    public static final ToolMaterial ENDURIUM = copy(ToolMaterials.NETHERITE);

    @SuppressWarnings("SameParameterValue")
    private static ToolMaterial copy(ToolMaterial base) {
        return of(
                base.getDurability(),
                base.getMiningSpeedMultiplier(), base.getMiningLevel(),
                base.getAttackDamage(),
                base.getEnchantability(),
                base.getRepairIngredient()
        );
    }

    private static ToolMaterial of(
            int durability,
            float miningSpeedMultiplier, int miningLevel,
            float attackDamage,
            int enchantability,
            Ingredient repairIngredient
    ) {
        return new ToolMaterial() {
            @Override
            public int getDurability() {
                return durability;
            }

            @Override
            public float getMiningSpeedMultiplier() {
                return miningSpeedMultiplier;
            }

            @Override
            public float getAttackDamage() {
                return attackDamage;
            }

            @Override
            public int getMiningLevel() {
                return miningLevel;
            }

            @Override
            public int getEnchantability() {
                return enchantability;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return repairIngredient;
            }
        };
    }

    public static void init() {
    }
}

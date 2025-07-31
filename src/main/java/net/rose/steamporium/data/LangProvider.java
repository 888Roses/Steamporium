package net.rose.steamporium.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.rose.steamporium.common.Steamporium;
import net.rose.steamporium.common.init.ModEffects;
import net.rose.steamporium.common.init.ModItems;

import java.util.List;

public class LangProvider extends FabricLanguageProvider {
    private TranslationBuilder lang;

    public LangProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        this.lang = translationBuilder;

        this.lang.add("itemgroup." + Steamporium.MOD_ID, "Steamporium");

        item(ModItems.ENDURIUM_ALLOY, "Endurium Alloy");
        item(ModItems.ENDURIUM_SCRAP, "Endurium Scrap");
        item(ModItems.ENDURIUM_BROADSWORD, "Endurium Broadsword");
        item(ModItems.ENDURIUM_ASPIS, "Endurium Aspis");
        item(ModItems.ENDURIUM_STEAMBOMB, "Endurium Steambomb");

        this.lang.add(ModEffects.STEAMY, "Steamy");
    }

    private void enchantment(Enchantment enchantment, String name, String desc) {
        lang.add(enchantment, name);
        lang.add(enchantment.getTranslationKey() + ".desc", desc);
    }

    /**
     * Generates translation keys for a death message.
     *
     * @param name The ID name of that sound message.
     * @param desc The text shown in chat when a named or owned entity dies by this death message.
     */
    private void deathMessage(String name, String desc) {
        lang.add("death.attack." + name, desc);
    }

    /**
     * Generates a translation key for a given sound for it to have a subtitle in-game.
     *
     * @param soundEvent The sound to generate a subtitle for.
     * @param subtitle   The subtitle of the sound as shown to the player in-game.
     */
    private void sound(SoundEvent soundEvent, String subtitle) {
        lang.add(soundEvent.getId().toTranslationKey("subtitles"), subtitle);
    }

    /**
     * Generates a lang key for a given block.
     *
     * @param block The block you wish to generate a lang key for.
     * @param name  The name of that block as shown to the player in-game.
     */
    private void block(Block block, String name) {
        lang.add(block, name);
    }

    /**
     * Generates a lang key for a given block, similarly to how {@link LangProvider#block(Block, String)} does it. Adds
     * a description to it. Please note that this description <b>is not automatically appended to the block tooltip.</b>
     * Rather, the while the key is being generated you still need to override
     * {@link Block#appendTooltip(ItemStack, BlockView, List, TooltipContext)} and use said translation key to have your
     * description be shown in-game.
     *
     * @param block The block you wish to generate a lang key for.
     * @param name  The name of that block as shown to the player in-game.
     * @param desc  The description of that block as shown to the player in-game.
     */
    private void block(Block block, String name, String desc) {
        block(block, name);
        lang.add(block.getTranslationKey() + ".desc", desc);
    }

    /**
     * Generates an item lang key.
     *
     * @param item The item for which you wish to generate translation.
     * @param name The name of the item as shown in-game.
     */
    private void item(Item item, String name) {
        lang.add(item, name);
    }

    /**
     * Generates an item lang key for the item, added a suffix. This means that for the item "banana_split" with the
     * suffix "burnt", the generated lang key will be "banana_split_burnt".
     *
     * @param item   The item for which you wish to generate translation.
     * @param suffix A string added to the end of the translation key of the item. If this string does not start with a
     *               '.' character, it will automatically be added to it.
     * @param name   The name of the item as shown in-game.
     */
    private void subItem(Item item, String suffix, String name) {
        if (!suffix.startsWith(".")) suffix = "." + suffix;
        lang.add(item.getTranslationKey() + suffix, name);
    }

    /**
     * Generates an item lang key similarly to how {@link LangProvider#item(Item, String)} does. Adds a description to
     * it. Please note that this description <b>is not automatically appended to the item tooltip.</b> Rather, the while
     * the key is being generated you still need to override
     * {@link Item#appendTooltip(ItemStack, World, List, TooltipContext)} and use said translation key to have your
     * description be shown in-game.
     *
     * @param item The item for which you wish to generate translation.
     * @param name The name of the item as shown in-game.
     * @param desc The description of the item as shown in-game.
     */
    private void item(Item item, String name, String... desc) {
        item(item, name);

        var descIndex = 1;
        for (var descItem : desc) {
            subItem(item, "desc" + descIndex, descItem);
            descIndex++;
        }
    }

    /**
     * Generates the language keys for an avancement.
     *
     * @param path The ID name of the advancement.
     * @param name The in-game literal name of the advancement as shown to the player in the advancement tab.
     * @param desc The description of the advancement as shown to the player in the advancement tab.
     */
    private void advancement(String path, String name, String desc) {
        lang.add("advancements.steamporium." + path, name);
        lang.add("advancements.steamporium." + path + ".desc", desc);
    }
}

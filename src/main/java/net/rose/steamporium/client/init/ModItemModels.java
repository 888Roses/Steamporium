package net.rose.steamporium.client.init;

import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.rose.steamporium.api.item.model.ItemContextualModelInfo;
import net.rose.steamporium.common.init.ModItems;

public class ModItemModels {
    public static final ItemContextualModelInfo BROADSWORD =
            ItemContextualModelInfo.create(ModItems.ENDURIUM_BROADSWORD)
                    .with("endurium_broadsword_handheld", ModItemModels::isHandheld)
                    .register();

    // region Util

    public static boolean isOwner(ItemContextualModelInfo.ContextInfo info) {
        if (!info.stack().hasNbt()) return false;
        var compound = info.stack().getOrCreateNbt();
        return compound.contains("is_owner") && compound.getBoolean("is_owner");
    }

    public static boolean isHandheld(ItemContextualModelInfo.ContextInfo info) {
        return info.mode() != ModelTransformationMode.GROUND && info.mode() != ModelTransformationMode.GUI;
    }

    // endregion

    // region BackEnd

    public static void init() {
    }

    // endregion
}

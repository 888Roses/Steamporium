package net.rose.steamporium.client.init;

import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.rose.steamporium.api.item.model.ItemContextualModelInfo;
import net.rose.steamporium.common.init.ModItems;

import java.util.Calendar;

public class ModItemModels {
    public static final ItemContextualModelInfo BROADSWORD =
            ItemContextualModelInfo.create(ModItems.ENDURIUM_BROADSWORD)
                    .with("endurium_broadsword_handheld", ModItemModels::isHandheld)
                    .register();

    public static final ItemContextualModelInfo STEAMBOMB =
            ItemContextualModelInfo.create(ModItems.ENDURIUM_STEAMBOMB)
                    .with("endurium_steambomb_christmas", info -> {
                        final var calendar = Calendar.getInstance();
                        return calendar.get(Calendar.MONTH) + 1 == 12 &&
                                calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26;
                    })
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

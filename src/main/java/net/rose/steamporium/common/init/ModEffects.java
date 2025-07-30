package net.rose.steamporium.common.init;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.rose.steamporium.common.Steamporium;
import net.rose.steamporium.common.effect.SteamyStatusEffect;

public class ModEffects {
    public static final StatusEffect STEAMY = register("steamy", new SteamyStatusEffect());

    public static void init() {}

    private static StatusEffect register(String id, StatusEffect entry) {
        return Registry.register(Registries.STATUS_EFFECT, Steamporium.id(id), entry);
    }
}

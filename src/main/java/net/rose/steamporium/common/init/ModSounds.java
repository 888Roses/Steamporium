package net.rose.steamporium.common.init;

import net.minecraft.sound.SoundEvent;
import net.rose.steamporium.common.Steamporium;

public class ModSounds {
    public static final SoundEvent MACE_SMASH_AIR = of("item.mace.smash_air");
    public static final SoundEvent MACE_SMASH_GROUND = of("item.mace.smash_ground");
    public static final SoundEvent MACE_SMASH_GROUND_HEAVY = of("item.mace.smash_ground_heavy");

    private static SoundEvent of(String path) {
        return SoundEvent.of(Steamporium.id(path));
    }

    public static void init() {}
}

package net.rose.steamporium.common.init;

import net.minecraft.sound.SoundEvent;
import net.rose.steamporium.common.Steamporium;

public class ModSounds {
    public static final SoundEvent MACE_SMASH_AIR = of("item.mace.smash_air");
    public static final SoundEvent MACE_SMASH_GROUND = of("item.mace.smash_ground");
    public static final SoundEvent MACE_SMASH_GROUND_HEAVY = of("item.mace.smash_ground_heavy");

    public static final SoundEvent STEAMBOMB_THROW = of("item.steambomb.throw");
    public static final SoundEvent STEAMBOMB_EXPLOSION = of("item.steambomb.explosion");
    public static final SoundEvent STEAMBOMB_BOUNCE = of("item.steambomb.bounce");
    public static final SoundEvent STEAMBOMB_FUSE_LOOP = of("item.steambomb.fuse_loop");

    private static SoundEvent of(String path) {
        return SoundEvent.of(Steamporium.id(path));
    }

    public static void init() {}
}

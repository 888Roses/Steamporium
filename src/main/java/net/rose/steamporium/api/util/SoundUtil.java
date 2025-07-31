package net.rose.steamporium.api.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class SoundUtil {
    public static void playSound(
            @NotNull World world, Vec3d pos,
            @NotNull SoundEvent event, @NotNull SoundCategory category,
            float volume, float pitch
    ) {
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.playSound(
                    null, pos.x, pos.y, pos.z,
                    event, category,
                    volume, pitch
            );
        }
    }

    public static void playSound(
            @NotNull World world, Vec3d pos,
            @NotNull SoundEvent event, SoundCategory category
    ) {
        playSound(world, pos, event, category, 1F, 1F);
    }

    public static void playSoundInstance(PlayerEntity player, SoundEvent sound, float pitch, float volume) {
        if (player.getWorld().isClient) {
            playSoundInstanceInternal(player, sound, pitch, volume);
        }
    }

    @Environment(EnvType.CLIENT)
    private static void playSoundInstanceInternal(PlayerEntity player, SoundEvent sound, float pitch, float volume) {
        MinecraftClient.getInstance().getSoundManager().play(new PositionedSoundInstance(
                sound, SoundCategory.PLAYERS, volume, pitch, Random.create(),
                player.getBlockPos()
        ));
    }
}

package net.rose.steamporium.common.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class EntityAttachedSoundInstance extends MovingSoundInstance {
    private final Entity entity;

    public EntityAttachedSoundInstance(Entity entity, SoundEvent soundEvent, SoundCategory soundCategory) {
        super(soundEvent, soundCategory, SoundInstance.createRandom());
        this.entity = entity;
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.repeat = true;
        this.setPositionToEntity();
    }

    @Override
    public void tick() {
        if (this.entity == null || this.entity.isRemoved()) {
            this.setDone();
            return;
        }

        this.setPositionToEntity();

        final var mc = MinecraftClient.getInstance();
        final var player = mc.player;
        if (player == null) return;
        final var dst = this.entity.distanceTo(player);
        final var volume = MathHelper.clamp((10d - dst) / 10d, 0d, 1d);
        this.volume = ((float) volume) * 0.3f;
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    private void setPositionToEntity() {
        this.x = this.entity.getX();
        this.y = this.entity.getY();
        this.z = this.entity.getZ();
    }
}

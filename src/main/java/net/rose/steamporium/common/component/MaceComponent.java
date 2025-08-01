package net.rose.steamporium.common.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.rose.steamporium.api.component.ExtendedComponent;
import net.rose.steamporium.common.init.ModEntityComponents;

public class MaceComponent implements CommonTickingComponent, AutoSyncedComponent, ExtendedComponent<MaceComponent> {
    private int maceHitTime;
    private final PlayerEntity player;

    public MaceComponent(PlayerEntity player) {
        this.player = player;
    }

    public int getMaceHitTime() {
        return maceHitTime;
    }

    public void maceHit() {
        this.maceHitTime = 4;
        this.sync(this.player);
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    @Override
    public void clientTick() {
        this.player.sendMessage(Text.literal("Time: " + this.maceHitTime), true);
        if (this.maceHitTime > 0) {
            if (this.maceHitTime == 3) MinecraftClient.getInstance().getSoundManager().pauseAll();
            this.maceHitTime -= 1;
            if (this.maceHitTime == 0) MinecraftClient.getInstance().getSoundManager().resumeAll();
        }

        CommonTickingComponent.super.clientTick();
    }

    @Override
    public void tick() {
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.maceHitTime = tag.getInt("time");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("time", this.maceHitTime);
    }

    @Override
    public ComponentKey<MaceComponent> getComponent() {
        return ModEntityComponents.MACE;
    }
}

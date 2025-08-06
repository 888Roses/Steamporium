package net.rose.steamporium.common.init;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.rose.steamporium.common.Steamporium;

import java.util.function.Function;

public class ModParticles {
    public static final DefaultParticleType STEAM_EXPLOSION = of("steam_explosion");

    public static void init() {}

    private static DefaultParticleType of(String name) {
        var particle = FabricParticleTypes.simple();
        Registry.register(Registries.PARTICLE_TYPE, Steamporium.id(name), particle);
        return particle;
    }

    private static <T extends ParticleEffect> ParticleType<T> of(String name, boolean alwaysShow,
                                                                 ParticleEffect.Factory<T> factory,
                                                                 final Function<ParticleType<T>, Codec<T>> codecGetter
    ) {
        return Registry.register(
                Registries.PARTICLE_TYPE,
                Steamporium.id(name),
                new ParticleType<T>(alwaysShow, factory) {
                    public Codec<T> getCodec() {
                        return codecGetter.apply(this);
                    }
                });
    }
}

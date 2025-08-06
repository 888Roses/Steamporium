package net.rose.steamporium.client.init;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.rose.steamporium.common.init.ModParticles;

public class ModParticleFactories {
    public static void init() {
        var registry = ParticleFactoryRegistry.getInstance();
        registry.register(ModParticles.STEAM_EXPLOSION, ExplosionLargeParticle.Factory::new);
    }
}

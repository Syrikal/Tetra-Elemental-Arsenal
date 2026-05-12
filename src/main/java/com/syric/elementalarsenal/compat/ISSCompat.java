package com.syric.elementalarsenal.compat;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;

//This class and everything in it should only be referenced after checking whether Iron's Spells and Spellbooks is loaded!
public class ISSCompat {

    public static MobEffect getChargedEffect() {
        return MobEffectRegistry.CHARGED.get();
    }

    public static SimpleParticleType getElectricityParticle() {
        return ParticleRegistry.ELECTRICITY_PARTICLE.get();
    }

    public static SoundEvent getClearSound() {
        return SoundRegistry.CLEANSE_CAST.get();
    }

}

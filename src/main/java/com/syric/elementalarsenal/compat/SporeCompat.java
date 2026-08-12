package com.syric.elementalarsenal.compat;

import com.Harbinger.Spore.Core.Seffects;
import com.Harbinger.Spore.Sentities.ColdWeakness;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class SporeCompat {

    public static void frostbiteTarget(LivingEntity target) {
        if (target instanceof ColdWeakness) {
            MobEffectInstance instance = target.getEffect(Seffects.FROSTBITE.get());
            if (instance != null && target.getRandom().nextFloat() > 0.2) return;
            int intensity = instance == null ? 0 : instance.getAmplifier() + 1;
            intensity = Math.min(intensity, 2);
            target.addEffect(new MobEffectInstance(Seffects.FROSTBITE.get(), 20 * 60, intensity));
//            ElementalArsenal.LOGGER.debug("Applying Frostbite level {} to a {}", intensity + 1, target.getType().toShortString());
        }
    }

}

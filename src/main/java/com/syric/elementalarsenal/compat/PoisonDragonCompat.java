package com.syric.elementalarsenal.compat;

import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.menoxd.poisondragons.entity.PoisonDragonEntity;
import net.minecraft.world.damagesource.DamageSource;

public class PoisonDragonCompat {
    public static boolean isPoisonDamage(DamageSource source) {
//        ElementalArsenal.LOGGER.debug("Checking for poison damage...");
//        ElementalArsenal.LOGGER.debug("Damage is lightning-type: {}. Damage type is {}", source.is(IafDamageRegistry.DRAGON_LIGHTNING_TYPE), source.type().toString());
//        ElementalArsenal.LOGGER.debug("Source entity poison dragon: {}. Source entity is {}", source.getEntity() instanceof PoisonDragonEntity, source.getEntity().getType().toShortString());
        return source.is(IafDamageRegistry.DRAGON_LIGHTNING_TYPE) && source.getEntity() instanceof PoisonDragonEntity;
    }
}

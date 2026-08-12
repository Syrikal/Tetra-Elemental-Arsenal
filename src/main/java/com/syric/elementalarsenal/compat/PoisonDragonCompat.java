package com.syric.elementalarsenal.compat;

import com.iafenvoy.iceandfire.registry.IafDamageTypes;
import com.menoxd.poisondragons.entity.PoisonDragonEntity;
import net.minecraft.world.damagesource.DamageSource;

public class PoisonDragonCompat {
    public static boolean isPoisonDamage(DamageSource source) {
//        ElementalArsenal.LOGGER.debug("Checking for poison damage...");
//        ElementalArsenal.LOGGER.debug("Damage is lightning-type: {}. Damage type is {}", source.is(IafDamageRegistry.DRAGON_LIGHTNING_TYPE), source.type().toString());
//        ElementalArsenal.LOGGER.debug("Source entity poison dragon: {}. Source entity is {}", source.getEntity() instanceof PoisonDragonEntity, source.getEntity().getType().toShortString());
        return source.is(IafDamageTypes.DRAGON_LIGHTNING_TYPE) && source.getEntity() instanceof PoisonDragonEntity;
    }
}

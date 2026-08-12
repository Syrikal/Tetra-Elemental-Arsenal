package com.syric.elementalarsenal.compat;

import com.iafenvoy.iceandfire.registry.IafDamageTypes;
import com.syric.elementalarsenal.enums.TEADragonType;
import com.syric.elementalarsenal.tetra_effects.DragonProtectionStatGetter;
import com.syric.elementalarsenal.tetra_effects.Effects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.ModList;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

public class IFCompat {

    //Reduces dragon damage
    public static void dragonscaleShieldCheck(LivingHurtEvent event) {
        if ((event.getSource().is(IafDamageTypes.DRAGON_FIRE_TYPE)
                || event.getSource().is(IafDamageTypes.DRAGON_ICE_TYPE)
                || event.getSource().is(IafDamageTypes.DRAGON_LIGHTNING_TYPE))
                && event.getEntity() instanceof Player player) {
            double dragonscale_protection = 0;

            if (event.getEntity().getMainHandItem().getItem() instanceof ModularShieldItem) {
                dragonscale_protection = getProtectionLevel(player, event.getEntity().getMainHandItem(), event.getSource());
            }
            if (event.getEntity().getOffhandItem().getItem() instanceof ModularShieldItem) {
                dragonscale_protection = Math.max(dragonscale_protection, getProtectionLevel(player, event.getEntity().getOffhandItem(), event.getSource()));
            }

            if (dragonscale_protection > 0) {
//                ElementalArsenal.LOGGER.debug("Dragonscale protection reduced damage from {} to {}", event.getAmount(), (event.getAmount() * (1 - dragonscale_protection)));
                event.setAmount((float) (event.getAmount() * (1 - dragonscale_protection)));
            }
        }
    }

    //Gets the appropriate level of protection.
    private static double getProtectionLevel(Player player, ItemStack stack, DamageSource source) {
        DragonProtectionStatGetter dragonProtectionStatGetter = null;
        if (stack.getItem() instanceof ModularShieldItem) {
            if (source.is(IafDamageTypes.DRAGON_FIRE_TYPE)) {
                dragonProtectionStatGetter = new DragonProtectionStatGetter(TEADragonType.FIRE, Effects.FIRE_DRAGON_PROTECTION);
            } else if (source.is(IafDamageTypes.DRAGON_ICE_TYPE)) {
                dragonProtectionStatGetter = new DragonProtectionStatGetter(TEADragonType.ICE, Effects.ICE_DRAGON_PROTECTION);
            } else if (source.is(IafDamageTypes.DRAGON_LIGHTNING_TYPE)) {
                boolean poison = false;
                if (ModList.get().isLoaded("poison_dragons")) {
                    if (PoisonDragonCompat.isPoisonDamage(source)) poison = true;
                }
                if (poison) {
                    dragonProtectionStatGetter = new DragonProtectionStatGetter(TEADragonType.POISON, Effects.POISON_DRAGON_PROTECTION);
                } else {
                    dragonProtectionStatGetter = new DragonProtectionStatGetter(TEADragonType.LIGHTNING, Effects.LIGHTNING_DRAGON_PROTECTION);
                }
            }
        }
        if (dragonProtectionStatGetter == null) {
            return 0;
        } else {
            return Math.min(0.3, 0.01 * dragonProtectionStatGetter.getValue(player, stack));
        }
    }

}

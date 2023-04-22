package com.syric.teupnepa.effects;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;

/**
 * Implementation of a slowing effect.
 */
public class FireEffect {
    public static final ItemEffect fire = ItemEffect.get("upgradednetherite:fire");

    /**
     * Event handler which checks if the mainhand item has our item effect
     * @param event
     */
    @SubscribeEvent
    public void attackEvent(LivingHurtEvent event) {
//        Entity source = event.getSource().getEntity();
//
//        if (source instanceof Player player) {
//            ItemStack heldStack = player.getMainHandItem();
//
//            if (heldStack.getItem() instanceof ModularItem item) {
//                int level = item.getEffectLevel(heldStack, frostbite);
//
//                if (level > 0) {
//                    event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 2));
//                    //player.sendMessage(new StringTextComponent("Applied Slowness 2"), player.getUUID());
//                }
//            }
//        }
    }

}

package com.syric.teupnepa.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

public class FindShield {

    public static ItemStack getModularShield(LivingEntity entity) {
        if (entity.getUseItem().getItem() instanceof ModularShieldItem) {
            return entity.getUseItem();
        } else if (entity.getOffhandItem().getItem() instanceof ModularShieldItem) {
            return entity.getOffhandItem();
        } else if (entity.getMainHandItem().getItem() instanceof ModularShieldItem) {
            return entity.getMainHandItem();
        }
        return null;
    }

}

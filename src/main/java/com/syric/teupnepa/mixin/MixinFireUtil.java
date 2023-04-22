package com.syric.teupnepa.mixin;

import com.rolfmao.upgradednetherite.utils.tool.FireUtil;
import com.syric.teupnepa.effects.FireEffect;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import se.mickelus.tetra.items.modular.ModularItem;

@Mixin(FireUtil.class)
public class MixinFireUtil {

    @Redirect(method = "isFireToolOrWeapon", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isSameIgnoreDurability(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private static boolean isFireToolOrWeapon(ItemStack item, ItemStack targetItem) {

        if (ItemStack.isSameIgnoreDurability(item, targetItem)) {
            return true;
        }

        if (item.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) item.getItem();
            return modularItem.getEffectData(item).contains(FireEffect.fire);
        }

        return false;

    }

}

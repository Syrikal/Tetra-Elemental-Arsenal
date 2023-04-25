package com.syric.teupnepa.mixin;

import com.rolfmao.upgradednetherite.config.UpgradedNetheriteConfig;
import com.rolfmao.upgradednetherite.utils.tool.FireUtil;
import com.syric.teupnepa.ModularUtil;
import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.effects.FireEffect;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import se.mickelus.tetra.items.modular.ModularItem;

import static com.syric.teupnepa.ItemListUtil.*;

@Mixin(value = FireUtil.class, remap = false)
public abstract class MixinFireUtil {

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFireToolOrWeapon(ItemStack itemStack) {
        return isFireWeapon(itemStack) || isFireTool(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFireWeapon(ItemStack itemStack) {
        return isFireMeleeWeapon(itemStack) || isFireRangedWeapon(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFireMeleeWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableFireTool) { return false; }

        if (fire_netherite_weapons.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            if (modularItem.getEffectData(itemStack).contains(FireEffect.fire)) {
                TeUpNePa.LOGGER.info("Detected fire effect");
            }
            if(ModularUtil.isModularMeleeWeapon(itemStack)) {
                TeUpNePa.LOGGER.info("Detected modular melee weapon");
            }

            return modularItem.getEffectData(itemStack).contains(FireEffect.fire) && ModularUtil.isModularMeleeWeapon(itemStack);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFireRangedWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableFireTool) { return false; }

        if (fire_netherite_ranged.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return modularItem.getEffectData(itemStack).contains(FireEffect.fire) && ModularUtil.isModularRangedWeapon(itemStack);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFireTool(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableFireTool) { return false; }

        if (fire_netherite_tools.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return modularItem.getEffectData(itemStack).contains(FireEffect.fire) && ModularUtil.isModularTool(itemStack);
        }

        return false;
    }


    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFireShield(ItemStack itemStack) {

        if (fire_netherite_shield.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return modularItem.getEffectData(itemStack).contains(FireEffect.fire) && ModularUtil.isModularShield(itemStack);
        }

        return false;
    }
}

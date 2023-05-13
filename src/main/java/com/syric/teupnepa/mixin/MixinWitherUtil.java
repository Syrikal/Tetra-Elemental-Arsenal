package com.syric.teupnepa.mixin;

import com.rolfmao.upgradednetherite.config.UpgradedNetheriteConfig;
import com.rolfmao.upgradednetherite.utils.tool.WitherUtil;
import com.syric.teupnepa.ModularUtil;
import com.syric.teupnepa.effects.Effects;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import se.mickelus.tetra.items.modular.ModularItem;

import static com.syric.teupnepa.ItemListUtil.*;

@Mixin(value = WitherUtil.class, remap = false)
public abstract class MixinWitherUtil {

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isWitherToolOrWeapon(ItemStack itemStack) {
        return isWitherWeapon(itemStack) || isWitherTool(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isWitherWeapon(ItemStack itemStack) {
        return isWitherMeleeWeapon(itemStack) || isWitherRangedWeapon(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isWitherMeleeWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableWitherTool) { return false; }

        if (wither_netherite_weapons.contains(itemStack.getItem()) || ultimate_netherite_weapons.contains(itemStack.getItem())) {return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
//            if (modularItem.getEffectData(itemStack).contains(Effects.wither_weapon)) {
//                TeUpNePa.LOGGER.info("Detected wither effect");
//            }
//            if(ModularUtil.isModularMeleeWeapon(itemStack)) {
//                TeUpNePa.LOGGER.info("Detected modular melee weapon");
//            }

            return modularItem.getEffectData(itemStack).contains(Effects.wither_weapon) || modularItem.getEffectData(itemStack).contains(Effects.wither_both) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_weapon) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_both);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isWitherRangedWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableWitherTool) { return false; }

        if (wither_netherite_ranged.contains(itemStack.getItem()) || ultimate_netherite_ranged.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return (modularItem.getEffectData(itemStack).contains(Effects.wither) || modularItem.getEffectData(itemStack).contains(Effects.ultimate)) && ModularUtil.isModularRangedWeapon(itemStack);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isWitherTool(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableWitherTool) { return false; }

        if (wither_netherite_tools.contains(itemStack.getItem()) || ultimate_netherite_tools.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return modularItem.getEffectData(itemStack).contains(Effects.wither_tool) || modularItem.getEffectData(itemStack).contains(Effects.wither_both) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_tool) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_both);
        }

        return false;
    }


    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isWitherShield(ItemStack itemStack) {

        if (wither_netherite_shield.contains(itemStack.getItem()) || ultimate_netherite_shield.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return (modularItem.getEffectData(itemStack).contains(Effects.wither) || modularItem.getEffectData(itemStack).contains(Effects.ultimate)) && ModularUtil.isModularShield(itemStack);
        }

        return false;
    }
}

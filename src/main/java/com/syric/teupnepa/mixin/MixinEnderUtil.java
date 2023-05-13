package com.syric.teupnepa.mixin;

import com.rolfmao.upgradednetherite.config.UpgradedNetheriteConfig;
import com.rolfmao.upgradednetherite.utils.tool.EnderUtil;
import com.syric.teupnepa.ModularUtil;
import com.syric.teupnepa.effects.Effects;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import se.mickelus.tetra.items.modular.ModularItem;

import static com.syric.teupnepa.ItemListUtil.*;

@Mixin(value = EnderUtil.class, remap = false)
public abstract class MixinEnderUtil {

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isEnderToolOrWeapon(ItemStack itemStack) {
        return isEnderWeapon(itemStack) || isEnderTool(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isEnderWeapon(ItemStack itemStack) {
        return isEnderMeleeWeapon(itemStack) || isEnderRangedWeapon(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isEnderMeleeWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableEnderTool) { return false; }

        if (ender_netherite_weapons.contains(itemStack.getItem()) || ultimate_netherite_weapons.contains(itemStack.getItem())) {return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
//            if (modularItem.getEffectData(itemStack).contains(Effects.ender_weapon)) {
//                TeUpNePa.LOGGER.info("Detected ender effect");
//            }
//            if(ModularUtil.isModularMeleeWeapon(itemStack)) {
//                TeUpNePa.LOGGER.info("Detected modular melee weapon");
//            }

            return modularItem.getEffectData(itemStack).contains(Effects.ender_weapon) || modularItem.getEffectData(itemStack).contains(Effects.ender_both) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_weapon) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_both);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isEnderRangedWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableEnderTool) { return false; }

        if (ender_netherite_ranged.contains(itemStack.getItem()) || ultimate_netherite_ranged.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return (modularItem.getEffectData(itemStack).contains(Effects.ender) || modularItem.getEffectData(itemStack).contains(Effects.ultimate)) && ModularUtil.isModularRangedWeapon(itemStack);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isEnderTool(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableEnderTool) { return false; }

        if (ender_netherite_tools.contains(itemStack.getItem()) || ultimate_netherite_tools.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return modularItem.getEffectData(itemStack).contains(Effects.ender_tool) || modularItem.getEffectData(itemStack).contains(Effects.ender_both) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_tool) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_both);
        }

        return false;
    }


    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isEnderShield(ItemStack itemStack) {

        if (ender_netherite_shield.contains(itemStack.getItem()) || ultimate_netherite_shield.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return (modularItem.getEffectData(itemStack).contains(Effects.ender) || modularItem.getEffectData(itemStack).contains(Effects.ultimate)) && ModularUtil.isModularShield(itemStack);
        }

        return false;
    }
}

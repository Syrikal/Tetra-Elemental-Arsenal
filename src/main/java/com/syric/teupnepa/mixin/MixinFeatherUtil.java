package com.syric.teupnepa.mixin;

import com.rolfmao.upgradednetherite.config.UpgradedNetheriteConfig;
import com.rolfmao.upgradednetherite.utils.tool.FeatherUtil;
import com.syric.teupnepa.ModularUtil;
import com.syric.teupnepa.effects.Effects;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import se.mickelus.tetra.items.modular.ModularItem;

import static com.syric.teupnepa.ItemListUtil.*;

@Mixin(value = FeatherUtil.class, remap = false)
public abstract class MixinFeatherUtil {

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFeatherToolOrWeapon(ItemStack itemStack) {
        return isFeatherWeapon(itemStack) || isFeatherTool(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFeatherWeapon(ItemStack itemStack) {
        return isFeatherMeleeWeapon(itemStack) || isFeatherRangedWeapon(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFeatherMeleeWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableFeatherTool) { return false; }

        if (feather_netherite_weapons.contains(itemStack.getItem()) || ultimate_netherite_weapons.contains(itemStack.getItem())) {return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
//            if (modularItem.getEffectData(itemStack).contains(Effects.feather_weapon)) {
//                TeUpNePa.LOGGER.info("Detected feather effect");
//            }
//            if(ModularUtil.isModularMeleeWeapon(itemStack)) {
//                TeUpNePa.LOGGER.info("Detected modular melee weapon");
//            }

            return modularItem.getEffectData(itemStack).contains(Effects.feather_weapon) || modularItem.getEffectData(itemStack).contains(Effects.feather_both) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_weapon) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_both);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFeatherRangedWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableFeatherTool) { return false; }

        if (feather_netherite_ranged.contains(itemStack.getItem()) || ultimate_netherite_ranged.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return (modularItem.getEffectData(itemStack).contains(Effects.feather) || modularItem.getEffectData(itemStack).contains(Effects.ultimate)) && ModularUtil.isModularRangedWeapon(itemStack);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFeatherTool(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableFeatherTool) { return false; }

        if (feather_netherite_tools.contains(itemStack.getItem()) || ultimate_netherite_tools.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return modularItem.getEffectData(itemStack).contains(Effects.feather_tool) || modularItem.getEffectData(itemStack).contains(Effects.feather_both) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_tool) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_both);
        }

        return false;
    }


    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFeatherShield(ItemStack itemStack) {

        if (feather_netherite_shield.contains(itemStack.getItem()) || ultimate_netherite_shield.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return (modularItem.getEffectData(itemStack).contains(Effects.feather) || modularItem.getEffectData(itemStack).contains(Effects.ultimate)) && ModularUtil.isModularShield(itemStack);
        }

        return false;
    }
}

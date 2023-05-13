package com.syric.teupnepa.mixin;

import com.rolfmao.upgradednetherite.config.UpgradedNetheriteConfig;
import com.rolfmao.upgradednetherite.utils.tool.GoldUtil;
import com.syric.teupnepa.ModularUtil;
import com.syric.teupnepa.effects.Effects;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import se.mickelus.tetra.items.modular.ModularItem;

import static com.syric.teupnepa.ItemListUtil.*;

@Mixin(value = GoldUtil.class, remap = false)
public abstract class MixinGoldUtil {

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isGoldToolOrWeapon(ItemStack itemStack) {
        return isGoldWeapon(itemStack) || isGoldTool(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isGoldWeapon(ItemStack itemStack) {
        return isGoldMeleeWeapon(itemStack) || isGoldRangedWeapon(itemStack);
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isGoldMeleeWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableGoldTool) { return false; }

        if (gold_netherite_weapons.contains(itemStack.getItem()) || ultimate_netherite_weapons.contains(itemStack.getItem())) {return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
//            if (modularItem.getEffectData(itemStack).contains(Effects.gold_weapon)) {
//                TeUpNePa.LOGGER.info("Detected gold effect");
//            }
//            if(ModularUtil.isModularMeleeWeapon(itemStack)) {
//                TeUpNePa.LOGGER.info("Detected modular melee weapon");
//            }

            return modularItem.getEffectData(itemStack).contains(Effects.gold_weapon) || modularItem.getEffectData(itemStack).contains(Effects.gold_both) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_weapon) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_both);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isGoldRangedWeapon(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableGoldTool) { return false; }

        if (gold_netherite_ranged.contains(itemStack.getItem()) || ultimate_netherite_ranged.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return (modularItem.getEffectData(itemStack).contains(Effects.gold) || modularItem.getEffectData(itemStack).contains(Effects.ultimate)) && ModularUtil.isModularRangedWeapon(itemStack);
        }

        return false;
    }

    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isGoldTool(ItemStack itemStack) {

        if (!UpgradedNetheriteConfig.EnableGoldTool) { return false; }

        if (gold_netherite_tools.contains(itemStack.getItem()) || ultimate_netherite_tools.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return modularItem.getEffectData(itemStack).contains(Effects.gold_tool) || modularItem.getEffectData(itemStack).contains(Effects.gold_both) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_tool) || modularItem.getEffectData(itemStack).contains(Effects.ultimate_both);
        }

        return false;
    }


    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isGoldShield(ItemStack itemStack) {

        if (gold_netherite_shield.contains(itemStack.getItem()) || ultimate_netherite_shield.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            return (modularItem.getEffectData(itemStack).contains(Effects.gold) || modularItem.getEffectData(itemStack).contains(Effects.ultimate)) && ModularUtil.isModularShield(itemStack);
        }

        return false;
    }
}

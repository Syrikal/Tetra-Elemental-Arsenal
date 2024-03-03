package com.syric.teupnepa.mixin;

import com.rolfmao.upgradednetherite.config.UpgradedNetheriteConfig;
import com.rolfmao.upgradednetherite.utils.tool.FireUtil;
import com.syric.teupnepa.effects.Effects;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;
import se.mickelus.tetra.module.data.EffectData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.syric.teupnepa.util.ItemListUtil.*;

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

        if (fire_netherite_weapons.contains(itemStack.getItem()) || ultimate_netherite_weapons.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularBladedItem || itemStack.getItem() instanceof ModularDoubleHeadedItem || itemStack.getItem() instanceof ModularSingleHeadedItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = ModList.get().isLoaded("upgradednetherite_ultimate") ? Arrays.asList(Effects.fire_weapon, Effects.fire_both, Effects.ultimate_weapon, Effects.ultimate_both) : Arrays.asList(Effects.fire_weapon, Effects.fire_both);
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
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

        if (fire_netherite_ranged.contains(itemStack.getItem()) || ultimate_netherite_ranged.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularBowItem || itemStack.getItem() instanceof ModularCrossbowItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = ModList.get().isLoaded("upgradednetherite_ultimate") ? Arrays.asList(Effects.fire, Effects.ultimate) : Collections.singletonList(Effects.fire);
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
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

        if (fire_netherite_tools.contains(itemStack.getItem()) || ultimate_netherite_tools.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularBladedItem || itemStack.getItem() instanceof ModularDoubleHeadedItem || itemStack.getItem() instanceof ModularSingleHeadedItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = ModList.get().isLoaded("upgradednetherite_ultimate") ? Arrays.asList(Effects.fire_tool, Effects.fire_both, Effects.ultimate_tool, Effects.ultimate_both) : Arrays.asList(Effects.fire_tool, Effects.fire_both);
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
        }

        return false;
    }


    /**
     * @author Syric
     * @reason Simplify and generalize
     */
    @Overwrite
    public static boolean isFireShield(ItemStack itemStack) {

        if (fire_netherite_shield.contains(itemStack.getItem()) || ultimate_netherite_shield.contains(itemStack.getItem())) { return true; }

        if (itemStack.getItem() instanceof ModularShieldItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = ModList.get().isLoaded("upgradednetherite_ultimate") ? Arrays.asList(Effects.fire, Effects.ultimate) : Collections.singletonList(Effects.fire);
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
        }

        return false;
    }
}

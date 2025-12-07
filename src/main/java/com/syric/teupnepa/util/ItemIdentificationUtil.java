package com.syric.teupnepa.util;

import com.syric.teupnepa.UpgradeType;
import com.syric.teupnepa.effects.Effects;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
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
import java.util.List;

public class ItemIdentificationUtil {

    public static boolean isUpgradedToolOrWeapon(ItemStack itemStack, UpgradeType upgradeType) {
        return isUpgradedWeapon(itemStack, upgradeType) || isUpgradedTool(itemStack, upgradeType);
    }

    public static boolean isUpgradedWeapon(ItemStack itemStack, UpgradeType upgradeType) {
        return isUpgradedMeleeWeapon(itemStack, upgradeType) || isUpgradedRangedWeapon(itemStack, upgradeType);
    }

    public static boolean isUpgradedTool(ItemStack itemStack, UpgradeType upgradeType) {
        if (itemStack.getItem() instanceof ModularBladedItem || itemStack.getItem() instanceof ModularDoubleHeadedItem || itemStack.getItem() instanceof ModularSingleHeadedItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = Arrays.asList(upgradeType.tool, upgradeType.both, Effects.ultimate_tool, Effects.ultimate_both);
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
        }
        return false;
    }

    public static boolean isUpgradedMeleeWeapon(ItemStack itemStack, UpgradeType upgradeType) {
        if (itemStack.getItem() instanceof ModularBladedItem || itemStack.getItem() instanceof ModularDoubleHeadedItem || itemStack.getItem() instanceof ModularSingleHeadedItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = Arrays.asList(upgradeType.weapon, upgradeType.both, Effects.ultimate_weapon, Effects.ultimate_both);
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
        }
        return false;
    }

    public static boolean isUpgradedRangedWeapon(ItemStack itemStack, UpgradeType upgradeType) {
        if (itemStack.getItem() instanceof ModularBowItem || itemStack.getItem() instanceof ModularCrossbowItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = Arrays.asList(upgradeType.base, Effects.ultimate);
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
        }
        return false;
    }

    public static boolean isUpgradedShield(ItemStack itemStack, UpgradeType upgradeType) {
        if (itemStack.getItem() instanceof ModularShieldItem modularItem) {
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = Arrays.asList(upgradeType.base, Effects.ultimate);
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
        }
        return false;
    }

    public static boolean isUpgradedProjectile(AbstractArrow arrow, UpgradeType upgradeType) {
        switch (upgradeType) {
            case CORRUPT -> {
                return arrow.getTags().contains("CorruptUpgradedNetheriteBow");
            }
            case ENDER -> {
                return arrow.getTags().contains("EnderUpgradedNetheriteBow");
            }
            case FEATHER -> {
                return arrow.getTags().contains("FeatherUpgradedNetheriteBow");
            }
            case FIRE -> {
                return arrow.getTags().contains("FireUpgradedNetheriteBow");
            }
            case GOLD -> {
                return arrow.getTags().contains("GoldUpgradedNetheriteBow");
            }
            case PHANTOM -> {
                return arrow.getTags().contains("PhantomUpgradedNetheriteBow");
            }
            case POISON -> {
                return arrow.getTags().contains("PoisonUpgradedNetheriteBow");
            }
            case ULTIMATE -> {
                return arrow.getTags().contains("UltimateUpgradedNetheriteBow");
            }
            case WATER -> {
                return arrow.getTags().contains("WaterUpgradedNetheriteBow");
            }
            case WITHER -> {
                return arrow.getTags().contains("WitherUpgradedNetheriteBow");
            }
        }
        return false;
    }

}

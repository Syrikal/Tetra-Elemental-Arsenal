package com.syric.elementalarsenal.util;

import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.tetra_effects.Effects;
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

    public static boolean isUpgradedItem(ItemStack itemStack, UpgradeType upgradeType) {
        return itemStack.getItem() instanceof ModularItem && (isUpgradedToolOrWeapon(itemStack, upgradeType) || isUpgradedShield(itemStack, upgradeType));
    }

    public static boolean isUpgradedToolOrWeapon(ItemStack itemStack, UpgradeType upgradeType) {
        return isUpgradedWeapon(itemStack, upgradeType) || isUpgradedTool(itemStack, upgradeType);
    }

    public static boolean isUpgradedToolOrWeapon(ItemStack itemStack) {
        for (UpgradeType upgradeType : UpgradeType.values()) {
            if (isUpgradedToolOrWeapon(itemStack, upgradeType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUpgradedWeapon(ItemStack itemStack, UpgradeType upgradeType) {
        return isUpgradedMeleeWeapon(itemStack, upgradeType) || isUpgradedRangedWeapon(itemStack, upgradeType);
    }

    public static boolean isUpgradedWeapon(ItemStack itemStack) {
        for (UpgradeType upgradeType : UpgradeType.values()) {
            if (isUpgradedWeapon(itemStack, upgradeType)) {
                return true;
            }
        }
        return false;
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

    public static boolean isUpgradedTool(ItemStack itemStack) {
        for (UpgradeType upgradeType : UpgradeType.values()) {
            if (isUpgradedTool(itemStack, upgradeType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUpgradedMeleeWeapon(ItemStack itemStack, UpgradeType upgradeType) {
        if (itemStack.getItem() instanceof ModularBladedItem || itemStack.getItem() instanceof ModularDoubleHeadedItem || itemStack.getItem() instanceof ModularSingleHeadedItem) {
            ModularItem modularItem = (ModularItem) itemStack.getItem();
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = Arrays.asList(upgradeType.weapon, upgradeType.both, Effects.ultimate_weapon, Effects.ultimate_both);
//            ElementalArsenal.LOGGER.debug("Valid effects: ");
//            validEffects.stream().forEach(x -> ElementalArsenal.LOGGER.debug(x.getKey()));
//            ElementalArsenal.LOGGER.debug("Testing item effects...");
//            effectData.levelMap.entrySet().stream().forEach(x -> ElementalArsenal.LOGGER.debug(x.getKey().getKey()));
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
        }
        return false;
    }

    public static boolean isUpgradedMeleeWeapon(ItemStack itemStack) {
        for (UpgradeType upgradeType : UpgradeType.values()) {
            if (isUpgradedMeleeWeapon(itemStack, upgradeType)) {
                return true;
            }
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
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getItem() instanceof ModularShieldItem modularItem) {
            EffectData effectData = modularItem.getEffectData(itemStack);
            List<ItemEffect> validEffects = Arrays.asList(upgradeType.base, Effects.ultimate);
            return effectData.levelMap.entrySet().stream().anyMatch(entry -> validEffects.contains(entry.getKey()));
        }
        return false;
    }


    public static boolean isUpgradedShield(ItemStack itemStack) {
        for (UpgradeType upgradeType : UpgradeType.values()) {
            if (isUpgradedShield(itemStack, upgradeType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUpgradedProjectile(AbstractArrow arrow, UpgradeType upgradeType) {
        if (arrow.getTags().contains("UltimateImbuedArrow")) {
            return true;
        }
        switch (upgradeType) {
            case CORRUPT -> {
                return arrow.getTags().contains("CorruptImbuedArrow");
            }
            case ECHO -> {
                return arrow.getTags().contains("EchoImbuedArrow");
            }
            case ENDER -> {
                return arrow.getTags().contains("EnderImbuedArrow");
            }
            case FEATHER -> {
                return arrow.getTags().contains("FeatherImbuedArrow");
            }
            case FIRE -> {
                return arrow.getTags().contains("FireImbuedArrow");
            }
            case FROST -> {
                return arrow.getTags().contains("FrostImbuedArrow");
            }
            case LIGHTNING -> {
                return arrow.getTags().contains("LightningImbuedArrow");
            }
            case GOLD -> {
                return arrow.getTags().contains("GoldImbuedArrow");
            }
            case PHANTOM -> {
                return arrow.getTags().contains("PhantomImbuedArrow");
            }
            case POISON -> {
                return arrow.getTags().contains("PoisonImbuedArrow");
            }
            case ULTIMATE -> {
                return arrow.getTags().contains("UltimateImbuedArrow");
            }
            case WATER -> {
                return arrow.getTags().contains("WaterImbuedArrow");
            }
            case WITHER -> {
                return arrow.getTags().contains("WitherImbuedArrow");
            }
            case RADIANT -> {
                return arrow.getTags().contains("RadiantImbuedArrow");
            }
            case FORGOTTEN -> {
                return arrow.getTags().contains("ForgottenImbuedArrow");
            }
            case AETHERIC -> {
                return arrow.getTags().contains("AethericImbuedArrow");
            }
            case ARCANE -> {
                return arrow.getTags().contains("ArcaneImbuedArrow");
            }
        }
        return false;
    }

}

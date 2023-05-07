package com.syric.teupnepa;

import com.rolfmao.upgradednetherite.utils.tool.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;

public class ArrowUtil {

    public static void addTags(ItemStack bowStack, AbstractArrowEntity arrow, PlayerEntity player) {
        if (GoldUtil.isGoldRangedWeapon(bowStack)) {
            arrow.addTag("GoldUpgradedNetheriteBow");
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, bowStack) > 0) {
                arrow.getPersistentData().putInt("LootingGoldUpgradedNetheriteBow", EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, bowStack));
            }
        }

        if (FireUtil.isFireRangedWeapon(bowStack)) {
            arrow.addTag("FireUpgradedNetheriteBow");
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowStack) > 0) {
                arrow.addTag("FlameFireUpgradedNetheriteBow");
                if (bowStack.getItem() instanceof ModularCrossbowItem) {
                    arrow.setSecondsOnFire(100);
                }
            }
        }

        if (EnderUtil.isEnderRangedWeapon(bowStack)) {
            arrow.addTag("EnderUpgradedNetheriteBow");
            if (bowStack.getOrCreateTag().getBoolean("UpgradedNetherite_Tagged")) {
                arrow.getPersistentData().putIntArray("UpgradedNetherite_Position", bowStack.getOrCreateTag().getIntArray("UpgradedNetherite_Position"));
                arrow.getPersistentData().putString("UpgradedNetherite_Dimension", bowStack.getOrCreateTag().getString("UpgradedNetherite_Dimension"));
                arrow.getPersistentData().putBoolean("UpgradedNetherite_Tagged", bowStack.getOrCreateTag().getBoolean("UpgradedNetherite_Tagged"));
            }
        }

        if (WaterUtil.isWaterRangedWeapon(bowStack)) {
            arrow.addTag("WaterUpgradedNetheriteBow");
        }

        if (WitherUtil.isWitherRangedWeapon(bowStack)) {
            arrow.addTag("WitherUpgradedNetheriteBow");
        }

        if (PoisonUtil.isPoisonRangedWeapon(bowStack)) {
            arrow.addTag("PoisonUpgradedNetheriteBow");
        }

        if (PhantomUtil.isPhantomRangedWeapon(bowStack)) {
            arrow.addTag("PhantomUpgradedNetheriteBow");
        }

        if (FeatherUtil.isFeatherRangedWeapon(bowStack)) {
            arrow.addTag("FeatherUpgradedNetheriteBow");
        }

        if (CorruptUtil.isCorruptRangedWeapon(bowStack)) {
            arrow.addTag("CorruptUpgradedNetheriteBow");
            arrow.getPersistentData().putInt("LootingCorruptUpgradedNetheriteBow", CorruptUtil.intWearingCorruptArmor(player, true));
        }
    }

}

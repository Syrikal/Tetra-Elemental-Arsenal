package com.syric.teupnepa.util;

import com.rolfmao.upgradednetherite.config.UpgradedNetheriteConfig;
import com.rolfmao.upgradednetherite.utils.tool.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;

@Mod.EventBusSubscriber(
        modid = "teupnepa",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ArrowUtil {

    public static void addTags(ItemStack bowStack, AbstractArrowEntity arrow, PlayerEntity player) {
        if (GoldUtil.isGoldRangedWeapon(bowStack)) {
            arrow.addTag("GoldUpgradedNetheriteBow");
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, bowStack) > 0) {
                arrow.getPersistentData().putInt("LootingGoldUpgradedNetheriteBow", EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, bowStack));
            }
        }

        if (FireUtil.isFireRangedWeapon(bowStack)) {
//            TeUpNePa.LOGGER.debug("Adding 'FireUpgradedNetheriteBow' to arrow");
            arrow.addTag("FireUpgradedNetheriteBow");
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowStack) > 0) {
//                TeUpNePa.LOGGER.debug("Adding 'FlameFireUpgradedNetheriteBow' to arrow");
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

    @SubscribeEvent
    public void arrowHitEvent(LivingHurtEvent event) {
//        TeUpNePa.LOGGER.debug("Triggered arrowHitEvent");
        if (event.getSource().getEntity() instanceof PlayerEntity) {
//            TeUpNePa.LOGGER.debug("Source is a player");
            if (event.getEntityLiving().level.isClientSide) {
//                TeUpNePa.LOGGER.debug("source is client, terminating");
                return;
            }
//            TeUpNePa.LOGGER.debug("Triggering arrowHitEvent, entity is a player not on client");
            Entity directEntity = event.getSource().getDirectEntity();
            if (directEntity instanceof AbstractArrowEntity) {
                AbstractArrowEntity arrow = (AbstractArrowEntity) directEntity;

                if (PoisonUtil.isPoisonProjectile(arrow)) {
//                    TeUpNePa.LOGGER.debug("Poison arrow detected");
                    boolean poisonEnabled = UpgradedNetheriteConfig.EnablePoisonEffect;
                    boolean crit = arrow.isCritArrow();

//                    TeUpNePa.LOGGER.debug("Poison enabled: " + poisonEnabled + ", crit: " + crit);

                    if (crit && poisonEnabled) {
//                        TeUpNePa.LOGGER.debug("Applying poison");
                        event.getEntityLiving().addEffect(new EffectInstance(Effects.POISON, 140, 0, false, true, true));
                    }
                }
                if (WitherUtil.isWitherProjectile(arrow)) {
//                    TeUpNePa.LOGGER.debug("Wither arrow detected");
                    boolean witherEnabled = UpgradedNetheriteConfig.EnableWitherEffect;
                    boolean crit = arrow.isCritArrow();

//                    TeUpNePa.LOGGER.debug("Wither enabled: " + witherEnabled + ", crit: " + crit);

                    if (crit && witherEnabled) {
//                        TeUpNePa.LOGGER.debug("Applying wither");
                        event.getEntityLiving().addEffect(new EffectInstance(Effects.WITHER, 200, 0, false, true, true));
                    }
                }
            }
        }
    }

}

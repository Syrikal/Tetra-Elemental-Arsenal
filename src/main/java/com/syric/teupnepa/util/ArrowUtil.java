package com.syric.teupnepa.util;


import com.syric.teupnepa.enums.UpgradeType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;

@Mod.EventBusSubscriber(
        modid = "teupnepa",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ArrowUtil {

    public static void addTags(ItemStack bowStack, AbstractArrow arrow, Player player) {
        if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.GOLD)) {
            arrow.addTag("GoldUpgradedNetheriteBow");
            if (bowStack.getEnchantmentLevel(Enchantments.MOB_LOOTING) > 0) {
                arrow.getPersistentData().putInt("LootingGoldUpgradedNetheriteBow", bowStack.getEnchantmentLevel(Enchantments.MOB_LOOTING));
            }
        }

        if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FIRE)) {
//            TeUpNePa.LOGGER.debug("Adding 'FireUpgradedNetheriteBow' to arrow");
            arrow.addTag("FireUpgradedNetheriteBow");
            if (bowStack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
//                TeUpNePa.LOGGER.debug("Adding 'FlameFireUpgradedNetheriteBow' to arrow");
                arrow.addTag("FlameFireUpgradedNetheriteBow");
                if (bowStack.getItem() instanceof ModularCrossbowItem) {
                    arrow.setSecondsOnFire(100);
                }
            }
        }

        if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.ENDER)) {
            arrow.addTag("EnderUpgradedNetheriteBow");
            if (bowStack.getOrCreateTag().getBoolean("UpgradedNetherite_Tagged")) {
                arrow.getPersistentData().putIntArray("UpgradedNetherite_Position", bowStack.getOrCreateTag().getIntArray("UpgradedNetherite_Position"));
                arrow.getPersistentData().putString("UpgradedNetherite_Dimension", bowStack.getOrCreateTag().getString("UpgradedNetherite_Dimension"));
                arrow.getPersistentData().putBoolean("UpgradedNetherite_Tagged", bowStack.getOrCreateTag().getBoolean("UpgradedNetherite_Tagged"));
            }
        }

        if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.WATER)) {
            arrow.addTag("WaterUpgradedNetheriteBow");
        }

        if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.WITHER)) {
            arrow.addTag("WitherUpgradedNetheriteBow");
        }

        if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.POISON)) {
            arrow.addTag("PoisonUpgradedNetheriteBow");
        }

        if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.PHANTOM)) {
            arrow.addTag("PhantomUpgradedNetheriteBow");
        }

        if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FEATHER)) {
            arrow.addTag("FeatherUpgradedNetheriteBow");
        }

        if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.CORRUPT)) {
            arrow.addTag("CorruptUpgradedNetheriteBow");
            //TODO figure out some way of handling Corrupt stuff
//            arrow.getPersistentData().putInt("LootingCorruptUpgradedNetheriteBow", CorruptUtil.intWearingCorruptArmor(player, true));
            arrow.getPersistentData().putInt("LootingCorruptUpgradedNetheriteBow", 0);
        }
    }

    @SubscribeEvent
    public void arrowHitEvent(LivingHurtEvent event) {
//        TeUpNePa.LOGGER.debug("Triggered arrowHitEvent");
        if (event.getSource().getEntity() instanceof Player) {
//            TeUpNePa.LOGGER.debug("Source is a player");
            if (event.getEntity().level().isClientSide) {
//                TeUpNePa.LOGGER.debug("source is client, terminating");
                return;
            }
//            TeUpNePa.LOGGER.debug("Triggering arrowHitEvent, entity is a player not on client");
            Entity directEntity = event.getSource().getDirectEntity();
            if (directEntity instanceof AbstractArrow arrow) {

                if (ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.POISON)) {
//                    TeUpNePa.LOGGER.debug("Poison arrow detected");
//                    boolean poisonEnabled = UpgradedNetheriteConfig.EnablePoisonEffect;
                    boolean crit = arrow.isCritArrow();

//                    TeUpNePa.LOGGER.debug("Poison enabled: " + poisonEnabled + ", crit: " + crit);

                    if (crit) {
//                        TeUpNePa.LOGGER.debug("Applying poison");
                        event.getEntity().addEffect(new MobEffectInstance(MobEffects.POISON, 140, 0, false, true, true));
                    }
                }
                if (ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.WITHER)) {
//                    TeUpNePa.LOGGER.debug("Wither arrow detected");
//                    boolean witherEnabled = UpgradedNetheriteConfig.EnableWitherEffect;
                    boolean witherEnabled = true;
                    boolean crit = arrow.isCritArrow();

//                    TeUpNePa.LOGGER.debug("Wither enabled: " + witherEnabled + ", crit: " + crit);

                    if (crit) {
//                        TeUpNePa.LOGGER.debug("Applying wither");
                        event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0, false, true, true));
                    }
                }
            }
        }
    }

}

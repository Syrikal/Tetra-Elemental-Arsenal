package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.network.PacketHandler;
import com.syric.teupnepa.network.S2CWaterArrowTagPacket;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import com.syric.teupnepa.util.SendMessageUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.event.ModularLooseProjectilesEvent;
import se.mickelus.tetra.event.ModularProjectileSpawnEvent;
import se.mickelus.tetra.items.modular.ModularItem;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class AddArrowTags {

    @SubscribeEvent
    public void arrowLooseEvent(ModularLooseProjectilesEvent event) {
        ItemStack bowStack = event.getFiringStack();
        if (event.getFiringStack().getItem() instanceof ModularItem ) {
//            TeUpNePa.LOGGER.debug("Firing ModularLooseProjectilesEvent on the " + (event.getShooter().level().isClientSide ? "client" : "server") + " side");
//            if (event.getShooter().level().isClientSide) SendMessageUtil.sendMessage(event.getShooter(), "Triggered arrowLooseEvent");

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.GOLD)) {
                SendMessageUtil.sendMessage(event.getShooter(), "Gold Upgrade");
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("GoldUpgradedNetheriteBow");
                    return arrow;
                });
                if (bowStack.getEnchantmentLevel(Enchantments.MOB_LOOTING) > 0) {
                    event.addProjectileRemapper(arrow -> {
                        arrow.getPersistentData().putInt("LootingGoldUpgradedNetheriteBow", bowStack.getEnchantmentLevel(Enchantments.MOB_LOOTING));
                        return arrow;
                    });
                }
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FIRE)) {
//            TeUpNePa.LOGGER.debug("Adding 'FireUpgradedNetheriteBow' to arrow");
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("FireUpgradedNetheriteBow");
                    return arrow;
                });
                if (bowStack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
//                TeUpNePa.LOGGER.debug("Adding 'FlameFireUpgradedNetheriteBow' to arrow");
                    event.addProjectileRemapper(arrow -> {
                        arrow.addTag("FlameFireUpgradedNetheriteBow");
                        return arrow;
                    });
                }
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.ENDER)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("EnderUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.WATER)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("WaterUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.ECHO)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("EchoUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.WITHER)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("WitherUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.POISON)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("PoisonUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.PHANTOM)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("PhantomUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FEATHER) && FeatherUpgrade.isActive(bowStack)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("FeatherUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.CORRUPT)) {
                //TODO figure out some way of handling Corrupt stuff
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("CorruptUpgradedNetheriteBow");
                    arrow.getPersistentData().putInt("LootingCorruptUpgradedNetheriteBow", 0);
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.RADIANT)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("RadiantUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FORGOTTEN)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("ForgottenUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.AETHERIC)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("AethericUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FROST)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("FrostUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.ARCANE)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("ArcaneUpgradedNetheriteBow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.LIGHTNING)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("LightningUpgradedNetheriteBow");
                    return arrow;
                });
            }
        }
    }

    @SubscribeEvent
    public void projectileSpawn(ModularProjectileSpawnEvent event) {
        if (!event.getLevel().isClientSide() && ItemIdentificationUtil.isUpgradedProjectile(event.getProjectileEntity(), UpgradeType.WATER)) {
            PacketHandler.sendWithEntity(new S2CWaterArrowTagPacket(event.getProjectileEntity().getId()), event.getProjectileEntity());
        }
    }

}

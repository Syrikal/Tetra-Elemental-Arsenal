package com.syric.elementalarsenal.events;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.network.PacketHandler;
import com.syric.elementalarsenal.network.S2CArrowTagPacket;
import com.syric.elementalarsenal.upgrade_types.FeatherUpgrade;
import com.syric.elementalarsenal.upgrade_types.LightningUpgrade;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import com.syric.elementalarsenal.util.SendMessageUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.event.ModularLooseProjectilesEvent;
import se.mickelus.tetra.event.ModularProjectileSpawnEvent;
import se.mickelus.tetra.items.modular.ModularItem;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class AddArrowTags {

    @SubscribeEvent
    public void arrowLooseEvent(ModularLooseProjectilesEvent event) {
        ItemStack bowStack = event.getFiringStack();
        if (event.getFiringStack().getItem() instanceof ModularItem ) {
//            ElementalArsenal.LOGGER.debug("Firing ModularLooseProjectilesEvent on the " + (event.getShooter().level().isClientSide ? "client" : "server") + " side");
//            if (event.getShooter().level().isClientSide) SendMessageUtil.sendMessage(event.getShooter(), "Triggered arrowLooseEvent");

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.GOLD)) {
                SendMessageUtil.sendMessage(event.getShooter(), "Gold Upgrade");
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("GoldImbuedArrow");
                    return arrow;
                });
                if (bowStack.getEnchantmentLevel(Enchantments.MOB_LOOTING) > 0) {
                    event.addProjectileRemapper(arrow -> {
                        arrow.getPersistentData().putInt("LootingGoldImbuedArrow", bowStack.getEnchantmentLevel(Enchantments.MOB_LOOTING));
                        return arrow;
                    });
                }
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FIRE)) {
//            ElementalArsenal.LOGGER.debug("Adding 'FireImbuedArrow' to arrow");
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("FireImbuedArrow");
                    return arrow;
                });
                if (bowStack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
//                ElementalArsenal.LOGGER.debug("Adding 'FlameFireImbuedArrow' to arrow");
                    event.addProjectileRemapper(arrow -> {
                        arrow.addTag("FlameFireImbuedArrow");
                        return arrow;
                    });
                }
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.ENDER)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("EnderImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.WATER)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("WaterImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.ECHO)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("EchoImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.WITHER)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("WitherImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.POISON)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("PoisonImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.PHANTOM)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("PhantomImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FEATHER) && FeatherUpgrade.isActive(bowStack)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("FeatherImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.CORRUPT)) {
                //TODO figure out some way of handling Corrupt stuff
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("CorruptImbuedArrow");
                    arrow.getPersistentData().putInt("LootingCorruptImbuedArrow", 0);
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.RADIANT)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("RadiantImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FORGOTTEN)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("ForgottenImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.AETHERIC)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("AethericImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.FROST)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("FrostImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.ARCANE)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("ArcaneImbuedArrow");
                    return arrow;
                });
            }

            if (ItemIdentificationUtil.isUpgradedRangedWeapon(bowStack, UpgradeType.LIGHTNING)) {
                event.addProjectileRemapper(arrow -> {
                    arrow.addTag("LightningImbuedArrow");
                    return arrow;
                });

                if (event.getShooter().hasEffect(LightningUpgrade.getChargedEffect())) {
                    MobEffectInstance instance = event.getShooter().getEffect(LightningUpgrade.getChargedEffect());
                    if (instance != null) {
                        int chargeLevel = instance.getAmplifier() + 1;
                        event.addProjectileRemapper(arrow -> {
                            arrow.addTag("ChargedArrow_" + chargeLevel);
                            return arrow;
                        });
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void projectileSpawn(ModularProjectileSpawnEvent event) {
        if (!event.getLevel().isClientSide() && ItemIdentificationUtil.isUpgradedProjectile(event.getProjectileEntity(), UpgradeType.WATER)) {
            PacketHandler.sendWithEntity(new S2CArrowTagPacket(event.getProjectileEntity().getId(), 0), event.getProjectileEntity());
        }
        if (!event.getLevel().isClientSide() && ItemIdentificationUtil.isUpgradedProjectile(event.getProjectileEntity(), UpgradeType.FEATHER)) {
            PacketHandler.sendWithEntity(new S2CArrowTagPacket(event.getProjectileEntity().getId(), 1), event.getProjectileEntity());
        }
        if (!event.getLevel().isClientSide() && ItemIdentificationUtil.isUpgradedProjectile(event.getProjectileEntity(), UpgradeType.RADIANT)) {
            PacketHandler.sendWithEntity(new S2CArrowTagPacket(event.getProjectileEntity().getId(), 2), event.getProjectileEntity());
        }
    }

}

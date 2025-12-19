package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.UpgradeType;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.ModularItem;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ShieldBlockEvent {

    @SubscribeEvent
    public void mineEvent(net.minecraftforge.event.entity.living.ShieldBlockEvent event) {
        LivingEntity defender = event.getEntity();
        Entity attacker = event.getDamageSource().getDirectEntity();
        ItemStack shield = event.getEntity().getUseItem();

        if (shield.getItem() instanceof ModularItem && ItemIdentificationUtil.isUpgradedShield(shield)) {
            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.GOLD)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.FIRE)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.ENDER)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.WATER)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.WITHER)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.POISON)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.PHANTOM)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.FEATHER)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.ECHO)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.CORRUPT)) {

            }

            if (ItemIdentificationUtil.isUpgradedShield(shield, UpgradeType.RADIANT)) {

            }
        }
    }


}

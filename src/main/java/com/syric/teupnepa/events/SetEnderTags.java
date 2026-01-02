package com.syric.teupnepa.events;

import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = "teupnepa",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class SetEnderTags {

    @SubscribeEvent
    public void RightClickEvent(PlayerInteractEvent.RightClickBlock event) {
//        TeUpNePa.LOGGER.debug("RightClickEvent Detected");
        Player player = event.getEntity();
        ItemStack itemStack = player.getItemInHand(event.getHand());

        boolean crouching = player.isCrouching();
        boolean isEnderItem = ItemIdentificationUtil.isUpgradedToolOrWeapon(itemStack, UpgradeType.ENDER);
        boolean notOnCooldown = !player.getCooldowns().isOnCooldown(itemStack.getItem());

        if (crouching && isEnderItem && notOnCooldown) {

//            TeUpNePa.LOGGER.debug("Attempting to set ender tags");
//            ItemUseContext context = new ItemUseContext(player, event.getHand(), event.getHitVec());
//            ActionResultType actionResultType = EnderUtil.EnderSetTag(context);
//            TeUpNePa.LOGGER.debug("Action result type: " + actionResultType);
        }

    }

}

package com.syric.teupnepa.events;

import com.rolfmao.upgradednetherite.utils.tool.EnderUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
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
        PlayerEntity player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand(event.getHand());

        boolean crouching = player.isCrouching();
        boolean isEnderItem = EnderUtil.isEnderToolOrWeapon(itemStack);
        boolean notOnCooldown = !player.getCooldowns().isOnCooldown(itemStack.getItem());

        if (crouching && isEnderItem && notOnCooldown) {

//            TeUpNePa.LOGGER.debug("Attempting to set ender tags");
            ItemUseContext context = new ItemUseContext(player, event.getHand(), event.getHitVec());
            ActionResultType actionResultType = EnderUtil.EnderSetTag(context);
//            TeUpNePa.LOGGER.debug("Action result type: " + actionResultType);
        }

    }

}

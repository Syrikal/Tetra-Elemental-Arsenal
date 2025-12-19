package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.UpgradeType;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.ModularItem;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class MineSpeedEvent {


    @SubscribeEvent
    public void mineEvent(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        Block block = event.getState().getBlock();
        ItemStack tool = event.getEntity().getMainHandItem();

        boolean below_y_0 = player.getY() < 0;
        boolean in_end = player.level().dimensionTypeId() == BuiltinDimensionTypes.END;
        boolean in_water = player.isInWater();

        if (tool.getItem() instanceof ModularItem && ItemIdentificationUtil.isUpgradedTool(tool)) {
            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.GOLD)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.FIRE)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.ENDER)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.WATER)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.WITHER)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.POISON)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.PHANTOM)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.FEATHER)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.ECHO)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.CORRUPT)) {

            }

            if (ItemIdentificationUtil.isUpgradedTool(tool, UpgradeType.RADIANT)) {

            }
        }
    }

}

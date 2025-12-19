package com.syric.teupnepa.mixin;

import com.syric.teupnepa.UpgradeType;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SculkSensorBlockEntity.VibrationUser.class)
public class MixinSculkSensorBlockEntity {

    @Inject(method = "canReceiveVibration", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelVibration(ServerLevel level, BlockPos pos, GameEvent event, GameEvent.Context context, CallbackInfoReturnable<Boolean> cir) {
        boolean block_break = event == GameEvent.BLOCK_DESTROY;
        if (block_break && context.sourceEntity() instanceof LivingEntity living) {
            ItemStack itemStack = living.getMainHandItem();
            if (ItemIdentificationUtil.isUpgradedTool(itemStack, UpgradeType.ECHO)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

}

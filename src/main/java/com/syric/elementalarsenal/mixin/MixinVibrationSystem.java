package com.syric.elementalarsenal.mixin;

import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationSystem.Listener.class)
public abstract class MixinVibrationSystem {

    @Inject(method = "handleGameEvent", at = @At("HEAD"), cancellable = true)
    private void cancelEchoToolVibrations(ServerLevel level, GameEvent event, GameEvent.Context context, Vec3 vec3, CallbackInfoReturnable<Boolean> cir) {
        if (event == GameEvent.BLOCK_DESTROY
                && context.sourceEntity() instanceof LivingEntity livingEntity
                && ItemIdentificationUtil.isUpgradedTool(livingEntity.getMainHandItem(), UpgradeType.ECHO)) {
            cir.setReturnValue(false);
            cir.cancel();
//        } else if (event == GameEvent.BLOCK_PLACE
//                && context.sourceEntity() instanceof LivingEntity livingEntity
//                && (ItemIdentificationUtil.isUpgradedTool(livingEntity.getMainHandItem(), UpgradeType.ECHO) || ItemIdentificationUtil.isUpgradedTool(livingEntity.getOffhandItem(), UpgradeType.ECHO))) {
//            cir.setReturnValue(false);
//            cir.cancel();
        } else if (event == GameEvent.HIT_GROUND
                && context.sourceEntity() instanceof ItemEntity itemEntity
                && itemEntity.getTags().contains("EchoUpgradeMuffled")) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }


}

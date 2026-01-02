package com.syric.teupnepa.mixin;

//@Mixin(value = Warden.VibrationUser.class)
//public class MixinWarden {
//
//    @Inject(method = "canReceiveVibration", at = @At("HEAD"), cancellable = true, remap = false)
//    private void cancelVibration(ServerLevel level, BlockPos pos, GameEvent event, GameEvent.Context context, CallbackInfoReturnable<Boolean> cir) {
//        boolean block_break = event == GameEvent.BLOCK_DESTROY;
//        if (block_break && context.sourceEntity() instanceof LivingEntity living) {
//            ItemStack itemStack = living.getMainHandItem();
//            if (ItemIdentificationUtil.isUpgradedTool(itemStack, UpgradeType.ECHO)) {
//                cir.setReturnValue(false);
//                cir.cancel();
//            }
//        }
//    }
//
//}

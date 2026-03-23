package com.syric.elementalarsenal.mixin;

import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity {

    @Unique
    private ItemEntity self() {
        return (ItemEntity) (Object) this;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void rescueFromVoid(CallbackInfo ci) {
        if (self().getY() < self().level().getMinBuildHeight() && ItemIdentificationUtil.isUpgradedItem(self().getItem(), UpgradeType.ENDER)) {
            self().revive();
            self().teleportTo(self().getX(), self().level().getMinBuildHeight() + 10, self().getZ());
            self().setNoGravity(true);
            self().setDeltaMovement(0, 0, 0);
            self().setGlowingTag(true);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void clearMuffledTag(CallbackInfo ci) {
        if (self().onGround()) {
            self().getItem().removeTagKey("EchoUpgradeMuffled");
        }
    }

}

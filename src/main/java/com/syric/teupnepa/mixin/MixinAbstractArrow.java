package com.syric.teupnepa.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syric.teupnepa.upgrade_types.LightningUpgrade.chargeArrow;
import static com.syric.teupnepa.upgrade_types.LightningUpgrade.particleCheck;

@Mixin(AbstractArrow.class)
public abstract class MixinAbstractArrow {

    @Shadow
    protected boolean inGround;

    @Unique
    private AbstractArrow self() {
        return (AbstractArrow) (Object) this;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;tick()V", shift = At.Shift.AFTER))
    private void tick(CallbackInfo ci) {

        chargeArrow(self());
        particleCheck(self(), this.inGround);

    }

}

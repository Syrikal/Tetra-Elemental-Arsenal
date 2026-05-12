package com.syric.elementalarsenal.mixin;

import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syric.elementalarsenal.upgrade_types.FeatherUpgrade.arrowWeightless;
import static com.syric.elementalarsenal.upgrade_types.LightningUpgrade.chargeArrow;
import static com.syric.elementalarsenal.upgrade_types.LightningUpgrade.particleCheck;
import static com.syric.elementalarsenal.upgrade_types.RadiantUpgrade.radiantStormbreak;

@Mixin(AbstractArrow.class)
public abstract class MixinAbstractArrow {

    @Shadow
    protected boolean inGround;

    @Shadow
    public int shakeTime;

    @Shadow
    protected abstract void tickDespawn();

    @Unique
    private AbstractArrow self() {
        return (AbstractArrow) (Object) this;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;tick()V", shift = At.Shift.AFTER))
    private void tick(CallbackInfo ci) {

        chargeArrow(self());
        particleCheck(self(), this.inGround);

        radiantStormbreak(self());

        arrowWeightless(self());
        if (ItemIdentificationUtil.isUpgradedProjectile(self(), UpgradeType.FEATHER) && self().getDeltaMovement().length() < 0.1f) {
            this.tickDespawn();
        }

    }

}

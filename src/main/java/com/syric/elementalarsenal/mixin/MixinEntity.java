package com.syric.elementalarsenal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @ModifyReturnValue(method = "isInWater", at = @At("RETURN"))
    private boolean arrowsIgnoreWater(boolean original) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof AbstractArrow arrow && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.WATER)) {
            return false;
        } else {
            return original;
        }
    }

}

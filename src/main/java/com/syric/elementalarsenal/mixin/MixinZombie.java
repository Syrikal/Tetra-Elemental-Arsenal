package com.syric.elementalarsenal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.util.FindShield;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Zombie.class)
public class MixinZombie {

    @WrapOperation(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zombie;isOnFire()Z"))
    private boolean shouldIgnite(Zombie instance, Operation<Boolean> original, @Local(argsOnly = true) Entity target) {
        return original.call(instance) && !(target instanceof LivingEntity livingEntity && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(livingEntity), UpgradeType.FIRE));
    }

}

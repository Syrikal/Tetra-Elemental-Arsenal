package com.syric.teupnepa.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitherBoss.class)
public abstract class MixinWither {

    @Unique
    private WitherBoss self() {
        return (WitherBoss) (Object) this;
    }

    @WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;isPowered()Z"))
    private boolean isShieldEffective(WitherBoss instance, Operation<Boolean> original, @Local(argsOnly = true) DamageSource source) {
        return original.call(instance) && !(source.getDirectEntity() instanceof AbstractArrow arrow && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.RADIANT));
    }


}

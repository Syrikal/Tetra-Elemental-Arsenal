package com.syric.teupnepa.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderMan.class)
public abstract class MixinEnderMan {

    @WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean suppressTeleport(DamageSource instance, TagKey<DamageType> tag, Operation<Boolean> original) {
        return original.call(instance, tag) && !(instance.getDirectEntity() instanceof AbstractArrow arrow && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.ENDER));
    }

}

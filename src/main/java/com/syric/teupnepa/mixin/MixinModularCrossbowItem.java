package com.syric.teupnepa.mixin;

import com.syric.teupnepa.util.ArrowUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;

@Mixin(value = ModularCrossbowItem.class, remap = false)
public class MixinModularCrossbowItem {

    @Inject(method = "fireProjectile", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/AbstractArrowEntity;setCritArrow(Z)V",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void applyTags(World world, ItemStack crossbowStack, ItemStack ammoStack, PlayerEntity player, double yaw, CallbackInfo ci, double strength, float velocityBonus, float projectileVelocity, ArrowItem ammoItem, AbstractArrowEntity projectile) {
        ArrowUtil.addTags(crossbowStack, projectile, player);
    }

}

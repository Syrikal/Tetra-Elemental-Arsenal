package com.syric.teupnepa.mixin;

import com.syric.teupnepa.util.ArrowUtil;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;

@Mixin(value = ModularCrossbowItem.class)
public class MixinModularCrossbowItem {

    @Inject(method = "fireProjectile", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setCritArrow(Z)V",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void applyTags(Level world, net.minecraft.world.item.ItemStack crossbowStack, net.minecraft.world.item.ItemStack ammoStack, Player player, double yaw, boolean isDupe, CallbackInfo ci, double strength, float velocityBonus, float projectileVelocity, net.minecraft.world.item.ArrowItem ammoItem, AbstractArrow projectile) {
        ArrowUtil.addTags(crossbowStack, projectile, player);
    }

}

package com.syric.teupnepa.mixin;

import com.syric.teupnepa.ArrowUtil;
import net.minecraft.entity.LivingEntity;
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
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;

@Mixin(value = ModularBowItem.class, remap = false)
public class MixinModularBowItem {

    @Inject(method = "fireArrow", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/AbstractArrowEntity;shootFromRotation(Lnet/minecraft/entity/Entity;FFFFF)V",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void applyTags(ItemStack itemStack, World world, LivingEntity entity, int timeLeft, CallbackInfo ci, PlayerEntity player, ItemStack ammoStack, boolean playerInfinite, int drawProgress, double strength, float velocityBonus, int suspendLevel, float projectileVelocity, ArrowItem ammoItem, boolean infiniteAmmo, int count, double spread, int powerLevel, int punchLevel, int flameLevel, int piercingLevel, int i, double yaw, AbstractArrowEntity projectile) {

        ArrowUtil.addTags(itemStack, projectile, player);

    }

}

package com.syric.elementalarsenal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Inventory.class)
public abstract class MixinInventory {

    @Shadow
    @Final
    public Player player;

    @WrapOperation(method = "dropAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean ignoreEchoItems(ItemStack instance, Operation<Boolean> original) {
        Player player = this.player;
        return original.call(instance) || (player.isDeadOrDying() && ItemIdentificationUtil.isUpgradedItem(instance, UpgradeType.ECHO));
    }

}

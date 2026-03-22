package com.syric.teupnepa.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ApplyBonusCount.class)
public abstract class MixinApplyBonusCount {

    @ModifyVariable(method = "run", at = @At("STORE"), ordinal = 0)
    private int modifiedFortuneLevel(int value, @Local(ordinal = 1) ItemStack toolStack, @Local(ordinal = 0, argsOnly = true)LootContext context) {
        if (ItemIdentificationUtil.isUpgradedTool(toolStack, UpgradeType.GOLD)) {
            return value + (value >= 3 ? 2 : 1);
        }
        if (ItemIdentificationUtil.isUpgradedTool(toolStack, UpgradeType.ENDER)
                && context.getLevel().dimension().location().getPath().equals("the_end")) {
            return value + 1;
        }
        return value;
    }

}

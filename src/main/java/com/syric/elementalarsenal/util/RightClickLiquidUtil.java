package com.syric.elementalarsenal.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class RightClickLiquidUtil {

    public static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluidContext) {
        float xRot = player.getXRot();
        float yRot = player.getYRot();
        Vec3 eyePos = player.getEyePosition();
        float f2 = Mth.cos(-yRot * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-yRot * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-xRot * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-xRot * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getBlockReach();
        Vec3 vec31 = eyePos.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return level.clip(new ClipContext(eyePos, vec31, ClipContext.Block.OUTLINE, fluidContext, player));
    }

}

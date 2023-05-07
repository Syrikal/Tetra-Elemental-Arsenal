package com.syric.teupnepa.mixin;

import com.rolfmao.upgradednetherite.utils.tool.CorruptUtil;
import com.syric.teupnepa.TeUpNePa;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
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

        if (false) {
            projectile.addTag("GoldUpgradedNetheriteBow");
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, itemStack) > 0) {
                projectile.getPersistentData().putInt("LootingGoldUpgradedNetheriteBow", EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, itemStack));
            }
        }

        if (MixinFireUtil.isFireRangedWeapon(itemStack)) {
            TeUpNePa.LOGGER.debug("Tested for being a fire bow: " + itemStack.toString());
            TeUpNePa.LOGGER.debug("Detected modular fire bow");
            projectile.addTag("FireUpgradedNetheriteBow");
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) > 0) {
                projectile.addTag("FlameFireUpgradedNetheriteBow");
            }
        }

        if (false) {
            projectile.addTag("EnderUpgradedNetheriteBow");
            if (itemStack.getOrCreateTag().getBoolean("UpgradedNetherite_Tagged")) {
                projectile.getPersistentData().putIntArray("UpgradedNetherite_Position", itemStack.getOrCreateTag().getIntArray("UpgradedNetherite_Position"));
                projectile.getPersistentData().putString("UpgradedNetherite_Dimension", itemStack.getOrCreateTag().getString("UpgradedNetherite_Dimension"));
                projectile.getPersistentData().putBoolean("UpgradedNetherite_Tagged", itemStack.getOrCreateTag().getBoolean("UpgradedNetherite_Tagged"));
            }
        }

        if (false) {
            projectile.addTag("WaterUpgradedNetheriteBow");
        }

        if (false) {
            projectile.addTag("WitherUpgradedNetheriteBow");
        }

        if (false) {
            projectile.addTag("PoisonUpgradedNetheriteBow");
        }

        if (false) {
            projectile.addTag("PhantomUpgradedNetheriteBow");
        }

        if (false) {
            projectile.addTag("FeatherUpgradedNetheriteBow");
        }

        if (false) {
            projectile.addTag("CorruptUpgradedNetheriteBow");
            projectile.getPersistentData().putInt("LootingCorruptUpgradedNetheriteBow", CorruptUtil.intWearingCorruptArmor(player, true));
        }

    }

}

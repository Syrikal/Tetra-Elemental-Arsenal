package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.util.FindShield;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import com.syric.teupnepa.util.RightClickLiquidUtil;
import com.syric.teupnepa.util.SendMessageUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class WaterUpgrade {

    @SubscribeEvent
    public static void waterAttack(LivingHurtEvent event) {
//        TeUpNePa.LOGGER.debug("LivingHurtEvent triggered");
//        TeUpNePa.LOGGER.debug("ClientSide?: " + event.getEntity().level().isClientSide + "; Damage Type Correct?: " + event.getSource().is(DamageTypes.MOB_ATTACK) + "; Attacker Living? " + (event.getSource().getEntity() instanceof LivingEntity));
//        TeUpNePa.LOGGER.debug("Damage type: " + event.getSource().type());

        if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                && !event.getSource().isIndirect()
                && event.getSource().getDirectEntity() instanceof LivingEntity
                && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.WATER)) {

            if (event.getEntity().getType().fireImmune() || event.getEntity().isSensitiveToWater()) {
//                    TeUpNePa.LOGGER.debug("+20% damage due to target type");
                SendMessageUtil.triggered(UpgradeType.WATER, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            } else if (event.getEntity().isInWaterOrBubble() && event.getSource().getDirectEntity().isInWaterOrBubble()) {
//                    TeUpNePa.LOGGER.debug("+10% damage due to being in water");
                SendMessageUtil.triggered(UpgradeType.WATER, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.1F);
            }
            if (event.getEntity().isOnFire() && event.getEntity().getRandom().nextFloat() < 0.5F) {
                event.getEntity().extinguishFire();
                event.getEntity().playSound(SoundEvents.LAVA_EXTINGUISH, 0.3F, 1);
                event.getEntity().playSound(SoundEvents.GENERIC_SPLASH);
            }

        } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                && event.getSource().getDirectEntity() instanceof Arrow arrow
                && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.WATER)) {
//                TeUpNePa.LOGGER.debug("Passed ranged checks, increasing damage");
            if (event.getEntity().getType().fireImmune() || event.getEntity().isSensitiveToWater()) {
//                    TeUpNePa.LOGGER.debug("+20% damage due to target type");
                SendMessageUtil.triggered(UpgradeType.WATER, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            } else if (event.getEntity().isInWaterOrBubble() && event.getSource().getDirectEntity().isInWaterOrBubble()) {
//                    TeUpNePa.LOGGER.debug("+10% damage due to being in water");
                SendMessageUtil.triggered(UpgradeType.WATER, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.1F);
            }
            if (event.getEntity().isOnFire() && ((Arrow) event.getSource().getDirectEntity()).isCritArrow() && event.getEntity().getRandom().nextFloat() < 0.5F) {
                event.getEntity().extinguishFire();
                event.getEntity().playSound(SoundEvents.LAVA_EXTINGUISH, 0.3F, 1);
                event.getEntity().playSound(SoundEvents.GENERIC_SPLASH);
            }
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (!event.getEntity().isCreative()
            && event.getEntity().isEyeInFluidType(ForgeMod.WATER_TYPE.get())
            && ItemIdentificationUtil.isUpgradedTool(event.getEntity().getMainHandItem(), UpgradeType.WATER)) {

//            TeUpNePa.LOGGER.debug("Detected candidate for water mining speed boost");

            boolean aqua_affinity = EnchantmentHelper.hasAquaAffinity(event.getEntity());
            boolean on_ground = event.getEntity().onGround();
            float multiplier = 1.2F;

            multiplier *= aqua_affinity ? 1 : 5;
            multiplier *= on_ground ? 1 : 2.5F;

//            TeUpNePa.LOGGER.debug("Final multiplier: " + multiplier);

            event.setNewSpeed(event.getOriginalSpeed() * multiplier);
        }
    }

    //Shield damages water-vulnerable enemies
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.WATER)) {
//            TeUpNePa.LOGGER.debug("Water shield detected");
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            if (attacker instanceof LivingEntity livingAttacker && (livingAttacker.getType().fireImmune() || livingAttacker.isSensitiveToWater()) && defender.getRandom().nextFloat() < 0.5) {
//                TeUpNePa.LOGGER.debug("Water shield retaliation activated");
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 4 + 2);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.WATER, defender);
            }
        }
    }

    @SubscribeEvent
    public static void rightClickEvent(PlayerInteractEvent.RightClickItem event) {
//        TeUpNePa.LOGGER.debug("UseItemEvent detected");
//        TeUpNePa.LOGGER.debug("\nWater upgraded: " + ItemIdentificationUtil.isUpgradedTool(event.getItemStack(), UpgradeType.WATER));
        if (ItemIdentificationUtil.isUpgradedTool(event.getItemStack(), UpgradeType.WATER)) {

//            TeUpNePa.LOGGER.debug("Passed preliminary checks for water use");

            Player player = event.getEntity();
            Level level = player.level();
            //Find right-clicked block
            BlockHitResult liquidHitResult = RightClickLiquidUtil.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
//            TeUpNePa.LOGGER.debug("Block hit: " + level.getBlockState(liquidHitResult.getBlockPos()));
            if (level.getBlockState(liquidHitResult.getBlockPos()).is(Blocks.LAVA)) {
//                TeUpNePa.LOGGER.debug("Triggering obsidian effect");
                event.setCanceled(true);
                level.setBlock(liquidHitResult.getBlockPos(), Blocks.OBSIDIAN.defaultBlockState(), Block.UPDATE_CLIENTS);
                level.playSound(player, liquidHitResult.getBlockPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1);
                if (!player.isCreative()) {
//                    TeUpNePa.LOGGER.debug("Damaging item used");
                    event.getItemStack().hurtAndBreak(6, event.getEntity(), (x) -> {});
                }
            }


        }
    }

}

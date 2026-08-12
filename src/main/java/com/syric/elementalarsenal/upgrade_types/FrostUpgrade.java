package com.syric.elementalarsenal.upgrade_types;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.compat.SporeCompat;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.registry.EATags;
import com.syric.elementalarsenal.util.FindShield;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import com.syric.elementalarsenal.util.RightClickLiquidUtil;
import com.syric.elementalarsenal.util.SendMessageUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class FrostUpgrade {

    @SubscribeEvent
    public static void frostAttack(LivingHurtEvent event) {

        if (!event.getEntity().level().isClientSide) {
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.FROST)) {

                if (event.getEntity().getType().is(EATags.EntityTypes.FROST_DAMAGED)) {
//                    ElementalArsenal.LOGGER.debug("+20% damage due to target type");
                    SendMessageUtil.triggered(UpgradeType.FROST, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                }

                boolean fullStrengthHit = !(event.getSource().getDirectEntity() instanceof Player) || ((Player) event.getSource().getDirectEntity()).getAttackStrengthScale(0) >= 0.95;
                if (!event.getEntity().isBlocking() && fullStrengthHit) {
                    event.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
                    if (ModList.get().isLoaded("spore")) {
                        SporeCompat.frostbiteTarget(event.getEntity());
                    }
                    int freezeDuration = getFreezeDuration(event.getEntity(), 1F);
                    event.getEntity().setTicksFrozen(event.getEntity().getTicksFrozen() + freezeDuration);
//                    ElementalArsenal.LOGGER.debug("Increased target's freeze duration from " + (event.getEntity().getTicksFrozen() - freezeDuration) + " to " + event.getEntity().getTicksFrozen() + " ticks (+" + freezeDuration + ")");
                }

            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.FROST)) {

                if (event.getEntity().getType().is(EATags.EntityTypes.FROST_DAMAGED)) {
//                    ElementalArsenal.LOGGER.debug("+20% damage due to target type");
                    SendMessageUtil.triggered(UpgradeType.FROST, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                }
                if (!event.getEntity().isBlocking() && ((Arrow) event.getSource().getDirectEntity()).isCritArrow()) {
                    event.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
                    if (ModList.get().isLoaded("spore")) {
                        SporeCompat.frostbiteTarget(event.getEntity());
                    }
                    int freezeDuration = getFreezeDuration(event.getEntity(), 2.5F);
                    event.getEntity().setTicksFrozen(event.getEntity().getTicksFrozen() + freezeDuration);
//                    ElementalArsenal.LOGGER.debug("Increased target's freeze duration from " + (event.getEntity().getTicksFrozen() - freezeDuration) + " to " + event.getEntity().getTicksFrozen() + " ticks (+" + freezeDuration + ")");
                }
            }
        }
    }

    //Shield damages frost-vulnerable enemies
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.FROST)) {
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            if (attacker != null && attacker.getType().is(EATags.EntityTypes.FROST_DAMAGED) && defender.getRandom().nextFloat() < 0.33) {
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 4 + 2);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.FROST, defender);
            }
        }
    }

    @SubscribeEvent
    public static void rightClickEvent(PlayerInteractEvent.RightClickItem event) {
        if (ItemIdentificationUtil.isUpgradedTool(event.getItemStack(), UpgradeType.FROST)) {

//            ElementalArsenal.LOGGER.debug("Passed preliminary checks for water use");

            Player player = event.getEntity();
            Level level = player.level();
            boolean clientSide = level.isClientSide;
            //Find right-clicked block
            BlockHitResult liquidHitResult = RightClickLiquidUtil.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            level.getBlockState(liquidHitResult.getBlockPos());
//            ElementalArsenal.LOGGER.debug("Block hit: " + level.getBlockState(liquidHitResult.getBlockPos()));
            if (level.getBlockState(liquidHitResult.getBlockPos()).is(Blocks.WATER)) {
//                ElementalArsenal.LOGGER.debug("Triggering obsidian effect");
                event.setCanceled(true);
                if (clientSide) {
                    level.playSound(player, liquidHitResult.getBlockPos(), SoundEvents.PLAYER_HURT_FREEZE, SoundSource.BLOCKS, 0.5F, 1.3F);
                } else {
                    level.setBlock(liquidHitResult.getBlockPos(), Blocks.ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                    if (!player.isCreative()) {
//                    ElementalArsenal.LOGGER.debug("Damaging item used");
                        event.getItemStack().hurtAndBreak(2, event.getEntity(), (x) -> {
                        });
                    }
                }


            } else if (level.getBlockState(liquidHitResult.getBlockPos()).is(Blocks.ICE)) {
                event.setCanceled(true);
                if (clientSide) {
                    level.playSound(player, liquidHitResult.getBlockPos(), SoundEvents.PLAYER_HURT_FREEZE, SoundSource.BLOCKS, 0.5F, 1);
                } else {
                    level.setBlock(liquidHitResult.getBlockPos(), Blocks.PACKED_ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                    if (!player.isCreative()) {
//                    ElementalArsenal.LOGGER.debug("Damaging item used");
                        event.getItemStack().hurtAndBreak(6, event.getEntity(), (x) -> {
                        });
                    }
                }
            } else if (level.getBlockState(liquidHitResult.getBlockPos()).is(Blocks.PACKED_ICE)) {
                event.setCanceled(true);
                if (clientSide) {
                    level.playSound(player, liquidHitResult.getBlockPos(), SoundEvents.PLAYER_HURT_FREEZE, SoundSource.BLOCKS, 0.5F, 0.7F);
                } else {
                    level.setBlock(liquidHitResult.getBlockPos(), Blocks.BLUE_ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                    if (!player.isCreative()) {
//                    ElementalArsenal.LOGGER.debug("Damaging item used");
                        event.getItemStack().hurtAndBreak(18, event.getEntity(), (x) -> {
                        });
                    }
                }
            }
        }
    }

    private static int getFreezeDuration(Entity target, float multiplier) {
        int existing = target.getTicksFrozen();

        if (target.getType().is(EATags.EntityTypes.FUNGAL)) {
            return (int) Mth.clamp(3 * existing, 140 * multiplier, 600);
        } else if (target.getType().is(EATags.EntityTypes.FROST_DAMAGED)) {
            return (int) Mth.clamp(3 * existing, 110 * multiplier, 450);
        } else {
            return (int) Mth.clamp(3 * existing, 80 * multiplier, 300);
        }
    }

}

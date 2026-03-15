package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.registry.TUNPMobEffects;
import com.syric.teupnepa.registry.TUNPTags;
import com.syric.teupnepa.util.FindShield;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import com.syric.teupnepa.util.SendMessageUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class EnderUpgrade {

    @SubscribeEvent
    public static void teleportCancellation(EntityTeleportEvent event) {
        if (event.getEntity() instanceof LivingEntity livingEntity && livingEntity.hasEffect(TUNPMobEffects.DIMENSIONAL_ANCHOR_EFFECT.get())) {
            event.setCanceled(true);
            MobEffectInstance dimensional_anchor = livingEntity.getEffect(TUNPMobEffects.DIMENSIONAL_ANCHOR_EFFECT.get());
            if (dimensional_anchor != null) {
                int damage = 2 * dimensional_anchor.getAmplifier();
                livingEntity.hurt(livingEntity.damageSources().magic(), damage);
            }
        }
    }

    //Weapons have increased damage against ender mobs or in the end
    @SubscribeEvent
    public static void enderAttack(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity().getType().is(TUNPTags.EntityTypes.END_NATIVE) || event.getEntity().level().dimension().location().getPath().equals("the_end")) {
                if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                        && !event.getSource().isIndirect()
                        && event.getSource().getDirectEntity() instanceof LivingEntity
                        && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.ENDER)) {
                    SendMessageUtil.triggered(UpgradeType.ENDER, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                        && event.getSource().getDirectEntity() instanceof Arrow
                        && event.getSource().getDirectEntity().getTags().contains("EnderUpgradedNetheriteBow")) {
                    SendMessageUtil.triggered(UpgradeType.ENDER, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                }
            }
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.ENDER)) {
                getAnchoredIdiot(event.getEntity());
            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow
                    && event.getSource().getDirectEntity().getTags().contains("EnderUpgradedNetheriteBow")) {
                getAnchoredIdiot(event.getEntity());
            }
        }
    }

    //Shield anchors attackers
    @SubscribeEvent
    public static void enderBlock(LivingHurtEvent event) {
        if (event.getEntity().isUsingItem()
                && ItemIdentificationUtil.isUpgradedShield(event.getEntity().getUseItem(), UpgradeType.ENDER)) {
            Entity attacker = event.getSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            if (attacker instanceof LivingEntity livingAttacker) {
                getAnchoredIdiot(livingAttacker);
                SendMessageUtil.triggered(UpgradeType.ENDER, event.getEntity());
            } else if (attacker instanceof Projectile projectile) {
                Entity shooter = projectile.getOwner();
                if (shooter instanceof LivingEntity livingShooter
                        && defender.getTicksUsingItem() < 10) {

                    if (defender instanceof Player player) {
                        player.playNotifySound(SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }

                    event.setAmount(0);

                    int duration = (int) Mth.lerp(Mth.clamp(defender.distanceTo(shooter), 0, 15) /15, 0, 60);
                    if (!event.getEntity().level().isClientSide()) {
                        randomTeleport(livingShooter, defender, duration);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void enderMine(PlayerEvent.BreakSpeed event) {
        if (!event.getEntity().isCreative()
                && event.getEntity().level().dimension().location().getPath().equals("the_end")
                && ItemIdentificationUtil.isUpgradedTool(event.getEntity().getMainHandItem(), UpgradeType.ENDER)) {
            event.setNewSpeed(event.getOriginalSpeed() * 1.5F);
        }
    }

    //Handles looting on mobs
    @SubscribeEvent
    public static void LootingMobEvent(LootingLevelEvent event) {
        assert event.getDamageSource() != null;
        if (!event.getEntity().level().isClientSide
                && event.getDamageSource().getDirectEntity() != null
                && event.getEntity().getType().is(TUNPTags.EntityTypes.END_NATIVE)) {
            if (event.getDamageSource().getDirectEntity() instanceof Arrow arrow) {
                if (arrow.getTags().contains("EnderUpgradedNetheriteBow")) {
                    event.setLootingLevel(Math.min(1, event.getLootingLevel() + 1));
                    SendMessageUtil.triggered(UpgradeType.ENDER, event.getDamageSource().getEntity());
                }
            } else if (event.getDamageSource().getDirectEntity() instanceof LivingEntity livingEntity
                        && livingEntity.getMainHandItem().getItem() instanceof ModularItem
                        && ItemIdentificationUtil.isUpgradedMeleeWeapon(livingEntity.getMainHandItem(), UpgradeType.ENDER)) {
                event.setLootingLevel(event.getLootingLevel() + 1);
                SendMessageUtil.triggered(UpgradeType.ENDER, event.getDamageSource().getEntity());
            }
        }
    }


    private static void getAnchoredIdiot(LivingEntity entity) {
        if (entity.hasEffect(TUNPMobEffects.DIMENSIONAL_ANCHOR_EFFECT.get())) {
            MobEffectInstance anchorInstance = entity.getEffect(TUNPMobEffects.DIMENSIONAL_ANCHOR_EFFECT.get());
            if (anchorInstance != null) {
                int level = anchorInstance.getAmplifier();
                int new_level = Math.min(level + 1, 3);
                entity.addEffect(new MobEffectInstance(TUNPMobEffects.DIMENSIONAL_ANCHOR_EFFECT.get(), 200, new_level));
            }
        } else {
            entity.addEffect(new MobEffectInstance(TUNPMobEffects.DIMENSIONAL_ANCHOR_EFFECT.get(), 200, 0));
        }
    }

    private static void randomTeleport(LivingEntity victim, LivingEntity target, int duration) {
        if (!victim.level().isClientSide) {
            Vec3 originalDisplacementVector = target.position().vectorTo(victim.position());

            double d0 = target.getX();
            double d1 = target.getY();
            double d2 = target.getZ();

            for(int i = 0; i < 16; i++) {
                double d3 = target.getX() + (victim.getRandom().nextDouble() - 0.5D) * 8.0D;
                double d4 = Mth.clamp(target.getY() + (double)(victim.getRandom().nextInt(8) - 4), (double)victim.level().getMinBuildHeight(), (double)(victim.level().getMinBuildHeight() + ((ServerLevel)victim.level()).getLogicalHeight() - 1));
                double d5 = target.getZ() + (victim.getRandom().nextDouble() - 0.5D) * 8.0D;
                if (victim.isPassenger()) {
                    victim.stopRiding();
                }

                Vec3 potentialDisplacementVector = target.position().vectorTo(new Vec3(d3, d4, d5));

                if (originalDisplacementVector.normalize().dot(potentialDisplacementVector.normalize()) < 0.6) {
                    continue;
                }

                Vec3 vec3 = victim.position();
                victim.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(victim));
                net.minecraftforge.event.entity.EntityTeleportEvent.ChorusFruit event = net.minecraftforge.event.ForgeEventFactory.onChorusFruitTeleport(victim, d3, d4, d5);
                if (victim.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                    SoundEvent soundevent = victim instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    victim.level().playSound((Player)null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    victim.playSound(soundevent, 1.0F, 1.0F);


                    victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 4));
                    victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, 1));
                    victim.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration, 1));
                    if (victim instanceof Mob mob) {
                        mob.setTarget(null);
                    }

                    break;
                }
            }
        }
    }

}

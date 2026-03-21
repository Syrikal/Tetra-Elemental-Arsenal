package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.compat.ISSCompat;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.registry.TUNPMobEffects;
import com.syric.teupnepa.registry.TUNPSounds;
import com.syric.teupnepa.util.FindShield;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import com.syric.teupnepa.util.SendMessageUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class LightningUpgrade {

    //Weapons have increased damage in inclement weather
    //Weapons shock when wielder is charged
    @SubscribeEvent
    public static void lightningAttack(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide) {

            float damage_boost = event.getEntity().level().isRaining() ? 0.1F : (event.getEntity().level().isThundering() ? 0.2F : 0);
            boolean shock = false;
            int shockLevel = 0;
            int shockDuration = event.getEntity().level().isRaining() ? 200 : (event.getEntity().level().isThundering() ? 300 : 100);

            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity livingEntity
                    && (ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.LIGHTNING)
                    || ItemIdentificationUtil.isUpgradedTool(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.LIGHTNING))) {
                if (livingEntity.hasEffect(getChargedEffect())) {
                    damage_boost *= 2;
                    shock = true;
                    shockLevel = getChargedLevel(livingEntity);
                }
                event.setAmount(event.getAmount() * (1 + damage_boost));
                SendMessageUtil.triggered(UpgradeType.LIGHTNING, event.getSource().getEntity());
            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.LIGHTNING)) {
                int chargeLevel = getChargedLevel(arrow);
                if (chargeLevel > 0) {
                    damage_boost *= 2;
                    shock = true;
                    shockLevel = chargeLevel;
                }
                event.setAmount(event.getAmount() * (1 + damage_boost));
                SendMessageUtil.triggered(UpgradeType.LIGHTNING, event.getSource().getEntity());
            }
            if (shock) {
                event.getEntity().addEffect(new MobEffectInstance(TUNPMobEffects.SHOCKED_EFFECT.get(), shockDuration, shockLevel));
            }
        }
    }

    //TODO delete the arrow after
    //Arrows with the "LightningStrike" tag spawn lightning on impact
    @SubscribeEvent
    public static void arrowImpact(ProjectileImpactEvent event) {
        if (event.getProjectile().level() instanceof ServerLevel serverLevel
                && event.getProjectile().getTags().contains("LightningStrike")
                && event.getProjectile().getTags().contains("LightningUpgradedNetheriteBow")) {
            EntityType.LIGHTNING_BOLT.spawn(serverLevel, event.getProjectile().getOnPos(), MobSpawnType.TRIGGERED);
        }
    }

    //Shield reduces lightning damage and Charges the player
    @SubscribeEvent
    public static void lightningResistance(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypes.LIGHTNING_BOLT)
            && (ItemIdentificationUtil.isUpgradedShield(event.getEntity().getMainHandItem(), UpgradeType.LIGHTNING)
                || ItemIdentificationUtil.isUpgradedShield(event.getEntity().getOffhandItem(), UpgradeType.LIGHTNING))) {

            event.setAmount(event.getAmount() * 0.4F);

            if (event.getEntity().isUsingItem()
                    && event.getEntity() instanceof Player player
                    && ItemIdentificationUtil.isUpgradedShield(event.getEntity().getUseItem(), UpgradeType.LIGHTNING)) {
                int chargedLevel = 1;
                int chargedDuration = 300;
                if (player.getTicksUsingItem() < 15) {
                    chargedLevel = 2;
                    chargedDuration = 600;
                    event.setAmount(event.getAmount() * 0.5F);
                    player.playNotifySound(SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0F, 1.0F);
                }

                if (!event.getEntity().level().isClientSide()) {
                    int existingChargedLevel = getChargedLevel(event.getEntity());
                    int applyChargeLevel = Math.min(existingChargedLevel + chargedLevel, 4);
                    event.getEntity().addEffect(new MobEffectInstance(getChargedEffect(), chargedDuration, applyChargeLevel - 1));
                }

            }
        }
    }

    //Sneak-block detonates Charged
    @SubscribeEvent
    public static void shockwaveBlock(ShieldBlockEvent event) {
        if (event.getEntity().isCrouching()
                && event.getEntity().hasEffect(getChargedEffect())
                && FindShield.getModularShield(event.getEntity()) != null
                && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.LIGHTNING)) {
            int chargedLevel = getChargedLevel(event.getEntity());
            double radius = Math.max(2, 1.5 * chargedLevel);
            float damage = 3 * chargedLevel;
            LivingEntity blocker = event.getEntity();

            if (!event.getEntity().level().isClientSide()) {
                AABB search_box = blocker.getBoundingBox().inflate(radius, radius / 2, radius);
                blocker.level().getEntities(blocker, search_box).stream()
                        .filter(targetEntity -> targetEntity instanceof LivingEntity)
                        .filter(targetEntity -> targetEntity.distanceTo(blocker) < radius)
                        .filter(targetEntity -> !(targetEntity instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwnerUUID() == blocker.getUUID()))
                        .forEach(targetEntity -> {
                            LivingEntity livingTarget = (LivingEntity) targetEntity;
                            livingTarget.hurt(targetEntity.damageSources().lightningBolt(), damage);
                            livingTarget.knockback(0.3 * chargedLevel, blocker.getX() - livingTarget.getX(), blocker.getZ() - livingTarget.getZ());
                            livingTarget.setDeltaMovement(livingTarget.getDeltaMovement().add(new Vec3(0, 0.15 * chargedLevel, 0)));
                            if (chargedLevel > 1) {
                                livingTarget.addEffect(new MobEffectInstance(TUNPMobEffects.SHOCKED_EFFECT.get(), 50 * chargedLevel, chargedLevel - 2));
                            }
                        });
                //TODO Reduce scaling and buff level 1
            }

            blocker.level().playSound(null, blocker.getX(), blocker.getY(), blocker.getZ(), TUNPSounds.LIGHTNING_SHOCKWAVE.get(), SoundSource.PLAYERS, (0.8F + 0.2F * chargedLevel), 1.0F);

            blocker.removeEffect(getChargedEffect());

            if (blocker.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < Math.pow(chargedLevel, 2); i++) {
                    for (int j = 0; j < 16; j++) {
                        serverLevel.sendParticles(getSparkParticle(),
                                blocker.getX() + serverLevel.getRandom().nextGaussian(),
                                blocker.getY() + blocker.getBbHeight() * 0.3 + serverLevel.getRandom().nextGaussian() / 9,
                                blocker.getZ() + serverLevel.getRandom().nextGaussian(),
                                1, 0.0, 0.0, 0.0, 0.0);
                    }
                }
            }

        }
    }


    //Needs to be called by a mixin every tick on an arrow. Charges arced arrows with a lightning strike.
    public static void chargeArrow(AbstractArrow arrow) {
        if (!arrow.level().isClientSide()
            && arrow.getOwner() instanceof LivingEntity shooter
            && Math.abs(arrow.getDeltaMovement().y()) < 0.1F
            && !arrow.getTags().contains("LightningStrikeFailed")
            && !arrow.getTags().contains("LightningStrike")
            && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.LIGHTNING)) {
            boolean raining = arrow.level().isRaining();
            boolean thunder = arrow.level().isThundering();

            double vertical_distance = Math.abs(arrow.getY() - shooter.getY());
            double min_vertical_distance = thunder ? 50 : raining ? 55 : 60;
//            min_vertical_distance = 0;

            double altitude = arrow.getY();
            double min_altitude = thunder ? 100 : raining ? 120 : 150;
//            min_altitude = 0;

            if (vertical_distance > min_vertical_distance
                && altitude > min_altitude) {

                int chargedLevel = getChargedLevel(arrow);
                double base_chance = thunder ? 0.75 : raining ? 0.5 : 0.25;
                double adjustment = (double) chargedLevel / (chargedLevel + 1);
                double final_chance = Mth.lerp(adjustment, base_chance, 1);
                final_chance = 1;

                if (shooter.getRandom().nextDouble() < final_chance) {
                    arrow.addTag("LightningStrike");
                    arrow.level().playSound(null, arrow.getX(), arrow.getY(), arrow.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10.0F, 1.0F);
                    if (arrow.getOwner() instanceof Player player) {
                        player.displayClientMessage(Component.literal("arrow charged"), false);
                    }
                } else {
                    arrow.addTag("LightningStrikeFailed");
                }
            }

        }
    }

    //Needs to be called by a mixin every tick on an arrow.
    public static void particleCheck(AbstractArrow arrow, boolean inGround) {
        if (arrow.getTags().contains("LightningStrike")) {
            if (inGround) {
                if (arrow.tickCount % 5 == 0) {
                    for (int i = 0; i < 1; ++i) {
                        spawnParticles(arrow);
                    }
                }
            } else {
                for (int i = 0; i < 2; ++i) {
                    spawnParticles(arrow);
                }
            }
        }
    }

    private static void spawnParticles(AbstractArrow arrow) {
        if (arrow.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    arrow.getX() + serverLevel.getRandom().nextGaussian() / 7.5,
                    arrow.getY() + serverLevel.getRandom().nextGaussian() / 4.5,
                    arrow.getZ() + serverLevel.getRandom().nextGaussian() / 7.5,
                    1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    //Returns either the TUNP charged effect or the Iron's Spellbooks one if that mod is present
    public static MobEffect getChargedEffect() {
        if (ModList.get().isLoaded("irons_spellbooks")) {
            return ISSCompat.getChargedEffect();
        } else {
            return TUNPMobEffects.CHARGED_EFFECT.get();
        }
    }

    public static SimpleParticleType getSparkParticle() {
        if (ModList.get().isLoaded("irons_spellbooks")) {
            return ISSCompat.getElectricityParticle();
        } else {
            return ParticleTypes.ELECTRIC_SPARK;
        }
    }

    //Gets the highest charge level tag on an arrow. Should be formatted "ChargedArrow_1", "ChargedArrow_5", etc.
    private static int getChargedLevel(AbstractArrow arrow) {
        try {
            Optional<String> max_level_tag = arrow.getTags().stream().filter(x -> x.contains("ChargedArrow")).max((a, b) -> {
                int level_a = Integer.parseInt(a.split("_")[1]);
                int level_b = Integer.parseInt(b.split("_")[1]);
                return level_a - level_b;
            });
            return max_level_tag.map(s -> Integer.parseInt(s.split("_")[1])).orElse(0);
        } catch (NumberFormatException ignored) {}
        return 0;
    }

    private static int getChargedLevel(LivingEntity entity) {
        if (!entity.hasEffect(getChargedEffect())) {
            return 0;
        } else {
            MobEffectInstance instance = entity.getEffect(getChargedEffect());
            return instance == null ? 0 : instance.getAmplifier() + 1;
        }
    }

}

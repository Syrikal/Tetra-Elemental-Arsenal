package com.syric.teupnepa.upgrade_types;

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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.storage.ServerLevelData;
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

            boolean thunder = event.getEntity().level().isRaining() && event.getEntity().level().isThundering();
            boolean rain = event.getEntity().level().isRaining();

            float damage_boost = thunder ? 0.2F : (rain ? 0.1F : 0);
            boolean shock = false;
            int shockLevel = 0;
            int shockDuration = thunder ? 300 : (rain ? 200 : 100);

            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity livingEntity
                    && (ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.LIGHTNING)
                    || ItemIdentificationUtil.isUpgradedTool(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.LIGHTNING))) {
                if (livingEntity.hasEffect(getChargedEffect())) {
                    damage_boost *= 2;
                    shock = true;
                    shockLevel = getChargedLevel(livingEntity) / 2;
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
                    shockLevel = chargeLevel / 2;
                }
                event.setAmount(event.getAmount() * (1 + damage_boost));
                SendMessageUtil.triggered(UpgradeType.LIGHTNING, event.getSource().getEntity());
            }
            if (shock) {
                event.getEntity().addEffect(new MobEffectInstance(TUNPMobEffects.SHOCKED_EFFECT.get(), shockDuration, shockLevel - 1));
            }
        }
    }

    //Arrows with the "LightningStrike" tag spawn lightning on impact
    @SubscribeEvent
    public static void arrowImpact(ProjectileImpactEvent event) {
        if (event.getProjectile().level() instanceof ServerLevel serverLevel
                && event.getProjectile().getTags().contains("LightningStrike")
                && event.getProjectile() instanceof AbstractArrow arrow
                && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.LIGHTNING)) {
            EntityType.LIGHTNING_BOLT.spawn(serverLevel, event.getProjectile().getOnPos(), MobSpawnType.TRIGGERED);
            arrow.kill();
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
                if (player.getTicksUsingItem() < 10) {
                    chargedLevel = 2;
                    chargedDuration = 600;
                    event.setAmount(0);
                    player.playNotifySound(SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.4F, 1.0F);
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
            if (chargedLevel <= 0) return;

            double radius = 3.3 + 0.7 * chargedLevel;
            float damage = (float) (4.5 + 1.5 * chargedLevel);
            double knockbackStrength = 0.5 + 0.2 * chargedLevel;
            double knockupStrength = 0.25 + 0.1 * chargedLevel;
            int shockDuration = 100 + 20 * chargedLevel;
            int shockedLevel = switch (chargedLevel) {
                case 1, 2 -> 1;
                default -> 2;
            };
            float volume = (float) (1.05 + 0.15 * chargedLevel);

            //Radius: 2, 3, 4.5, 6 -> 4, 4.7, 5.4, 6.1
            //Damage: 3, 6, 9, 12 -> 6, 7.5, 9, 10.5
            //Knockback strength: 0.3, 0.6, 0.9, 1.2 -> 0.7, 0.9, 1.1, 1.3
            //Knockup strength: 0.15, 0.3, 0.45, 0.6 -> 0.35, 0.45, 0.55, 0.65
            //Shock duration: 50, 100, 150, 200 -> 120, 140, 160, 180
            //Shock level: 0, 1, 2, 3 -> 1, 1, 2, 2
            //Volume: 1, 1.2, 1.4, 1.6 -> 1.2, 1.35, 1.5, 1.65

            LivingEntity blocker = event.getEntity();

            if (!event.getEntity().level().isClientSide()) {
                AABB search_box = blocker.getBoundingBox().inflate(radius, radius / 2, radius);
                blocker.level().getEntities(blocker, search_box).stream()
                        .filter(targetEntity -> targetEntity instanceof LivingEntity)
                        .filter(targetEntity -> targetEntity.distanceTo(blocker) < radius)
                        .filter(targetEntity -> !(targetEntity instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwnerUUID() == blocker.getUUID()))
                        .filter(targetEntity -> !(targetEntity instanceof Player player && player.getTeam() != null && player.getTeam() == blocker.getTeam() && !player.getTeam().isAllowFriendlyFire()))
                        .forEach(targetEntity -> {
                            LivingEntity livingTarget = (LivingEntity) targetEntity;
                            livingTarget.hurt(targetEntity.damageSources().lightningBolt(), damage);
                            livingTarget.knockback(knockbackStrength, blocker.getX() - livingTarget.getX(), blocker.getZ() - livingTarget.getZ());
                            livingTarget.setDeltaMovement(livingTarget.getDeltaMovement().add(new Vec3(0, knockupStrength, 0)));
                            if (chargedLevel > 1) {
                                livingTarget.addEffect(new MobEffectInstance(TUNPMobEffects.SHOCKED_EFFECT.get(), shockDuration, shockedLevel - 1));
                            }
                        });
            }

            blocker.level().playSound(null, blocker.getX(), blocker.getY(), blocker.getZ(), TUNPSounds.LIGHTNING_SHOCKWAVE.get(), SoundSource.PLAYERS, volume, 1.0F);
            blocker.level().playSound(null, blocker.getX(), blocker.getY(), blocker.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, volume * 0.4F, 1.0F);

            blocker.removeEffect(getChargedEffect());

            if (blocker.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < Math.pow(chargedLevel, 2); i++) {
                    for (int j = 0; j < 64; j++) {

                        double distance = 0.5 + serverLevel.getRandom().nextGaussian() * 0.04;
                        float angle = (float) (serverLevel.getRandom().nextDouble() * 2 * Math.PI);
                        double height_displacement = serverLevel.getRandom().nextGaussian() * 0.04;
                        double speed = 0.26 * radius * distance;

                        double x1 = blocker.getX() + distance * Mth.cos(angle);
                        double y1 = blocker.getY() + (blocker.getBbHeight() * 0.6) + height_displacement;
                        double z1 = blocker.getZ() + distance * Mth.sin(angle);

                        double dx = x1 - blocker.getX();
                        double dy = y1 - (blocker.getY() + (blocker.getBbHeight()) * 0.6);
                        double dz = z1 - blocker.getZ();

                        Vec3 displacementVector = new Vec3(dx, dy, dz).normalize().scale(speed);
                        SimpleParticleType particle = getSparkParticle();
//                        particle = ParticleTypes.ELECTRIC_SPARK;
                        if (particle == ParticleTypes.ELECTRIC_SPARK) {
                            displacementVector.scale(3);
                        }

                        serverLevel.sendParticles(particle,
                                x1, y1, z1,
                                0,
                                displacementVector.x, displacementVector.y, displacementVector.z,
                                speed * 2);
                    }
                }
            }

        }
    }

    //Needs to be called by a mixin every tick on an arrow. Charges arced arrows with a lightning strike.
    public static void chargeArrow(AbstractArrow arrow) {
        if (!arrow.level().isClientSide()
            && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.LIGHTNING)
            && arrow.getOwner() instanceof LivingEntity shooter
            && Math.abs(arrow.getDeltaMovement().y()) < 0.05F
            && !arrow.level().dimensionType().hasCeiling()
            && arrow.level().dimensionType().hasSkyLight()
            && !arrow.level().dimension().location().getPath().equals("the_end")
            && !arrow.getTags().contains("LightningStrikeFailed")
            && !arrow.getTags().contains("LightningStrike")
            && arrow.level().getLevelData() instanceof ServerLevelData serverLevelData) {


            boolean raining = serverLevelData.isRaining();
            boolean thunder = serverLevelData.isThundering() && serverLevelData.isRaining();

            double vertical_distance = Math.abs(arrow.getY() - shooter.getY());
            double min_vertical_distance = thunder ? 40 : raining ? 50 : 60;

            double altitude = arrow.getY();
            double min_altitude = thunder ? 100 : raining ? 130 : 160;

            if (vertical_distance > min_vertical_distance
                && altitude > min_altitude) {

                int chargedLevel = getChargedLevel(arrow);
                double base_chance = thunder ? 0.75 : raining ? 0.5 : 0.25;
                double adjustment = (double) chargedLevel / (chargedLevel + 1);
                double final_chance = Mth.lerp(adjustment, base_chance, 1);
//                final_chance = 1;

                if (shooter.getRandom().nextDouble() < final_chance) {
                    arrow.addTag("LightningStrike");
                    arrow.level().playSound(null, arrow.getX(), arrow.getY(), arrow.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10.0F, 1.0F);

                    TeUpNePa.LOGGER.debug("ServerLevelData before: ThunderTime {}, RainTime {}, ClearTime{}, isThundering {}, isRaining {}",
                            serverLevelData.getThunderTime(), serverLevelData.getRainTime(), serverLevelData.getClearWeatherTime(), serverLevelData.isThundering(), serverLevelData.isRaining());

                    if (!raining) {
                        serverLevelData.setRainTime(Math.max(60, serverLevelData.getRainTime() / 2));
                        serverLevelData.setClearWeatherTime(0);
                    }
                    if (raining && !thunder) {
                        serverLevelData.setThunderTime(Math.max(60, serverLevelData.getThunderTime() / 3));
                        serverLevelData.setClearWeatherTime(0);
                    }

                    TeUpNePa.LOGGER.debug("ServerLevelData before: ThunderTime {}, RainTime {}, ClearTime{}, isThundering {}, isRaining {}",
                            serverLevelData.getThunderTime(), serverLevelData.getRainTime(), serverLevelData.getClearWeatherTime(), serverLevelData.isThundering(), serverLevelData.isRaining());

                } else {
                    arrow.addTag("LightningStrikeFailed");
                }
            }

        }
    }

    //Needs to be called by a mixin every tick on an arrow.
    public static void particleCheck(AbstractArrow arrow, boolean inGround) {
        if (arrow.getTags().contains("LightningStrike") || getChargedLevel(arrow) > 0) {
            boolean lightningStrike = arrow.getTags().contains("LightningStrike");
            int bonus = getChargedLevel(arrow) / 2;
            int bonus2 = (getChargedLevel(arrow) - 1) / 3;

            if (inGround) {
                if (arrow.tickCount % (lightningStrike ? 5 : 10) == 0) {
                    for (int i = 0; i < 1 + bonus2; ++i) {
                        spawnParticles(arrow);
                    }
                }
            } else {
                if (lightningStrike || arrow.tickCount % 5 == 0) {
                    for (int i = 0; i < 1 + bonus; ++i) {
                        spawnParticles(arrow);
                    }
                }
            }
        }
    }

    private static void spawnParticles(AbstractArrow arrow) {
        if (arrow.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(serverLevel.getRandom().nextDouble() < 0.15 ? getSparkParticle() : ParticleTypes.ELECTRIC_SPARK,
                    arrow.getX() + serverLevel.getRandom().nextGaussian() / 7.5,
                    arrow.getY() + serverLevel.getRandom().nextGaussian() / 7.5,
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
            int charge_level = max_level_tag.map(s -> Integer.parseInt(s.split("_")[1])).orElse(0);
//            TeUpNePa.LOGGER.debug("Charge level of arrow: " + charge_level);
            return charge_level;
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

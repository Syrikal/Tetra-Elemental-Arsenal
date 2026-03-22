package com.syric.teupnepa.upgrade_types;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.registry.TUNPTags;
import com.syric.teupnepa.util.FindShield;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import com.syric.teupnepa.util.SendMessageUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class RadiantUpgrade {

    //Weapons have increased damage against piglin-type mobs
    @SubscribeEvent
    public static void radiantAttack(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide) {
            //Melee
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.RADIANT)) {

                //Reduces duration of harmful effects
                if (!(event.getSource().getDirectEntity() instanceof Player) || ((Player) event.getSource().getDirectEntity()).getAttackStrengthScale(0) >= 0.95) {
                    reduceEffects(event.getSource().getDirectEntity(), false);
                }

                //Increases damage
                boolean wither = event.getEntity().getType().is(TUNPTags.EntityTypes.WITHER);
                boolean sculk = event.getEntity().getType().is(TUNPTags.EntityTypes.SCULK);
                boolean fungal = event.getEntity().getType().is(TUNPTags.EntityTypes.FUNGAL);
                boolean undead = event.getEntity() instanceof Mob mob && mob.getMobType() == MobType.UNDEAD;

                if (wither || sculk || fungal || undead) {

                    float boost;
                    boolean smite = ((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem().getEnchantmentLevel(Enchantments.SMITE) >= 4;

                    if (wither) {
                        boost = 0.15F;
                    } else if (undead) {
                        boost = 0.1F;
                    } else {
                        boost = 0.08F;
                    }

                    if (smite) {
                        boost *= 2;
                    }

                    SendMessageUtil.triggered(UpgradeType.RADIANT, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * (1 + boost));

                }


            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.RADIANT)) {

                if (!(arrow.getOwner() instanceof Player) || arrow.isCritArrow()) {
                    reduceEffects(arrow.getOwner(), true);
                }

                boolean wither = event.getEntity().getType().is(TUNPTags.EntityTypes.WITHER);
                boolean sculk = event.getEntity().getType().is(TUNPTags.EntityTypes.SCULK);
                boolean fungal = event.getEntity().getType().is(TUNPTags.EntityTypes.FUNGAL);
                boolean undead = event.getEntity() instanceof Mob mob && mob.getMobType() == MobType.UNDEAD;
                if (wither || sculk || fungal || undead) {

                    float boost;

                    if (wither) {
                        boost = 0.2F;
                    } else if (undead) {
                        boost = 0.15F;
                    } else {
                        boost = 0.12F;
                    }

                    event.setAmount(event.getAmount() * (1 + boost));
                    SendMessageUtil.triggered(UpgradeType.RADIANT, event.getSource().getEntity());
                }
            }
        }
    }

    private static void reduceEffects(Entity entity, boolean arrow) {
        float multiplier_strong = arrow ? 0.85F : 0.9F;
        float multiplier_weak = arrow ? 0.92F : 0.95F;
        int flat_strong = arrow ? 90 : 60;
        int flat_weak = arrow ? 45 : 30;

        if (entity instanceof LivingEntity livingEntity) {
            List<MobEffectInstance> new_effects = new ArrayList<>();
            List<MobEffect> to_remove = new ArrayList<>();
//            TeUpNePa.LOGGER.debug("Attempting to reduce duration of effects");
            for (MobEffectInstance instance : livingEntity.getActiveEffects()) {
//                TeUpNePa.LOGGER.debug("Analyzing instance of " + instance.getEffect().getDisplayName().getString());
                if (!instance.getEffect().isBeneficial() && !instance.getEffect().isInstantenous()) {
//                    TeUpNePa.LOGGER.debug("Is not beneficial or instantaneous");
                    ITagManager<MobEffect> tagManager = ForgeRegistries.MOB_EFFECTS.tags();
                    if (tagManager != null) {
                        boolean radiant_reduces_strong = tagManager.getReverseTag(instance.getEffect()).isPresent() &&
                                tagManager.getReverseTag(instance.getEffect()).get().containsTag(TUNPTags.MobEffects.RADIANT_REDUCES_STRONG);
                        boolean radiant_reduces = tagManager.getReverseTag(instance.getEffect()).isPresent() &&
                                tagManager.getReverseTag(instance.getEffect()).get().containsTag(TUNPTags.MobEffects.RADIANT_REDUCES);
//                        TeUpNePa.LOGGER.debug("Passed initial tests");

                        if (radiant_reduces_strong) {
                            int current_duration = instance.getDuration();
                                int new_duration = (int) Math.min(current_duration * multiplier_strong, current_duration - flat_strong);
                                new_duration = Math.max(new_duration, 0);
//                            TeUpNePa.LOGGER.debug("Reducing strongly from " + current_duration + " to " + new_duration + " ticks");
                            MobEffectInstance new_instance = new MobEffectInstance(instance.getEffect(), new_duration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());
                            new_effects.add(new_instance);
                            to_remove.add(instance.getEffect());
                        } else if (radiant_reduces) {
                            int current_duration = instance.getDuration();
                            int new_duration = (int) Math.min(current_duration * multiplier_weak, current_duration - flat_weak);
                            new_duration = Math.max(new_duration, 0);
//                            TeUpNePa.LOGGER.debug("Reducing weakly from " + current_duration + " to " + new_duration + " ticks");
                            MobEffectInstance new_instance = new MobEffectInstance(instance.getEffect(), new_duration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());
                            new_effects.add(new_instance);
                            to_remove.add(instance.getEffect());
                        }
                    }
                }
            }
            for (MobEffect effect : to_remove) {
                livingEntity.removeEffectNoUpdate(effect);
            }
            for (MobEffectInstance instance : new_effects) {
                livingEntity.addEffect(instance);
            }

        }
    }

    //Needs to be called by a mixin every tick on an arrow. Banishes storms.
    public static void radiantStormbreak(AbstractArrow arrow) {
        if (!arrow.level().isClientSide()
                && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.RADIANT)
                && arrow.getOwner() instanceof LivingEntity
                && Math.abs(arrow.getDeltaMovement().y()) < 0.05F
                && !arrow.level().dimensionType().hasCeiling()
                && arrow.level().dimensionType().hasSkyLight()
                && !arrow.level().dimension().location().getPath().equals("the_end")
                && arrow.level().getLevelData() instanceof ServerLevelData serverLevelData) {


            boolean raining = serverLevelData.isRaining();
            boolean thundering = serverLevelData.isThundering() && serverLevelData.isRaining();

            if (raining || thundering) {

                arrow.level().playSound(null, arrow.getX(), arrow.getY(), arrow.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10.0F, 1.0F);
                arrow.kill();
                if (raining) {
                    serverLevelData.setRainTime(Math.min(serverLevelData.getRainTime(), 80));
                }
                if (thundering) {
                    serverLevelData.setThunderTime(Math.min(serverLevelData.getThunderTime(), 40));
                }
            }
        }
    }

    //Shield damages enemies
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.RADIANT)) {
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();

            assert attacker != null;
            boolean wither = attacker.getType().is(TUNPTags.EntityTypes.WITHER);
            boolean sculk = attacker.getType().is(TUNPTags.EntityTypes.SCULK);
            boolean fungal = attacker.getType().is(TUNPTags.EntityTypes.FUNGAL);
            boolean undead = attacker instanceof Mob mob && mob.getMobType() == MobType.UNDEAD;

            if ((wither || sculk || fungal || undead) && defender.getRandom().nextFloat() < 0.5) {

                float multiplier;

                if (wither) {
                    multiplier = 1.5F;
                } else if (undead) {
                    multiplier = 1;
                } else {
                    multiplier = 0.8F;
                }

                attacker.hurt(attacker.damageSources().thorns(defender), (defender.getRandom().nextFloat() * 4 + 2) * multiplier);
//                attacker.hurt(attacker.damageSources().thorns(defender), 10 * multiplier);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.RADIANT, defender);
            }
        }
    }

}

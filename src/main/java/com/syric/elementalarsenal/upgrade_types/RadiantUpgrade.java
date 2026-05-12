package com.syric.elementalarsenal.upgrade_types;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.compat.ISSCompat;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.registry.EATags;
import com.syric.elementalarsenal.util.FindShield;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import com.syric.elementalarsenal.util.SendMessageUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class RadiantUpgrade {

    //Weapons have increased damage against undead, wither, and sculk mobs
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
                boolean wither = event.getEntity().getType().is(EATags.EntityTypes.WITHER);
                boolean sculk = event.getEntity().getType().is(EATags.EntityTypes.SCULK);
                boolean fungal = event.getEntity().getType().is(EATags.EntityTypes.FUNGAL);
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

                    if (smite) boost *= 2;

                    SendMessageUtil.triggered(UpgradeType.RADIANT, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * (1 + boost));

                }


            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.RADIANT)) {

                if (!(arrow.getOwner() instanceof Player) || arrow.isCritArrow()) {
                    reduceEffects(arrow.getOwner(), true);
                }

                boolean wither = event.getEntity().getType().is(EATags.EntityTypes.WITHER);
                boolean sculk = event.getEntity().getType().is(EATags.EntityTypes.SCULK);
                boolean fungal = event.getEntity().getType().is(EATags.EntityTypes.FUNGAL);
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
//            ElementalArsenal.LOGGER.debug("Attempting to reduce duration of effects");
            for (MobEffectInstance instance : livingEntity.getActiveEffects()) {
//                ElementalArsenal.LOGGER.debug("Analyzing instance of " + instance.getEffect().getDisplayName().getString());
                if (!instance.getEffect().isBeneficial() && !instance.getEffect().isInstantenous()) {
//                    ElementalArsenal.LOGGER.debug("Is not beneficial or instantaneous");
                    ITagManager<MobEffect> tagManager = ForgeRegistries.MOB_EFFECTS.tags();
                    if (tagManager != null) {
                        boolean radiant_reduces_strong = tagManager.getReverseTag(instance.getEffect()).isPresent() &&
                                tagManager.getReverseTag(instance.getEffect()).get().containsTag(EATags.MobEffects.RADIANT_REDUCES_STRONG);
                        boolean radiant_reduces = tagManager.getReverseTag(instance.getEffect()).isPresent() &&
                                tagManager.getReverseTag(instance.getEffect()).get().containsTag(EATags.MobEffects.RADIANT_REDUCES);
//                        ElementalArsenal.LOGGER.debug("Passed initial tests");

                        if (radiant_reduces_strong) {
                            int current_duration = instance.getDuration();
                                int new_duration = (int) Math.min(current_duration * multiplier_strong, current_duration - flat_strong);
                                new_duration = Math.max(new_duration, 0);
//                            ElementalArsenal.LOGGER.debug("Reducing strongly from " + current_duration + " to " + new_duration + " ticks");
                            MobEffectInstance new_instance = new MobEffectInstance(instance.getEffect(), new_duration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());
                            new_effects.add(new_instance);
                            to_remove.add(instance.getEffect());
                        } else if (radiant_reduces) {
                            int current_duration = instance.getDuration();
                            int new_duration = (int) Math.min(current_duration * multiplier_weak, current_duration - flat_weak);
                            new_duration = Math.max(new_duration, 0);
//                            ElementalArsenal.LOGGER.debug("Reducing weakly from " + current_duration + " to " + new_duration + " ticks");
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

                SoundEvent clearSound = ModList.get().isLoaded("irons_spellbooks") ? ISSCompat.getClearSound() : SoundEvents.AMETHYST_BLOCK_CHIME;

                for (int i = 0; i < 20; i++) {
                    arrow.level().playSound(null, arrow.getOwner().getX(), arrow.getOwner().getY(), arrow.getOwner().getZ(), clearSound, SoundSource.WEATHER, 15.0F, 1.0F);
                }

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
            boolean wither = attacker.getType().is(EATags.EntityTypes.WITHER);
            boolean sculk = attacker.getType().is(EATags.EntityTypes.SCULK);
            boolean fungal = attacker.getType().is(EATags.EntityTypes.FUNGAL);
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

    //When a player ticks, reduce all their effects by an additional tick
    @SubscribeEvent
    public static void shieldEffects(TickEvent.PlayerTickEvent event) {
        List<MobEffectInstance> newEffects = new ArrayList<>();
        List<MobEffectInstance> effectsForRemoval = new ArrayList<>();
        ITagManager<MobEffect> tagManager = ForgeRegistries.MOB_EFFECTS.tags();
        if (tagManager != null
                && !event.player.level().isClientSide()
                && !event.player.getActiveEffects().isEmpty()
                && FindShield.getModularShield(event.player) != null
                && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.player), UpgradeType.RADIANT)) {

            event.player.getActiveEffects().stream()
                    .filter(o -> isStronglyReduced(o.getEffect(), tagManager) || isWeaklyReduced(o.getEffect(), tagManager))
                    .forEach(o -> {
                        if (isStronglyReduced(o.getEffect(), tagManager)) {
                            newEffects.add(new MobEffectInstance(
                                    o.getEffect(),
                                    o.getDuration() - 1,
                                    o.getAmplifier(),
                                    o.isAmbient(),
                                    o.isVisible(),
                                    o.showIcon()));
                            effectsForRemoval.add(o);
                        } else if (isWeaklyReduced(o.getEffect(), tagManager)) {
                            newEffects.add(new MobEffectInstance(
                                    o.getEffect(),
                                    o.getDuration() - ((event.player.tickCount % 2 == 0) ? 1 : 0),
                                    o.getAmplifier(),
                                    o.isAmbient(),
                                    o.isVisible(),
                                    o.showIcon()));
                            effectsForRemoval.add(o);
                        }
                    });

            effectsForRemoval.forEach(x -> event.player.removeEffectNoUpdate(x.getEffect()));
            newEffects.forEach(event.player::addEffect);

        }

    }

    private static boolean isStronglyReduced(MobEffect effect, ITagManager<MobEffect> tagManager) {
        return tagManager.getReverseTag(effect).isPresent() &&
                tagManager.getReverseTag(effect).get().containsTag(EATags.MobEffects.RADIANT_REDUCES_STRONG);
    }
    private static boolean isWeaklyReduced(MobEffect effect, ITagManager<MobEffect> tagManager) {
        return tagManager.getReverseTag(effect).isPresent() &&
                tagManager.getReverseTag(effect).get().containsTag(EATags.MobEffects.RADIANT_REDUCES);
    }

}

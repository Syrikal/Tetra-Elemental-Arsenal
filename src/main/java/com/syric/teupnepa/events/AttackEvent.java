package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.registry.TUNPTags;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.ModularItem;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class AttackEvent {

    @SubscribeEvent
    public void attackEvent(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
        Entity target = event.getEntity();
        float damage = event.getAmount();
        ItemStack weapon = event.getEntity().getMainHandItem();
        if (source == null || target == null) {
            return;
        }
        if (weapon.getItem() instanceof ModularItem && ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon)) {
            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.GOLD)) {
                if (target.getType().is(TUNPTags.EntityTypes.GOLD_DAMAGED)) {
                    event.setAmount(damage * 1.2F);
                }
            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.FIRE)) {
                if (target.isOnFire() && !target.getType().fireImmune()) {
                    event.setAmount(damage * 1.2F);
                }
            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.ENDER)) {
                if (target.getType().is(TUNPTags.EntityTypes.END_NATIVE)) {
                    event.setAmount(damage * 1.2F);
                }
            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.WATER)) {
                if (target.getType() == EntityType.ENDERMAN || target.getType().fireImmune()) {
                    event.setAmount(damage * 1.2F);
                } else if (source.isInWater() && target.isInWater()) {
                    event.setAmount(damage * 1.1F);
                }
            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.WITHER)) {
                if (target instanceof LivingEntity living) {
                    if (living.hasEffect(MobEffects.WITHER)) {
                        event.setAmount(damage * 1.2F);
                    } else {
                        living.addEffect(new MobEffectInstance(MobEffects.WITHER, 20));
                    }
                }
            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.POISON)) {
                if (target instanceof LivingEntity living) {
                    if (living.hasEffect(MobEffects.POISON)) {
                        event.setAmount(damage * 1.2F);
                    } else {
                        living.addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1));
                    }
                }
            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.PHANTOM)) {
                if (target.getType() == EntityType.PHANTOM) {
                    event.setAmount(damage * 1.5F);
                } else if (target.isNoGravity()) {
                    event.setAmount(damage * 1.2F);
                }
            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.FEATHER)) {
                if (target instanceof LivingEntity living) {
                    float mult = 1.0F;
                    if (!target.onGround()) {
                        mult += 0.1F;
                    }
                    if (living.hasEffect(MobEffects.LEVITATION)) {
                        mult += 0.1F;
                    }
                    event.setAmount(damage * mult);
                }
            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.ECHO)) {
                if (target.getType().is(TUNPTags.EntityTypes.SCULK)) {
                    event.setAmount(damage * 1.2F);
                }
            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.CORRUPT)) {

            }

            if (ItemIdentificationUtil.isUpgradedMeleeWeapon(weapon, UpgradeType.RADIANT)) {
                //Additional damage to wither/sculk/undead mobs
                if (target.getType().is(TUNPTags.EntityTypes.WITHER)) {
                    event.setAmount(damage * 1.2F);
                } else if (target.getType().is(TUNPTags.EntityTypes.SCULK)) {
                    event.setAmount(damage * 1.1F);
                } else if (target instanceof Mob mob && mob.getMobType() == MobType.UNDEAD) {
                    boolean smite = weapon.getEnchantmentLevel(Enchantments.SMITE) >= 4;
                    event.setAmount(damage * (smite ? 1.1F : 1.05F));
                }

                //Shortens duration of effects on attacker
                if (source instanceof LivingEntity living) {
                    if (living instanceof Player player && player.getCurrentItemAttackStrengthDelay() != 0) {
                        return;
                    }

                    List<MobEffectInstance> new_effects = new ArrayList<>();
                    for (MobEffectInstance instance : living.getActiveEffects()) {
                        if (instance.getEffect() == MobEffects.WITHER || instance.getEffect() == MobEffects.POISON) {
                            int current_duration = instance.getDuration();
                            int new_duration = (int) Math.min(current_duration * 0.7, current_duration - 40);
                            MobEffectInstance new_instance = new MobEffectInstance(instance.getEffect(), new_duration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());
                            new_effects.add(new_instance);
                            living.removeEffectNoUpdate(instance.getEffect());
                        } else if (!instance.getEffect().isBeneficial() && !instance.getEffect().isInstantenous()) {
                            int current_duration = instance.getDuration();
                            int new_duration = (int) Math.min(current_duration * 0.9, current_duration - 20);
                            MobEffectInstance new_instance = new MobEffectInstance(instance.getEffect(), new_duration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());
                            new_effects.add(new_instance);
                            living.removeEffectNoUpdate(instance.getEffect());
                        }
                    }
                    for (MobEffectInstance instance : new_effects) {
                        living.addEffect(instance);
                    }
                }
            }
        }
    }

}

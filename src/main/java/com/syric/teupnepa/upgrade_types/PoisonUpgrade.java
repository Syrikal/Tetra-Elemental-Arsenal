package com.syric.teupnepa.upgrade_types;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.util.FindShield;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import com.syric.teupnepa.util.SendMessageUtil;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class PoisonUpgrade {
    
    //Weapons have increased damage against poisoned targets
    //and apply poison to everyone else
    @SubscribeEvent
    public static void poisonAttack(LivingHurtEvent event) {
//        TeUpNePa.LOGGER.debug("LivingHurtEvent triggered");
//        TeUpNePa.LOGGER.debug("ClientSide?: " + event.getEntity().level().isClientSide + "; Damage Type Correct?: " + event.getSource().is(DamageTypes.MOB_ATTACK) + "; Attacker Living? " + (event.getSource().getEntity() instanceof LivingEntity));
//        TeUpNePa.LOGGER.debug("Damage type: " + event.getSource().type());

        if (!event.getEntity().level().isClientSide) {
//            TeUpNePa.LOGGER.debug("Passed first checks");
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.POISON)) {
//                TeUpNePa.LOGGER.debug("Passed melee checks, testing whether target is poisoned");
                if (event.getEntity().hasEffect(MobEffects.POISON)) {
//                    TeUpNePa.LOGGER.debug("Target is poisoned, increased damage");
                    SendMessageUtil.triggered(UpgradeType.POISON, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                } else {
//                    TeUpNePa.LOGGER.debug("Target is not poisoned, applied effect");
                    event.getEntity().addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                    SendMessageUtil.triggered(UpgradeType.POISON, event.getSource().getEntity());
                }
            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.POISON)) {
//                TeUpNePa.LOGGER.debug("Passed ranged checks, testing whether target is poisoned");
                if (event.getEntity().hasEffect(MobEffects.POISON)) {
//                    TeUpNePa.LOGGER.debug("Target is poisoned, increased damage");
                    SendMessageUtil.triggered(UpgradeType.POISON, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                } else {
//                    TeUpNePa.LOGGER.debug("Target is not poisoned, applied effect");
                    event.getEntity().addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                    SendMessageUtil.triggered(UpgradeType.POISON, event.getSource().getEntity());
                }
            }
        }
    }

    //Shield poisons attackers
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.POISON)) {
//            TeUpNePa.LOGGER.debug("Poison shield detected");
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            if (attacker instanceof LivingEntity livingAttacker && defender.getRandom().nextFloat() < 0.5) {
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
//                TeUpNePa.LOGGER.debug("Poison shield retaliation activated");
                livingAttacker.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                SendMessageUtil.triggered(UpgradeType.POISON, event.getEntity());
            }
        }
    }

}

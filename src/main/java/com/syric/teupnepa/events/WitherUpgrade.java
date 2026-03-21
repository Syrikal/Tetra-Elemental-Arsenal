package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.registry.TUNPTags;
import com.syric.teupnepa.util.FindShield;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import com.syric.teupnepa.util.SendMessageUtil;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.TetraRegistries;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

import java.util.Optional;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class WitherUpgrade {

    //Weapons have increased damage against withering targets
    //and apply wither to everyone else
    @SubscribeEvent
    public static void witherAttack(LivingHurtEvent event) {
//        TeUpNePa.LOGGER.debug("LivingHurtEvent triggered");
//        TeUpNePa.LOGGER.debug("ClientSide?: " + event.getEntity().level().isClientSide + "; Damage Type Correct?: " + event.getSource().is(DamageTypes.MOB_ATTACK) + "; Attacker Living? " + (event.getSource().getEntity() instanceof LivingEntity));
//        TeUpNePa.LOGGER.debug("Damage type: " + event.getSource().type());

        if (!event.getEntity().level().isClientSide) {
//            TeUpNePa.LOGGER.debug("Passed first checks");
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.WITHER)) {
//                TeUpNePa.LOGGER.debug("Passed melee checks, testing whether target is withering");
                if (event.getEntity().hasEffect(MobEffects.WITHER)) {
//                    TeUpNePa.LOGGER.debug("Target is withering, increased damage");
                    SendMessageUtil.triggered(UpgradeType.WITHER, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                } else {
//                    TeUpNePa.LOGGER.debug("Target is not withering, applied effect");
                    event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
                    SendMessageUtil.triggered(UpgradeType.WITHER, event.getSource().getEntity());
                }
            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.WITHER)) {
//                TeUpNePa.LOGGER.debug("Passed ranged checks, testing whether target is withering");
                if (event.getEntity().hasEffect(MobEffects.WITHER)) {
//                    TeUpNePa.LOGGER.debug("Target is withering, increased damage");
                    SendMessageUtil.triggered(UpgradeType.WITHER, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                } else {
//                    TeUpNePa.LOGGER.debug("Target is not withering, applied effect");
                    event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
                    SendMessageUtil.triggered(UpgradeType.WITHER, event.getSource().getEntity());
                }
            }
        }
    }

    //Shield withers attackers
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.WITHER)) {
//            TeUpNePa.LOGGER.debug("Wither shield detected");
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            if (attacker instanceof LivingEntity livingAttacker && defender.getRandom().nextFloat() < 0.5) {
//                TeUpNePa.LOGGER.debug("Wither shield retaliation activated");
                livingAttacker.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.WITHER, event.getEntity());
            }
        }
    }

}

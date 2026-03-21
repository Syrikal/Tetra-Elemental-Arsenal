package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.registry.TUNPTags;
import com.syric.teupnepa.util.FindShield;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import com.syric.teupnepa.util.SendMessageUtil;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.*;
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
public class GoldUpgrade {

    //Weapons have increased damage against piglin-type mobs
    @SubscribeEvent
    public static void goldAttack(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide
                && event.getEntity().getType().is(TUNPTags.EntityTypes.GOLD_DAMAGED) ) {
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.GOLD)) {
                SendMessageUtil.triggered(UpgradeType.GOLD, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.GOLD)) {
                SendMessageUtil.triggered(UpgradeType.GOLD, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            }
        }
    }

    //Handles looting on mobs
    //Confirmed functional
    @SubscribeEvent
    public static void LootingMobEvent(LootingLevelEvent event) {
        assert event.getDamageSource() != null;
        if (event.getDamageSource().getDirectEntity() != null && !event.getEntity().level().isClientSide) {
            if (event.getDamageSource().getDirectEntity() instanceof Arrow arrow) {
                if (ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.GOLD)) {
                    int bonus = event.getLootingLevel() >= 3 ? 2 : 1;
                    event.setLootingLevel(Math.min(1, event.getLootingLevel() + bonus));
                    SendMessageUtil.triggered(UpgradeType.GOLD, event.getDamageSource().getEntity());
                }
            } else if (event.getDamageSource().getDirectEntity() instanceof LivingEntity livingEntity) {
                if (livingEntity.getMainHandItem().getItem() instanceof ModularItem) {
                    if (ItemIdentificationUtil.isUpgradedMeleeWeapon(livingEntity.getMainHandItem(), UpgradeType.GOLD)) {
                        int bonus = event.getLootingLevel() >= 3 ? 2 : 1;
                        event.setLootingLevel(event.getLootingLevel() + bonus);
                        SendMessageUtil.triggered(UpgradeType.GOLD, event.getDamageSource().getEntity());
                    }
                }
            }
        }
    }

    //For fortune upgrade, mixin to ApplyBonusCount's use of net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel

    //Shield reduces Hoglin knockback in either hand, even when unused
    @SubscribeEvent
    public static void HoglinKnockbackEvent(LivingKnockBackEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity().getLastAttacker() instanceof Hoglin && ItemIdentificationUtil.isUpgradedShield(event.getEntity().getMainHandItem(), UpgradeType.GOLD) || ItemIdentificationUtil.isUpgradedShield(event.getEntity().getOffhandItem(), UpgradeType.GOLD)) {
            event.setStrength(event.getOriginalStrength() * 0.33F);
            SendMessageUtil.triggered(UpgradeType.GOLD, event.getEntity());
        }
    }

    //Shield damages piglin-type mobs and can't be disabled by brutes
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.GOLD)) {
            //Cancel shield breaking from Piglin brutes
            if (event.getDamageSource().getDirectEntity() instanceof PiglinBrute) {
                if (event.getEntity() instanceof Player player) {
                    Optional<RegistryObject<Item>> shieldRegistry = TetraRegistries.items.getEntries().stream().filter(itemRegistryObject -> itemRegistryObject.get() instanceof ModularShieldItem).findFirst();
                    if (shieldRegistry.isPresent()) {
                        Item modularShield = shieldRegistry.get().get();
                        ServerScheduler.schedule(0, () -> player.getCooldowns().removeCooldown(modularShield));
                    }
                }
                SendMessageUtil.triggered(UpgradeType.GOLD, event.getEntity());
            }
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            if (attacker != null && attacker.getType().is(TUNPTags.EntityTypes.GOLD_DAMAGED) && defender.getRandom().nextFloat() < 0.5) {
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 4 + 2);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.GOLD, defender);
            }
        }
    }

}

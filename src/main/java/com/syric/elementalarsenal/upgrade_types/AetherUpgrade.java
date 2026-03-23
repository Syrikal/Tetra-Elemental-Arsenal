package com.syric.elementalarsenal.upgrade_types;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.util.FindShield;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import com.syric.elementalarsenal.util.SendMessageUtil;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class AetherUpgrade {

    //Weapons have increased damage against aether mobs
    @SubscribeEvent
    public static void aetherAttack(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide
            && (Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType())).getNamespace().contains("aether")
                || event.getEntity().level().dimension().location().getPath().equals("the_aether"))) {
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.AETHERIC)) {
                SendMessageUtil.triggered(UpgradeType.AETHERIC, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.AETHERIC)) {
                SendMessageUtil.triggered(UpgradeType.AETHERIC, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            }
        }
    }

    //Shield damages aetheric mobs
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide
                && event.getDamageSource().getDirectEntity() != null
                && FindShield.getModularShield(event.getEntity()) != null
                && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.AETHERIC)) {
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            ElementalArsenal.LOGGER.debug("Attacker Namespace: {}", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType())).getNamespace());
            if (Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType())).getNamespace().contains("aether") && defender.getRandom().nextFloat() < 0.5) {
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 4 + 2);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.AETHERIC, defender);
            }
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (!event.getEntity().isCreative()
                && ItemIdentificationUtil.isUpgradedTool(event.getEntity().getMainHandItem(), UpgradeType.AETHERIC)
                && (event.getEntity().level().dimension().location().getPath().equals("the_aether")
                || Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(event.getState().getBlock())).getNamespace().contains("aether"))) {
            event.setNewSpeed(event.getOriginalSpeed() * 1.33F);
        }
    }

}

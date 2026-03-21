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
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.TetraRegistries;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

import java.util.Objects;
import java.util.Optional;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class AetherUpgrade {

    //Weapons have increased damage against aether mobs
    @SubscribeEvent
    public static void aetherAttack(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide
                && !event.getSource().isIndirect()
                && event.getSource().getDirectEntity() instanceof LivingEntity
                && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.AETHERIC)) {

            TeUpNePa.LOGGER.debug("Detected attack with aetheric-upgraded weapon");
            TeUpNePa.LOGGER.debug("Aether native: " + event.getEntity().getType().is(TUNPTags.EntityTypes.AETHER_NATIVE));
            TeUpNePa.LOGGER.debug("Namespace contains aether: " + Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType())).getNamespace().contains("aether") + ", namespace: " + Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType())).getNamespace());

        }

        if (!event.getEntity().level().isClientSide
            && (event.getEntity().getType().is(TUNPTags.EntityTypes.AETHER_NATIVE) || Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType())).getNamespace().contains("aether"))) {
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
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.AETHERIC)) {
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            TeUpNePa.LOGGER.debug("Attacker Namespace: " + Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType())).getNamespace());
            if (attacker != null && (attacker.getType().is(TUNPTags.EntityTypes.AETHER_NATIVE) || Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType())).getNamespace().contains("aether")) && defender.getRandom().nextFloat() < 0.5) {
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 4 + 2);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.AETHERIC, defender);
            }
        }
    }

    @SubscribeEvent
    public void breakSpeed(PlayerEvent.BreakSpeed event) {
        TeUpNePa.LOGGER.debug("Detected BreakSpeed event.");
        TeUpNePa.LOGGER.debug("Aether dimension: " + event.getEntity().level().dimension().location().getPath().equals("the_aether") + ", (id: '" + event.getEntity().level().dimensionTypeId() + "', path: '" + event.getEntity().level().dimension().location().getPath() + "')");
        TeUpNePa.LOGGER.debug("Aether block: " + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(event.getState().getBlock())).getNamespace().contains("aether") + ", (namespace: " + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(event.getState().getBlock())).getNamespace() + ")");
        if (!event.getEntity().isCreative()
                && ItemIdentificationUtil.isUpgradedTool(event.getEntity().getMainHandItem(), UpgradeType.AETHERIC)
                && (event.getEntity().level().dimension().location().getPath().equals("the_aether") || Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(event.getState().getBlock())).getNamespace().contains("aether"))) {
            TeUpNePa.LOGGER.debug("Activated aetheric speed boost");
            TeUpNePa.LOGGER.debug("Dimension: " + event.getEntity().level().dimensionTypeId().toString().contains("aether") + "; Block namespace: " + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(event.getState().getBlock())).getNamespace().contains("aether"));
            event.setNewSpeed(event.getOriginalSpeed() * 1.33F);
        }
    }

}

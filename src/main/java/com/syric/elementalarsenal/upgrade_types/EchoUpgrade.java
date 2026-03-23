package com.syric.elementalarsenal.upgrade_types;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.registry.EATags;
import com.syric.elementalarsenal.util.FindShield;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import com.syric.elementalarsenal.util.SendMessageUtil;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.ModularItem;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class EchoUpgrade {

    //Weapons have increased damage against sculk-type mobs
    @SubscribeEvent
    public static void echoAttack(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide
                && event.getEntity().getType().is(EATags.EntityTypes.SCULK) ) {
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.ECHO)) {
                SendMessageUtil.triggered(UpgradeType.ECHO, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.ECHO)) {
                SendMessageUtil.triggered(UpgradeType.ECHO, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            }
        }
    }

    //Shield damages sculk-type mobs
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.ECHO)) {
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            if (attacker != null && attacker.getType().is(EATags.EntityTypes.SCULK) && defender.getRandom().nextFloat() < 0.5) {
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 4 + 2);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.ECHO, defender);
            }
        }
    }

    //Shield heals you in the Deep Dark
    @SubscribeEvent
    public static void shieldTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide()
                && (event.player.level().getBiome(event.player.blockPosition()).is(Biomes.DEEP_DARK) || event.player.level().dimensionTypeId().toString().contains("otherside"))
                && FindShield.getModularShield(event.player) != null
                && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.player), UpgradeType.ECHO)
                && event.player.tickCount % 100 == 0) {
            event.player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 51, 0, false, false));
//            ElementalArsenal.LOGGER.debug("Detected player in valid biome holding echo-upgraded shield");
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (ItemIdentificationUtil.isUpgradedTool(event.getEntity().getMainHandItem(), UpgradeType.ECHO)) {
            if (!event.getEntity().isCreative()
                    && (event.getEntity().position().y() <= 0 || event.getEntity().level().getBiome(event.getEntity().blockPosition()).is(Biomes.DEEP_DARK) || event.getEntity().level().dimensionTypeId().toString().contains("otherside"))) {

//            ElementalArsenal.LOGGER.debug("Detected candidate for echo mining speed boost");
//            ElementalArsenal.LOGGER.debug("Y-level: " + event.getEntity().position().y());
//            ElementalArsenal.LOGGER.debug("Deep dark: " + event.getEntity().level().getBiome(event.getEntity().blockPosition()).is(Biomes.DEEP_DARK));
//            ElementalArsenal.LOGGER.debug("Otherside: " + event.getEntity().level().dimensionTypeId().toString().contains("otherside"));
//            ElementalArsenal.LOGGER.debug("Original speed: " + event.getOriginalSpeed());

                float multiplier = 1;
                //1.5x if Otherside, or if y<0 in the deep dark
                if (event.getEntity().level().dimensionTypeId().toString().contains("otherside") ||
                        (event.getEntity().position().y() <= 0 && event.getEntity().level().getBiome(event.getEntity().blockPosition()).is(Biomes.DEEP_DARK))) {
//                multiplier = 1.2F;
                    multiplier = 1.5F;
                    //1.25x if y<0 OR deep dark but not both
                } else if (event.getEntity().position().y() <= 0 ^ event.getEntity().level().getBiome(event.getEntity().blockPosition()).is(Biomes.DEEP_DARK)) {
                    multiplier = 1.25F;
                }
//            ElementalArsenal.LOGGER.debug("Final multiplier: " + multiplier);
                event.setNewSpeed(event.getOriginalSpeed() * multiplier);
//            ElementalArsenal.LOGGER.debug("Final speed: " + event.getNewSpeed());
            }

            event.getEntity().getMainHandItem().getOrCreateTag().putBoolean("EchoUpgradeMuffled", true);

        }
    }

    @SubscribeEvent
    public static void dropXP(LivingExperienceDropEvent event) {
        if (!event.getEntity().level().isClientSide
            && event.getAttackingPlayer() != null
            && event.getAttackingPlayer().getMainHandItem().getItem() instanceof ModularItem
            && ItemIdentificationUtil.isUpgradedWeapon(event.getAttackingPlayer().getMainHandItem(), UpgradeType.ECHO)) {
            event.setDroppedExperience((int) Math.ceil(event.getOriginalExperience() * 1.33));
        }
    }

    //Muffles echo-upgraded items when dropped
    @SubscribeEvent
    public static void dropItem(ItemTossEvent event) {
        if (ItemIdentificationUtil.isUpgradedItem(event.getEntity().getItem(), UpgradeType.ECHO)) {
            event.getEntity().getItem().getOrCreateTag().putBoolean("EchoUpgradeMuffled", true);
        }
    }

    //Removes muffling tag when item picked up
    @SubscribeEvent
    public static void unmuffleItem(PlayerEvent.ItemPickupEvent event) {
        if (event.getStack().hasTag()) {
            event.getStack().removeTagKey("EchoUpgradeMuffled");
        }
    }

    @SubscribeEvent
    public static void returnSoulbound(PlayerEvent.Clone event) {
//        ElementalArsenal.LOGGER.debug("Detected player cloning");
        if (event.isWasDeath() && !event.getEntity().level().isClientSide) {
//            ElementalArsenal.LOGGER.debug("Detected death on server side");
            for (ItemStack itemStack : event.getOriginal().getInventory().items) {
//                ElementalArsenal.LOGGER.debug("Analyzing item: " + itemStack.getItem());
                if (ItemIdentificationUtil.isUpgradedItem(itemStack, UpgradeType.ECHO)) {
//                    ElementalArsenal.LOGGER.debug("Detected echo item, returning to player");
                    event.getEntity().getInventory().add(event.getOriginal().getInventory().findSlotMatchingItem(itemStack), itemStack);

                }
            }
        }
    }

}

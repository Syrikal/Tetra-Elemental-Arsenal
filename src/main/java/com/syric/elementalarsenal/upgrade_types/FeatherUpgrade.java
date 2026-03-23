package com.syric.elementalarsenal.upgrade_types;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.network.C2SFeatherTogglePacket;
import com.syric.elementalarsenal.network.PacketHandler;
import com.syric.elementalarsenal.util.FindShield;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

import java.util.Objects;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class FeatherUpgrade {

    //Weapons have increased damage against levitating targets or targets not standing on the ground
    @SubscribeEvent
    public static void featherAttack(LivingHurtEvent event) {

        if (!event.getEntity().level().isClientSide) {
//            ElementalArsenal.LOGGER.debug("Passed first checks");
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity attacker
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.FEATHER)) {

                LivingEntity target = event.getEntity();

                //Increased damage against enemies levitating and/or not on ground
                if (!target.onGround() || target.hasEffect(MobEffects.LEVITATION)) {
                    float boost = 0;

                    if (target.hasEffect(MobEffects.LEVITATION)) {
                        boost += 0.05F;
                    }
                    if (!target.onGround()) {
                        boost += 0.05F;
                    }

                    event.setAmount(event.getAmount() * (1.0F + boost));
                }

                //Levitates enemies
                boolean fullStrengthHit = !(event.getSource().getDirectEntity() instanceof Player) || ((Player) event.getSource().getDirectEntity()).getAttackStrengthScale(0) >= 0.95;
                if (isActive(attacker.getMainHandItem()) && fullStrengthHit) {
                    int levitation_level = target.hasEffect(MobEffects.LEVITATION) ? 1 : 0;
                    int levitation_time = target.hasEffect(MobEffects.LEVITATION) ? 100 : 50;
                    target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, levitation_time, levitation_level));
                    target.setOnGround(false);
                    target.addTag("FeatherAttacked");
                }


            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.FEATHER)
                    && (!event.getEntity().getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)
                        || Objects.requireNonNull(event.getEntity().getAttribute(Attributes.KNOCKBACK_RESISTANCE)).getValue() < 1)
                    && arrow.getOwner() != null) {

                Vec3 impact_vector = arrow.getDeltaMovement();

                float distanceToShooter = Math.min(event.getEntity().distanceTo(arrow.getOwner()), 20);
                float intensity = Mth.lerp(distanceToShooter / 20, 0, 2F);
                if (event.getEntity().getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)) {
                    AttributeInstance knockback = event.getEntity().getAttribute(Attributes.KNOCKBACK_RESISTANCE);
                    if (knockback != null) {
                        intensity *= (float) (1 - knockback.getValue());
                    }
                }
                Vec3 launch_vector = impact_vector.scale(-1 * intensity);
                event.getEntity().setOnGround(false);
                event.getEntity().setDeltaMovement(event.getEntity().getDeltaMovement().add(launch_vector));
            }
        }
    }

    //Sword does additional knockback when active.
    @SubscribeEvent
    public static void FeatherKnockbackEvent(LivingKnockBackEvent event) {
        if (!event.getEntity().level().isClientSide()
                && event.getEntity().getLastAttacker() != null
                && event.getEntity().getTags().contains("FeatherAttacked")
                && !event.getEntity().getLastAttacker().getMainHandItem().isEmpty()
                && isActive(event.getEntity().getLastAttacker().getMainHandItem())
                && ItemIdentificationUtil.isUpgradedMeleeWeapon(event.getEntity().getLastAttacker().getMainHandItem(), UpgradeType.FEATHER)) {
            event.setStrength(event.getStrength() * 2F);
            event.getEntity().setOnGround(false);
        }
        event.getEntity().removeTag("FeatherAttacked");
    }


    //Shield levitates attackers
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide
                && FindShield.getModularShield(event.getEntity()) != null) {
            ElementalArsenal.LOGGER.debug("Detected block with modular shield. Testing for active feather upgrade");
            ItemStack modularShield = FindShield.getModularShield(event.getEntity());
            assert modularShield != null;
            if (isActive(modularShield)) {
                Entity attacker = event.getDamageSource().getDirectEntity();
                LivingEntity defender = event.getEntity();
                if (attacker instanceof LivingEntity livingAttacker && defender.getRandom().nextFloat() < 0.4) {
                    attacker.hurt(attacker.damageSources().thorns(defender), 0.1F);
                    livingAttacker.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 0));
                    livingAttacker.knockback(0.3, defender.getX() - livingAttacker.getX(), defender.getZ() - livingAttacker.getZ());
                    livingAttacker.setOnGround(false);
                    modularShield.hurtAndBreak(1, defender, (x) -> {});
                }
            }
        }
    }

    //Ignores not-on-ground penalty
    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (!event.getEntity().isCreative()
                && ItemIdentificationUtil.isUpgradedTool(event.getEntity().getMainHandItem(), UpgradeType.FEATHER)) {

            float multiplier = event.getEntity().onGround() ? 1 : 5;

            event.setNewSpeed(event.getOriginalSpeed() * multiplier);
        }
    }

//    Tools pull items towards you
    @SubscribeEvent
    public static void tickEvent(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide()
                && ((isActive(event.player.getMainHandItem()) && ItemIdentificationUtil.isUpgradedTool(event.player.getMainHandItem(), UpgradeType.FEATHER))
                    || (isActive(event.player.getOffhandItem()) && ItemIdentificationUtil.isUpgradedTool(event.player.getOffhandItem(), UpgradeType.FEATHER)))) {

                pullEntities(event.player, ItemEntity.class, 7);
                pullEntities(event.player, ExperienceOrb.class, 7);
        }
    }

    private static <T extends Entity> void pullEntities(Player player, Class<T> entityClass, double range) {
        AABB search_box = player.getBoundingBox().inflate(range, range, range);
        player.level().getEntitiesOfClass(entityClass, search_box, EntitySelector.NO_SPECTATORS)
                .stream().filter(targetEntity -> targetEntity.distanceTo(player) < range)
                .forEach(targetEntity -> {
                    if (targetEntity instanceof  ItemEntity itemEntity) {
                        if (itemEntity.getOwner() == player && itemEntity.getAge() < 60) {
                            return;
                        }
                        if (targetEntity.distanceTo(player) < 1) {
                            itemEntity.setPickUpDelay(0);
                        }

                    }
                    float distance = targetEntity.distanceTo(player);
                    Vec3 direction_vector = targetEntity.position().vectorTo(player.position().add(0, 0.5, 0));
                    float intensity = (float) Mth.lerp(1 - (distance /range), 0, 0.1F);
                    targetEntity.setDeltaMovement(targetEntity.getDeltaMovement().add(direction_vector.scale(intensity)));
                });
    }

    public static void arrowWeightless(AbstractArrow arrow) {
        if (ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.FEATHER)) {
            arrow.setNoGravity(true);
        }
    }

    //Toggle on sneak-right-click for all except bows, crossbows, and shields
    @SubscribeEvent
    public static void toggleAbilities(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().isCrouching()
                && !event.getItemStack().isEmpty()
                && event.getItemStack().getItem() instanceof ModularItem
                && (event.getItemStack().getItem() instanceof ModularBladedItem
                    || event.getItemStack().getItem() instanceof ModularDoubleHeadedItem
                    || event.getItemStack().getItem() instanceof ModularSingleHeadedItem)
                && ItemIdentificationUtil.isUpgradedItem(event.getItemStack(), UpgradeType.FEATHER)) {

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                toggleActive(event.getItemStack(), event.getEntity());
        }
    }

    //Toggle on sneak-left-click for bows, crossbows, and shields
    //Runs on client, sends packet to server to toggle activity
    @SubscribeEvent
    public static void toggleAbilities(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getEntity().isCrouching()
                && !event.getItemStack().isEmpty()
                && event.getItemStack().getItem() instanceof ModularItem
                && (event.getItemStack().getItem() instanceof ModularBowItem
                    || event.getItemStack().getItem() instanceof ModularShieldItem
                    || event.getItemStack().getItem() instanceof ModularCrossbowItem)
                && ItemIdentificationUtil.isUpgradedItem(event.getItemStack(), UpgradeType.FEATHER)) {

                event.setCancellationResult(InteractionResult.SUCCESS);
//                ElementalArsenal.LOGGER.debug("toggleAbilities sending packet to server");
                toggleActive(event.getItemStack(), event.getEntity());
                PacketHandler.sendToServer(new C2SFeatherTogglePacket());
        }
    }

    public static void toggleActive(ItemStack stack, Player player) {
//            ElementalArsenal.LOGGER.debug("Running toggleActive on " + stack.getDisplayName().getString() + " on the " + (player.level().isClientSide() ? "client" : "server") + " side");
            boolean active = isActive(stack);
            setActive(stack, !active);
            if (player.level().isClientSide()) {
                player.sendSystemMessage(Component.translatable(!active ? "message.elementalarsenal.feather_toggled_on" : "message.elementalarsenal.feather_toggled_off"));
            }
    }

    private static void setActive(ItemStack stack, boolean active) {
//        ElementalArsenal.LOGGER.debug("Setting item " + stack.getDisplayName().getString() + " to " + (active ? "active" : "inactive"));
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean("FeatherUpgradeActive", active);
        stack.setTag(tag);
//        boolean isActiveNow = stack.getOrCreateTag().getBoolean("FeatherUpgradeActive");
//        ElementalArsenal.LOGGER.debug("Item tag is now set to " + (isActiveNow ? "active" : "inactive"));
//        isActive(stack);
    }

    //Should only be run on server!
    //Checks item for the tag. If it's a modular item without the tag, adds the tag.
    public static boolean isActive(ItemStack stack) {
        if (!stack.isEmpty()
                && stack.getItem() instanceof ModularItem
                && stack.hasTag()
                && stack.getTag() != null
                && stack.getTag().contains("FeatherUpgradeActive")) {
            return stack.getTag().getBoolean("FeatherUpgradeActive");
        } else if (stack.getItem() instanceof ModularItem) {
            stack.getOrCreateTag().putBoolean("FeatherUpgradeActive", ItemIdentificationUtil.isUpgradedItem(stack, UpgradeType.FEATHER));
            return ItemIdentificationUtil.isUpgradedItem(stack, UpgradeType.FEATHER);
        }
        return false;
    }

}

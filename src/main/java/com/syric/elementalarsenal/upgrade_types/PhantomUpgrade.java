package com.syric.elementalarsenal.upgrade_types;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.registry.EATags;
import com.syric.elementalarsenal.util.FindShield;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import com.syric.elementalarsenal.util.SendMessageUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class PhantomUpgrade {

    @SubscribeEvent
    public static void phantomAttack(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide) {
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.PHANTOM)) {

                if (event.getEntity().getType().is(EATags.EntityTypes.PHANTOM)) {
                    SendMessageUtil.triggered(UpgradeType.PHANTOM, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.5F);
                } else if (event.getEntity().isNoGravity() || event.getEntity() instanceof FlyingAnimal || event.getEntity() instanceof FlyingMob) {
                    SendMessageUtil.triggered(UpgradeType.PHANTOM, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                }

            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.PHANTOM)) {
                if (event.getEntity().getType().is(EATags.EntityTypes.PHANTOM)) {
                    SendMessageUtil.triggered(UpgradeType.PHANTOM, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.5F);
                } else if (event.getEntity().isNoGravity() || event.getEntity() instanceof FlyingAnimal || event.getEntity() instanceof FlyingMob) {
                    SendMessageUtil.triggered(UpgradeType.PHANTOM, event.getSource().getEntity());
                    event.setAmount(event.getAmount() * 1.2F);
                }
            }
        }
    }

    //Shield damages flying enemies, especially phantoms
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.PHANTOM)) {
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            if (attacker != null && attacker.getType().is(EATags.EntityTypes.PHANTOM)) {
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 6 + 3);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.PHANTOM, defender);
            } else if (attacker != null && (attacker instanceof FlyingMob || attacker instanceof FlyingAnimal || attacker.isNoGravity()) && defender.getRandom().nextFloat() < 0.5) {
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 4 + 2);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.PHANTOM, defender);
            }
        }
    }

    @SubscribeEvent
    public static void projectileDeflect(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult entityHitResult
                && entityHitResult.getEntity() instanceof Player player
                && !event.getProjectile().getTags().contains("reflected")
                && player.isUsingItem()
                && player.getTicksUsingItem() < 10
                && ItemIdentificationUtil.isUpgradedShield(player.getUseItem(), UpgradeType.PHANTOM)
                && isBlocked(player, event.getProjectile())) {

            player.playNotifySound(SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.8F, 1.0F);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1F, 1F);

            event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
            Projectile projectile = event.getProjectile();
            projectile.hasImpulse = true;
            projectile.setOwner(player);
            projectile.addTag("reflected");

            boolean sneaking = player.isShiftKeyDown();
            int sneaking_durability_mult = sneaking ? 2 : 1;

            //If not sneaking, flip it around.
            if (!sneaking) {
                projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-1));
                projectile.setYRot(projectile.getYRot() + 180);
                projectile.setXRot(projectile.getXRot() * -1);

                if (projectile instanceof AbstractArrow arrow) {
                    arrow.setCritArrow(true);
                    arrow.setBaseDamage(arrow.getBaseDamage() * 1.4);
                }

                if (projectile instanceof AbstractHurtingProjectile abstractHurtingProjectile) {
                    abstractHurtingProjectile.xPower *= -1;
                    abstractHurtingProjectile.yPower *= -1;
                    abstractHurtingProjectile.zPower *= -1;
                }

            //If sneaking, fire it in the direction the player is looking.
            } else {
                Vec2 accurateAimVector = new Vec2(player.getViewXRot(1.0F), player.getViewYRot(1.0F));
                Vec2 inaccurateAimVector = new Vec2(player.getViewXRot(1.0F), player.getViewYRot(1.0F)).add(new Vec2(6 * player.getRandom().nextFloat() - 3, 6 * player.getRandom().nextFloat() - 3));
                boolean accurate = true;
                Vec3 finalAimVector = Vec3.directionFromRotation(
                        accurate ? accurateAimVector.x : inaccurateAimVector.x,
                        accurate ? accurateAimVector.y : inaccurateAimVector.y);

                projectile.setDeltaMovement(finalAimVector.normalize().scale(projectile.getDeltaMovement().length() * 1.5));
                projectile.setYRot(accurate ? accurateAimVector.y : inaccurateAimVector.y);
                projectile.setXRot(accurate ? accurateAimVector.x : inaccurateAimVector.x);

                if (projectile instanceof AbstractArrow arrow) {
                    arrow.setBaseDamage(arrow.getBaseDamage() * 1.4);
                }

                if (projectile instanceof AbstractHurtingProjectile abstractHurtingProjectile) {
                    Vec3 originalPowerVector = new Vec3(abstractHurtingProjectile.xPower, abstractHurtingProjectile.yPower, abstractHurtingProjectile.zPower);
                    Vec3 powerVector = finalAimVector.normalize().scale(originalPowerVector.length());
                    abstractHurtingProjectile.xPower = powerVector.x();
                    abstractHurtingProjectile.yPower = powerVector.y();
                    abstractHurtingProjectile.zPower = powerVector.z();
                }
            }

            //Damage the shield
            ItemStack shield = player.getUseItem();
            if (projectile instanceof AbstractArrow arrow) {
                if (!player.isCreative()) {
                    shield.hurtAndBreak((int) (arrow.getBaseDamage() * 2 * sneaking_durability_mult), player, (x) -> {});
                }
            } else {
                if (!player.isCreative()) {
                    shield.hurtAndBreak(6 * sneaking_durability_mult, player, (x) -> {});
                }
            }

        }

    }

    private static boolean isBlocked(Entity target, Projectile projectile) {
        if (projectile instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
            return false;
        }
        Vec3 shooterPos = projectile.getEffectSource().position();
        Vec3 shooterToTarget = shooterPos.vectorTo(target.position());
        Vec3 targetLookDirection = target.getViewVector(1);
        Vec3 shooterToTargetHorizontal = new Vec3(shooterToTarget.x, 0, shooterToTarget.z);

        return targetLookDirection.dot(shooterToTargetHorizontal) < 0;

    }


    @SubscribeEvent
    public static void rightClickEvent(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity().isCrouching() && ItemIdentificationUtil.isUpgradedTool(event.getItemStack(), UpgradeType.PHANTOM)) {
            Vec3 originPos = event.getPos().getCenter();
            List<Entity> entities = event.getEntity().level().getEntities(event.getEntity(), new AABB(originPos.x() - 40, originPos.y() - 40, originPos.z() - 40, originPos.x() + 40, originPos.y() + 40, originPos.z() + 40));
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity && entity.position().distanceTo(originPos) < 40) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
                }
            }
            if (!event.getEntity().isCreative() && !entities.isEmpty()) {
                event.getItemStack().hurtAndBreak(3, event.getEntity(), (x) -> {});
            }
        }

    }

}

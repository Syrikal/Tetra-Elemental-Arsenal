package com.syric.teupnepa.events;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.enums.UpgradeType;
import com.syric.teupnepa.util.FindShield;
import com.syric.teupnepa.util.ItemIdentificationUtil;
import com.syric.teupnepa.util.SendMessageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.ServerScheduler;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class FireUpgrade {

    //Shield prevents zombies setting you on fire
    @SubscribeEvent
    public static void ZombieAttackEvent(LivingHurtEvent event) {
        if (event.getSource().getEntity() == null) {
            return;
        }
//        TeUpNePa.LOGGER.debug("LivingHurtEvent triggered");
//        TeUpNePa.LOGGER.debug("ClientSide: " + event.getEntity().level().isClientSide
//                + "; Attacker: " + event.getSource().getEntity().getType()
//                + "; Attacker on fire: " + event.getSource().getEntity().isOnFire()
//                + "; Defender on fire: " + event.getEntity().isOnFire()
//                + "; fire shield: " + ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.FIRE));
        if (!event.getEntity().level().isClientSide
                && event.getSource().getEntity() instanceof Zombie
                && event.getSource().getEntity().isOnFire()
                && !event.getEntity().isOnFire()
                && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.FIRE)) {
//            TeUpNePa.LOGGER.debug("Non-burning creature attacked by burning zombie while holding fire-upgraded shield");
            ServerScheduler.schedule(0, () -> event.getEntity().setRemainingFireTicks(0));
            ServerScheduler.schedule(0, () -> event.getEntity().clearFire());
//            ServerScheduler.schedule(0, () -> event.getEntity().push(0, 5, 0));
            SendMessageUtil.triggered(UpgradeType.FIRE, event.getEntity());
        }
    }

    //Increase damage dealt to burning targets
    @SubscribeEvent
    public static void fireAttack(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide
                && event.getEntity().isOnFire()
                && !event.getEntity().fireImmune()) {
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.FIRE)) {
                SendMessageUtil.triggered(UpgradeType.FIRE, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow arrow
                    && ItemIdentificationUtil.isUpgradedProjectile(arrow, UpgradeType.FIRE)) {
                SendMessageUtil.triggered(UpgradeType.FIRE, event.getSource().getEntity());
                event.setAmount(event.getAmount() * 1.2F);
            }
        }
    }

    //Enhances fire aspect / flame
    @SubscribeEvent
    public static void increaseFireTime(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide
                && !event.getEntity().fireImmune()) {
            if ((event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK))
                    && !event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof LivingEntity
                    && ItemIdentificationUtil.isUpgradedMeleeWeapon(((LivingEntity) event.getSource().getDirectEntity()).getMainHandItem(), UpgradeType.FIRE)
                    && EnchantmentHelper.getFireAspect((LivingEntity) event.getSource().getDirectEntity()) > 0) {
                SendMessageUtil.triggered(UpgradeType.FIRE, event.getSource().getEntity());
                int fireAspect = EnchantmentHelper.getFireAspect((LivingEntity) event.getSource().getDirectEntity());
                event.getEntity().setSecondsOnFire(fireAspect * 6);
            } else if (event.getSource().is(DamageTypes.ARROW) && event.getSource().isIndirect()
                    && event.getSource().getDirectEntity() instanceof Arrow
                    && event.getSource().getDirectEntity().getTags().contains("FlameFireUpgradedNetheriteBow")) {
                event.getEntity().setSecondsOnFire(8);
            }
        }
    }

    //Shield damages burning enemies
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
        if (!event.getEntity().level().isClientSide && FindShield.getModularShield(event.getEntity()) != null && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.FIRE)) {
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
            if (attacker != null && !attacker.fireImmune() && attacker.isOnFire() && defender.getRandom().nextFloat() < 0.5) {
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 4 + 2);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.FIRE, defender);
            }
        }
    }

    //Fire tools smelt block drops
    @SubscribeEvent
    public static void destroyBlock(BlockEvent.BreakEvent event) {
        LevelAccessor levelAccessor = event.getLevel();

        if (levelAccessor instanceof ServerLevel level) {
            Player player = event.getPlayer();
            BlockState state = event.getState();
            BlockPos pos = event.getPos();
            ItemStack heldStack = player.getMainHandItem();

            if (!player.isCreative() &&
                    player.hasCorrectToolForDrops(state) &&
                    ItemIdentificationUtil.isUpgradedTool(heldStack, UpgradeType.FIRE)) {
                if (event.getExpToDrop() > 0) {
                    state.getBlock().popExperience(level, pos, event.getExpToDrop());
                }

                List<ItemStack> drops = Block.getDrops(state, level, pos, level.getBlockEntity(pos), player, player.getMainHandItem());

                for (ItemStack drop : drops) {
                    Container container = new SimpleContainer(drop);
                    Optional<SmeltingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, level);
                    if (recipe.isPresent()) {
                        ItemStack smelted_stack = recipe.get().getResultItem(level.registryAccess());
                        smelted_stack.setCount(drop.getCount());
                        Block.popResource(level, pos, smelted_stack);
                    } else {
                        Block.popResource(level, pos, drop);
                    }
                }
                level.getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

}

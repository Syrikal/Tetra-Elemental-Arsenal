package com.syric.elementalarsenal.upgrade_types;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.util.FindShield;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import com.syric.elementalarsenal.util.SendMessageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class FireUpgrade {

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
                    && event.getSource().getDirectEntity().getTags().contains("FlameFireImbuedArrow")) {
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


    //Shield prevents zombies setting you on fire
    @SubscribeEvent
    public static void ZombieAttackEvent(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide
                && event.getSource().getEntity() != null
                && event.getSource().getEntity() instanceof Zombie zombie
                && zombie.isOnFire()
                && !event.getEntity().isOnFire()
                && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.FIRE)) {
//            ElementalArsenal.LOGGER.debug("Non-burning creature attacked by burning zombie while holding fire-upgraded shield");
            ServerScheduler.schedule(0, () -> event.getEntity().setRemainingFireTicks(0));
            ServerScheduler.schedule(0, () -> event.getEntity().clearFire());
//            ServerScheduler.schedule(0, () -> event.getEntity().push(0, 5, 0));
            SendMessageUtil.triggered(UpgradeType.FIRE, event.getEntity());
        }
    }

    //Fire tools smelt block drops
    @SubscribeEvent
    public static void destroyBlock(BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof ServerLevel level) {
            Player player = event.getPlayer();
            BlockState state = event.getState();
            BlockPos pos = event.getPos();
            ItemStack heldStack = player.getMainHandItem();

            if (!player.isCreative()
                    && player.hasCorrectToolForDrops(state)
                    && isActive(heldStack)
                    && ItemIdentificationUtil.isUpgradedTool(heldStack, UpgradeType.FIRE)) {
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

    //Toggle auto smelting on sneak=right-click
    @SubscribeEvent
    public static void toggleAbilities(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().isCrouching()
                && !event.getItemStack().isEmpty()
                && event.getItemStack().getItem() instanceof ModularItem
                && (event.getItemStack().getItem() instanceof ModularBladedItem
                || event.getItemStack().getItem() instanceof ModularDoubleHeadedItem
                || event.getItemStack().getItem() instanceof ModularSingleHeadedItem)
                && ItemIdentificationUtil.isUpgradedTool(event.getItemStack(), UpgradeType.FIRE)) {

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            toggleActive(event.getItemStack(), event.getEntity());
        }
    }

    public static void toggleActive(ItemStack stack, Player player) {
        if (isActive(stack)) {
            stack.getOrCreateTag().putBoolean("FireUpgradeSmeltingDisabled", true);
        } else {
            stack.removeTagKey("FireUpgradeSmeltingDisabled");
        }
        if (player.level().isClientSide()) {
            player.sendSystemMessage(Component.translatable(isActive(stack) ? "message.elementalarsenal.fire_toggled_on" : "message.elementalarsenal.fire_toggled_off"));
        }
    }

    private static boolean isActive(ItemStack stack) {
        return ItemIdentificationUtil.isUpgradedTool(stack, UpgradeType.FIRE) &&
                (stack.getTag() == null
                        || !stack.getTag().contains("FireUpgradeSmeltingDisabled")
                        || !stack.getTag().getBoolean("FireUpgradeSmeltingDisabled"));
    }

}

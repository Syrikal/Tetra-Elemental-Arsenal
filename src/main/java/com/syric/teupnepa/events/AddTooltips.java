package com.syric.teupnepa.events;

import com.rolfmao.upgradednetherite.config.UpgradedNetheriteConfig;
import com.rolfmao.upgradednetherite.utils.ToolUtil;
import com.rolfmao.upgradednetherite.utils.tool.*;
import com.rolfmao.upgradednetherite_ultimate.config.UpgradedNetheriteUltimateConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;
import se.mickelus.tetra.module.data.EffectData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(
        modid = "teupnepa",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class AddTooltips {

    @SubscribeEvent
    public void TooltipEvent(ItemTooltipEvent event) {
//        TeUpNePa.LOGGER.debug("Detected TooltipEvent");
        if (event.getItemStack().getItem() instanceof ModularItem && !UpgradedNetheriteConfig.DisableTooltips && Screen.hasShiftDown() && event.getPlayer() != null) {
//            TeUpNePa.LOGGER.debug("TooltipEvent triggered");
            ModularItem modularItem = (ModularItem) event.getItemStack().getItem();
            EffectData effectData = modularItem.getEffectData(event.getItemStack());
            String[] upgrades = {"corrupt", "ender", "feather", "fire", "gold", "phantom", "poison", "water", "wither", "ultimate"};
            ArrayList<String> allEffects = new ArrayList<>();
            for (String upgrade : upgrades) {
                allEffects.add("upgradednetherite:" + upgrade);
                allEffects.add("upgradednetherite:" + upgrade + "_tool");
                allEffects.add("upgradednetherite:" + upgrade + "_weapon");
                allEffects.add("upgradednetherite:" + upgrade + "_both");
            }
            ArrayList<String> ultimateEffects = new ArrayList<>();
            ultimateEffects.add("upgradednetherite:ultimate");
            ultimateEffects.add("upgradednetherite:ultimate_tool");
            ultimateEffects.add("upgradednetherite:ultimate_weapon");
            ultimateEffects.add("upgradednetherite:ultimate_both");

            if (effectData.levelMap.entrySet().stream().anyMatch((effect) -> allEffects.contains(effect.getKey().getKey()))) {
//                TeUpNePa.LOGGER.debug("Modular item is an upgraded netherite item");

                boolean ultimate = effectData.levelMap.entrySet().stream().anyMatch((effect) -> ultimateEffects.contains(effect.getKey().getKey()));

                if (ultimate) {
                    addUltimateLines(event, event.getItemStack(), effectData);
                    return;
                }

                boolean both = addBothLines(event, event.getItemStack());
                if (both) {
                    return;
                }
                addWeaponLines(event, event.getItemStack());
                addToolLines(event, event.getItemStack());
                addRangedWeaponLines(event, event.getItemStack());
                addShieldLines(event, event.getItemStack());
            }
        }
//        TeUpNePa.LOGGER.debug("TooltipEvent did not fire. ModularItem? " + (event.getItemStack().getItem() instanceof ModularItem) + ", tooltips enabled? " + (!UpgradedNetheriteConfig.DisableTooltips) + ", shift down? " + Screen.hasShiftDown() + ", player not null? " + (event.getPlayer() != null) );
    }

    public boolean addBothLines(ItemTooltipEvent event, ItemStack stack) {
        List<ITextComponent> tooltip = event.getToolTip();
        float EnchantBonus;
        Map<Enchantment, Integer> enchantments;
        int EnchantLevel;
        if (GoldUtil.isGoldTool(stack) && GoldUtil.isGoldMeleeWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusGoldWeapon || UpgradedNetheriteConfig.EnableFortuneBonus || UpgradedNetheriteConfig.EnableLootingBonus)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusGoldWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Gold_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusGoldWeapon + "%");
            }

            EnchantBonus = 0.0F;
            enchantments = EnchantmentHelper.getEnchantments(stack);
            if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.BLOCK_FORTUNE)) {
                EnchantLevel = enchantments.get(Enchantments.BLOCK_FORTUNE);
                EnchantBonus = (float) EnchantLevel;
            }

            float EnchantBonusLooting = 0.0F;
            if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.MOB_LOOTING)) {
                EnchantLevel = enchantments.get(Enchantments.MOB_LOOTING);
                EnchantBonusLooting = (float) EnchantLevel;
            }

            if (UpgradedNetheriteConfig.EnableFortuneBonus) {
                if (EnchantBonus >= 3.0F) {
                    addWithArguments(tooltip, "upgradednetherite.Gold_Tool.TT", "§6" + (UpgradedNetheriteConfig.FortuneBonus + UpgradedNetheriteConfig.FortuneEnchantBonus));
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Gold_Tool.TT", "§6" + UpgradedNetheriteConfig.FortuneBonus);
                }
            }

            if (UpgradedNetheriteConfig.EnableLootingBonus) {
                if (EnchantBonusLooting >= 3.0F) {
                    addWithArguments(tooltip, "upgradednetherite.Gold_Weapon2.TT", "§6" + (UpgradedNetheriteConfig.LootingBonus + UpgradedNetheriteConfig.LootingEnchantBonus));
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Gold_Weapon2.TT", "§6" + UpgradedNetheriteConfig.LootingBonus);
                }
            }

            if (EnchantBonus < 3.0F && UpgradedNetheriteConfig.EnableFortuneBonus) {
                addWithArguments(tooltip, "upgradednetherite.Gold_Tool2.TT", "§d" + UpgradedNetheriteConfig.FortuneEnchantBonus);
            }

            if (EnchantBonusLooting < 3.0F && UpgradedNetheriteConfig.EnableLootingBonus) {
                if (!(EnchantBonusLooting > 0.0F)) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Gold_Bow.TT"));
                }

                addWithArguments(tooltip, "upgradednetherite.Gold_Weapon3.TT", "§d" + UpgradedNetheriteConfig.LootingEnchantBonus);
            }
        }
        else if (FireUtil.isFireTool(stack) && FireUtil.isFireMeleeWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusFireWeapon || UpgradedNetheriteConfig.EnableAutoSmelt)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusFireWeapon) {
                EnchantBonus = 0.0F;
                enchantments = EnchantmentHelper.getEnchantments(stack);
                if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.FIRE_ASPECT)) {
                    EnchantLevel = enchantments.get(Enchantments.FIRE_ASPECT);
                    EnchantBonus = (float) EnchantLevel;
                }

                if (EnchantBonus >= 2.0F) {
                    addWithArguments(tooltip, "upgradednetherite.Fire_Weapon.TT", "§6" + (UpgradedNetheriteConfig.DamageBonusFireWeapon + UpgradedNetheriteConfig.DamageBonusFireEnchantWeapon) + "%");
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Fire_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusFireWeapon + "%");
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Fire_Weapon3.TT"));
                    addWithArguments(tooltip, "upgradednetherite.Fire_Weapon2.TT", "§d" + UpgradedNetheriteConfig.DamageBonusFireEnchantWeapon + "%");
                }
            }

            if (UpgradedNetheriteConfig.EnableAutoSmelt) {
                if (ToolUtil.getDisableEffect(stack)) {
                    addWithArguments(tooltip, "upgradednetherite.Fire_Tool.TT", "§c• ");
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Fire_Tool.TT", "§7• ");
                }
            }
        }
        else if (EnderUtil.isEnderTool(stack) && EnderUtil.isEnderMeleeWeapon(stack)) {
            if (UpgradedNetheriteConfig.EnablePreventTeleport) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Weapon.TT"));
            }

            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusEnderWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Ender_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusEnderWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableDoubleLootingBonusEnderWeapon) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Weapon3.TT"));
            }

            if (UpgradedNetheriteConfig.EnableTeleportChest) {
                if (ToolUtil.getDisableEffect(stack)) {
                    addWithArguments(tooltip, "upgradednetherite.Ender_Tool.TT", "§c• ");
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Ender_Tool.TT", "§7• ");
                }

                if (Objects.requireNonNull(stack.getTag()).getBoolean("UpgradedNetherite_Tagged")) {
                    String world = Objects.requireNonNull(event.getPlayer()).level.dimension().location().getPath();
                    if (!world.equals(stack.getTag().getString("UpgradedNetherite_Dimension"))) {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Dim.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.RED))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    } else {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.BLUE))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    }
                }
            }
        }
        else if (WaterUtil.isWaterTool(stack) && WaterUtil.isWaterMeleeWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusWaterWeapon || UpgradedNetheriteConfig.EnableDamageBonusWaterEndermanWeapon || UpgradedNetheriteConfig.EnableMiningSpeedUnderwater)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusWaterWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Water_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusWaterWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableDamageBonusWaterEndermanWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Water_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusWaterEndermanWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableMiningSpeedUnderwater) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Water_Tool.TT"));
            }
        }
        else if (WitherUtil.isWitherTool(stack) && WitherUtil.isWitherMeleeWeapon(stack)) {
            if (UpgradedNetheriteConfig.EnableWitherEffect) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Wither_Weapon.TT"));
            }

            if (UpgradedNetheriteConfig.EnableDamageBonusWitherWeapon) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                addWithArguments(tooltip, "upgradednetherite.Wither_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusWitherWeapon + "%");
            }
        }
        else if (PoisonUtil.isPoisonTool(stack) && PoisonUtil.isPoisonMeleeWeapon(stack)) {
            if (UpgradedNetheriteConfig.EnablePoisonEffect) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Poison_Weapon.TT"));
            }

            if (UpgradedNetheriteConfig.EnableDamageBonusPoisonWeapon) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                addWithArguments(tooltip, "upgradednetherite.Poison_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusPoisonWeapon + "%");
            }
        }
        else if (PhantomUtil.isPhantomTool(stack) && PhantomUtil.isPhantomMeleeWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusPhantomWeapon || UpgradedNetheriteConfig.EnableGlowingEffect || UpgradedNetheriteConfig.EnableReachEffect)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusPhantomWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Phantom_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusPhantomWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableGlowingEffect) {
                if (ToolUtil.getDisableEffect(stack)) {
                    addWithArguments(tooltip, "upgradednetherite.Phantom_Tool.TT", "§c• ");
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Phantom_Tool.TT", "§7• ");
                }
            }

            if (UpgradedNetheriteConfig.EnableReachEffect) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Phantom_Tool2.TT"));
            }
        }
        else if (FeatherUtil.isFeatherTool(stack) && FeatherUtil.isFeatherMeleeWeapon(stack) && UpgradedNetheriteConfig.EnableAttractItem) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (ToolUtil.getDisableEffect(stack)) {
                addWithArguments(tooltip, "upgradednetherite.Feather_Tool.TT", "§c• ");
            } else {
                addWithArguments(tooltip, "upgradednetherite.Feather_Tool.TT", "§7• ");
            }
        }
        else if (CorruptUtil.isCorruptWeapon(stack) && CorruptUtil.isCorruptTool(stack)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Malus.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Corrupt_Bonus2.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Corrupt_Enchant.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusCorruptWeapon || UpgradedNetheriteConfig.EnableLootingBonusCorruptWeapon || UpgradedNetheriteConfig.EnableFortuneBonusCorruptTool) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            }

            if (Minecraft.getInstance().player != null && CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) > 0) {
                if (UpgradedNetheriteConfig.EnableDamageBonusCorruptWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon3.TT", "§6" + CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) * UpgradedNetheriteConfig.DamageBonusCorruptWeapon + "%");
                }

                if (UpgradedNetheriteConfig.EnableLootingBonusCorruptWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon4.TT", "§6" + CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) * UpgradedNetheriteConfig.LootingBonusCorruptWeapon);
                }

                if (UpgradedNetheriteConfig.EnableFortuneBonusCorruptTool) {
                    addWithArguments(tooltip, "upgradednetherite.Corrupt_Tool2.TT", "§6" + CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) * UpgradedNetheriteConfig.FortuneBonusCorruptTool);
                }
            }

            EnchantBonus = 0.0F;
            enchantments = EnchantmentHelper.getEnchantments(stack);
            if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.MOB_LOOTING)) {
                EnchantLevel = enchantments.get(Enchantments.MOB_LOOTING);
                EnchantBonus = (float) EnchantLevel;
            }

            if (!(EnchantBonus > 0.0F)) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Gold_Bow.TT"));
            }

            if (UpgradedNetheriteConfig.EnableDamageBonusCorruptWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon2.TT", "§d" + UpgradedNetheriteConfig.DamageBonusCorruptWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableLootingBonusCorruptWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon.TT", "§d" + UpgradedNetheriteConfig.LootingBonusCorruptWeapon);
            }

            if (UpgradedNetheriteConfig.EnableFortuneBonusCorruptTool) {
                addWithArguments(tooltip, "upgradednetherite.Corrupt_Tool.TT", "§d" + UpgradedNetheriteConfig.FortuneBonusCorruptTool);
            }
        }
        else {
            return false;
        }
        return true;
    }

    public void addWeaponLines(ItemTooltipEvent event, ItemStack stack) {
        List<ITextComponent> tooltip = event.getToolTip();
        Map<Enchantment, Integer> enchantments;
        int EnchantLevel;
        float EnchantBonus;
        if (GoldUtil.isGoldMeleeWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusGoldWeapon || UpgradedNetheriteConfig.EnableLootingBonus)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusGoldWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Gold_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusGoldWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableLootingBonus) {
                EnchantBonus = 0.0F;
                enchantments = EnchantmentHelper.getEnchantments(stack);
                if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.MOB_LOOTING)) {
                    EnchantLevel = enchantments.get(Enchantments.MOB_LOOTING);
                    EnchantBonus = (float) EnchantLevel;
                }

                if (EnchantBonus >= 3.0F) {
                    addWithArguments(tooltip, "upgradednetherite.Gold_Weapon2.TT", "§6" + (UpgradedNetheriteConfig.LootingBonus + UpgradedNetheriteConfig.LootingEnchantBonus));
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Gold_Weapon2.TT", "§6" + UpgradedNetheriteConfig.LootingBonus);
                    addWithArguments(tooltip, "upgradednetherite.Gold_Weapon3.TT", "§d" + UpgradedNetheriteConfig.LootingEnchantBonus);
                }
            }
        }
        else if (FireUtil.isFireMeleeWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusFireWeapon || UpgradedNetheriteConfig.EnableAutoSmelt)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusFireWeapon) {
                EnchantBonus = 0.0F;
                enchantments = EnchantmentHelper.getEnchantments(stack);
                if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.FIRE_ASPECT)) {
                    EnchantLevel = enchantments.get(Enchantments.FIRE_ASPECT);
                    EnchantBonus = (float) EnchantLevel;
                }

                if (EnchantBonus >= 2.0F) {
                    addWithArguments(tooltip, "upgradednetherite.Fire_Weapon.TT", "§6" + (UpgradedNetheriteConfig.DamageBonusFireWeapon + UpgradedNetheriteConfig.DamageBonusFireEnchantWeapon) + "%");
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Fire_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusFireWeapon + "%");
                    addWithArguments(tooltip, "upgradednetherite.Fire_Weapon2.TT", "§d" + UpgradedNetheriteConfig.DamageBonusFireEnchantWeapon + "%");
                }
            }

            if (UpgradedNetheriteConfig.EnableAutoSmelt) {
                if (ToolUtil.getDisableEffect(stack)) {
                    addWithArguments(tooltip, "upgradednetherite.Fire_Tool.TT", "§c• ");
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Fire_Tool.TT", "§7• ");
                }
            }
        }
        else if (EnderUtil.isEnderMeleeWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusEnderWeapon || UpgradedNetheriteConfig.EnablePreventTeleport || UpgradedNetheriteConfig.EnableDoubleLootingBonusEnderWeapon || UpgradedNetheriteConfig.EnableTeleportChest)) {
            if (UpgradedNetheriteConfig.EnablePreventTeleport) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Weapon.TT"));
            }

            if (UpgradedNetheriteConfig.EnableDamageBonusEnderWeapon || UpgradedNetheriteConfig.EnableDoubleLootingBonusEnderWeapon || UpgradedNetheriteConfig.EnableTeleportChest) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                if (UpgradedNetheriteConfig.EnableDamageBonusEnderWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Ender_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusEnderWeapon + "%");
                }

                if (UpgradedNetheriteConfig.EnableDoubleLootingBonusEnderWeapon) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Weapon3.TT"));
                }

                if (UpgradedNetheriteConfig.EnableTeleportChest) {
                    if (ToolUtil.getDisableEffect(stack)) {
                        addWithArguments(tooltip, "upgradednetherite.Ender_Tool.TT", "§c• ");
                    } else {
                        addWithArguments(tooltip, "upgradednetherite.Ender_Tool.TT", "§7• ");
                    }

                    if (Objects.requireNonNull(stack.getTag()).getBoolean("UpgradedNetherite_Tagged")) {
                        String world = Objects.requireNonNull(event.getPlayer()).level.dimension().location().getPath();
                        if (!world.equals(stack.getTag().getString("UpgradedNetherite_Dimension"))) {
                            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                            tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                            tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Dim.TT"));
                            tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                    .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.RED))
                                    .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                    .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                    .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                    .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                    .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                    .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                    .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));                        } else {
                            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                            tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                            tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                    .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.BLUE))
                                    .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                    .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                    .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                    .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                    .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                    .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                    .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));                        }
                    }
                }
            }
        }
        else if (WaterUtil.isWaterMeleeWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusWaterWeapon || UpgradedNetheriteConfig.EnableDamageBonusWaterEndermanWeapon || UpgradedNetheriteConfig.EnableMiningSpeedUnderwater)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusWaterWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Water_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusWaterWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableDamageBonusWaterEndermanWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Water_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusWaterEndermanWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableMiningSpeedUnderwater) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Water_Tool.TT"));
            }
        }
        else if (WitherUtil.isWitherMeleeWeapon(stack)) {
            if (UpgradedNetheriteConfig.EnableWitherEffect) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Wither_Weapon.TT"));
            }

            if (UpgradedNetheriteConfig.EnableDamageBonusWitherWeapon) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                addWithArguments(tooltip, "upgradednetherite.Wither_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusWitherWeapon + "%");
            }
        }
        else if (PoisonUtil.isPoisonMeleeWeapon(stack)) {
            if (UpgradedNetheriteConfig.EnablePoisonEffect) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Poison_Weapon.TT"));
            }

            if (UpgradedNetheriteConfig.EnableDamageBonusPoisonWeapon) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                addWithArguments(tooltip, "upgradednetherite.Poison_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusPoisonWeapon + "%");
            }
        }
        else if (PhantomUtil.isPhantomMeleeWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusPhantomWeapon || UpgradedNetheriteConfig.EnableGlowingEffect || UpgradedNetheriteConfig.EnableReachEffect)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusPhantomWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Phantom_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusPhantomWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableGlowingEffect) {
                if (ToolUtil.getDisableEffect(stack)) {
                    addWithArguments(tooltip, "upgradednetherite.Phantom_Tool.TT", "§c• ");
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Phantom_Tool.TT", "§7• ");
                }
            }

            if (UpgradedNetheriteConfig.EnableReachEffect) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Phantom_Tool2.TT"));
            }
        }
        else if (FeatherUtil.isFeatherMeleeWeapon(stack) && UpgradedNetheriteConfig.EnableAttractItem) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (ToolUtil.getDisableEffect(stack)) {
                addWithArguments(tooltip, "upgradednetherite.Feather_Tool.TT", "§c• ");
            } else {
                addWithArguments(tooltip, "upgradednetherite.Feather_Tool.TT", "§7• ");
            }
        }
        else if (CorruptUtil.isCorruptMeleeWeapon(stack)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Malus.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Corrupt_Bonus2.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Corrupt_Enchant.TT"));
            if (UpgradedNetheriteConfig.EnableDamageBonusCorruptWeapon || UpgradedNetheriteConfig.EnableLootingBonusCorruptWeapon) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            }

            if (Minecraft.getInstance().player != null && CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) > 0) {
                if (UpgradedNetheriteConfig.EnableDamageBonusCorruptWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon3.TT", "§6" + CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) * UpgradedNetheriteConfig.DamageBonusCorruptWeapon + "%");
                }

                if (UpgradedNetheriteConfig.EnableLootingBonusCorruptWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon4.TT", "§6" + CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) * UpgradedNetheriteConfig.LootingBonusCorruptWeapon);
                }
            }

            if (UpgradedNetheriteConfig.EnableDamageBonusCorruptWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon2.TT", "§d" + UpgradedNetheriteConfig.DamageBonusCorruptWeapon + "%");
            }

            if (UpgradedNetheriteConfig.EnableLootingBonusCorruptWeapon) {
                addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon.TT", "§d" + UpgradedNetheriteConfig.LootingBonusCorruptWeapon);
            }
        }
    }

    public void addToolLines(ItemTooltipEvent event, ItemStack stack) {
        List<ITextComponent> tooltip = event.getToolTip();
        if (GoldUtil.isGoldTool(stack) && UpgradedNetheriteConfig.EnableFortuneBonus) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            float EnchantBonus = 0.0F;
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.BLOCK_FORTUNE)) {
                int EnchantLevel = enchantments.get(Enchantments.BLOCK_FORTUNE);
                EnchantBonus = (float) EnchantLevel;
            }

            if (EnchantBonus >= 3.0F) {
                addWithArguments(tooltip, "upgradednetherite.Gold_Tool.TT", "§6" + (UpgradedNetheriteConfig.FortuneBonus + UpgradedNetheriteConfig.FortuneEnchantBonus));
            } else {
                addWithArguments(tooltip, "upgradednetherite.Gold_Tool.TT", "§6" + UpgradedNetheriteConfig.FortuneBonus);
                addWithArguments(tooltip, "upgradednetherite.Gold_Tool2.TT", "§d" + UpgradedNetheriteConfig.FortuneEnchantBonus);
            }
        }
        else if (FireUtil.isFireTool(stack) && UpgradedNetheriteConfig.EnableAutoSmelt) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (ToolUtil.getDisableEffect(stack)) {
                addWithArguments(tooltip, "upgradednetherite.Fire_Tool.TT", "§c• ");
            } else {
                addWithArguments(tooltip, "upgradednetherite.Fire_Tool.TT", "§7• ");
            }
        }
        else if (EnderUtil.isEnderTool(stack) && UpgradedNetheriteConfig.EnableTeleportChest) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (ToolUtil.getDisableEffect(stack)) {
                addWithArguments(tooltip, "upgradednetherite.Ender_Tool.TT", "§c• ");
            } else {
                addWithArguments(tooltip, "upgradednetherite.Ender_Tool.TT", "§7• ");
            }

            if (stack.getTag() != null && stack.getTag().contains("UpgradedNetherite_Tagged") && stack.getTag().getBoolean("UpgradedNetherite_Tagged")) {
                String world = Objects.requireNonNull(event.getPlayer()).level.dimension().location().getPath();
                if (!world.equals(stack.getTag().getString("UpgradedNetherite_Dimension"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Dim.TT"));
                    tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                            .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.RED))
                            .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                            .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                            .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                            .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                            .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                            .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                            .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));                } else {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                    tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                            .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.BLUE))
                            .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                            .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                            .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                            .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                            .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                            .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                            .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));                }
            }
        }
        else if (WaterUtil.isWaterTool(stack) && UpgradedNetheriteConfig.EnableMiningSpeedUnderwater) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Water_Tool.TT"));
        }
        else if (PhantomUtil.isPhantomTool(stack) && (UpgradedNetheriteConfig.EnableGlowingEffect || UpgradedNetheriteConfig.EnableReachEffect)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (UpgradedNetheriteConfig.EnableGlowingEffect) {
                if (ToolUtil.getDisableEffect(stack)) {
                    addWithArguments(tooltip, "upgradednetherite.Phantom_Tool.TT", "§c• ");
                } else {
                    addWithArguments(tooltip, "upgradednetherite.Phantom_Tool.TT", "§7• ");
                }
            }

            if (UpgradedNetheriteConfig.EnableReachEffect) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Phantom_Tool2.TT"));
            }
        }
        else if (FeatherUtil.isFeatherTool(stack) && UpgradedNetheriteConfig.EnableAttractItem) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
            if (ToolUtil.getDisableEffect(stack)) {
                addWithArguments(tooltip, "upgradednetherite.Feather_Tool.TT", "§c• ");
            } else {
                addWithArguments(tooltip, "upgradednetherite.Feather_Tool.TT", "§7• ");
            }
        }
        else if (CorruptUtil.isCorruptTool(stack)) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Malus.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Corrupt_Bonus2.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Corrupt_Enchant.TT"));
            if (UpgradedNetheriteConfig.EnableFortuneBonusCorruptTool) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                if (Minecraft.getInstance().player != null && CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) > 0) {
                    addWithArguments(tooltip, "upgradednetherite.Corrupt_Tool2.TT", "§6" + CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) * UpgradedNetheriteConfig.FortuneBonusCorruptTool);
                }

                addWithArguments(tooltip, "upgradednetherite.Corrupt_Tool.TT", "§d" + UpgradedNetheriteConfig.FortuneBonusCorruptTool);
            }
        }
    }

    public void addRangedWeaponLines(ItemTooltipEvent event, ItemStack stack) {
        List<ITextComponent> tooltip = event.getToolTip();
            float EnchantBonus;
            Map<Enchantment, Integer> enchantments;
            int EnchantLevel;
            if (GoldUtil.isGoldRangedWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusGoldWeapon || UpgradedNetheriteConfig.EnableLootingBonus)) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                if (UpgradedNetheriteConfig.EnableDamageBonusGoldWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Gold_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusGoldWeapon + "%");
                }

                if (UpgradedNetheriteConfig.EnableLootingBonus) {
                    EnchantBonus = 0.0F;
                    enchantments = EnchantmentHelper.getEnchantments(stack);
                    if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.MOB_LOOTING)) {
                        EnchantLevel = enchantments.get(Enchantments.MOB_LOOTING);
                        EnchantBonus = (float) EnchantLevel;
                    }

                    if (EnchantBonus >= 3.0F) {
                        addWithArguments(tooltip, "upgradednetherite.Gold_Weapon2.TT", "§6" + (UpgradedNetheriteConfig.LootingBonus + UpgradedNetheriteConfig.LootingEnchantBonus));
                    } else {
                        addWithArguments(tooltip, "upgradednetherite.Gold_Weapon2.TT", "§6" + UpgradedNetheriteConfig.LootingBonus);
                        if (!(EnchantBonus > 0.0F)) {
                            tooltip.add(new TranslationTextComponent("upgradednetherite.Gold_Bow.TT"));
                        }

                        addWithArguments(tooltip, "upgradednetherite.Gold_Weapon3.TT", "§d" + UpgradedNetheriteConfig.LootingEnchantBonus);
                    }
                }
            }
            else if (FireUtil.isFireRangedWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusFireWeapon || UpgradedNetheriteConfig.EnableAutoSmelt)) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                if (UpgradedNetheriteConfig.EnableDamageBonusFireWeapon) {
                    EnchantBonus = 0.0F;
                    enchantments = EnchantmentHelper.getEnchantments(stack);
                    if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.FLAMING_ARROWS)) {
                        EnchantLevel = enchantments.get(Enchantments.FLAMING_ARROWS);
                        EnchantBonus = (float) EnchantLevel;
                    }

                    tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                    if (EnchantBonus >= 1.0F) {
                        addWithArguments(tooltip, "upgradednetherite.Fire_Weapon.TT", "§6" + (UpgradedNetheriteConfig.DamageBonusFireWeapon + UpgradedNetheriteConfig.DamageBonusFireEnchantWeapon) + "%");
                    } else {
                        addWithArguments(tooltip, "upgradednetherite.Fire_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusFireWeapon + "%");
                        addWithArguments(tooltip, "upgradednetherite.Fire_Bow.TT", "§d" + UpgradedNetheriteConfig.DamageBonusFireEnchantWeapon + "%");
                    }
                }

                if (UpgradedNetheriteConfig.EnableAutoSmelt) {
                    addWithArguments(tooltip, "upgradednetherite.Fire_Tool.TT", "§7• ");
                }
            }
            else if (EnderUtil.isEnderRangedWeapon(stack)) {
            if (UpgradedNetheriteConfig.EnablePreventTeleport) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Weapon.TT"));
            }

            if (UpgradedNetheriteConfig.EnableTeleportChest) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Tool.TT"));
                if (stack.getTag() != null && stack.getTag().contains("UpgradedNetherite_Tagged") && stack.getTag().getBoolean("UpgradedNetherite_Tagged")) {
                    String world = Objects.requireNonNull(event.getPlayer()).level.dimension().location().getPath();
                    if (!world.equals(stack.getTag().getString("UpgradedNetherite_Dimension"))) {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Dim.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.RED))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));                    } else {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.BLUE))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));                    }
                }
            }
        }
            else if (WaterUtil.isWaterRangedWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusWaterWeapon || UpgradedNetheriteConfig.EnableMiningSpeedUnderwater)) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                if (UpgradedNetheriteConfig.EnableDamageBonusWaterWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Water_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusWaterWeapon + "%");
                }

                if (UpgradedNetheriteConfig.EnableMiningSpeedUnderwater) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Water_Tool.TT"));
                }
            }
            else if (WitherUtil.isWitherRangedWeapon(stack)) {
                if (UpgradedNetheriteConfig.EnableWitherEffect) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Wither_Weapon.TT"));
                }

                if (UpgradedNetheriteConfig.EnableDamageBonusWitherWeapon) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                    addWithArguments(tooltip, "upgradednetherite.Wither_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusWitherWeapon + "%");
                }
            }
            else if (PoisonUtil.isPoisonRangedWeapon(stack)) {
                if (UpgradedNetheriteConfig.EnablePoisonEffect) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Poison_Weapon.TT"));
                }

                if (UpgradedNetheriteConfig.EnableDamageBonusPoisonWeapon) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                    addWithArguments(tooltip, "upgradednetherite.Poison_Weapon2.TT", "§6" + UpgradedNetheriteConfig.DamageBonusPoisonWeapon + "%");
                }
            }
            else if (PhantomUtil.isPhantomRangedWeapon(stack) && (UpgradedNetheriteConfig.EnableDamageBonusPhantomWeapon || UpgradedNetheriteConfig.EnableGlowingEffect || UpgradedNetheriteConfig.EnableReachEffect)) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                if (UpgradedNetheriteConfig.EnableDamageBonusPhantomWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Phantom_Weapon.TT", "§6" + UpgradedNetheriteConfig.DamageBonusPhantomWeapon + "%");
                }

                if (UpgradedNetheriteConfig.EnableGlowingEffect) {
                    addWithArguments(tooltip, "upgradednetherite.Phantom_Tool.TT", "§7• ");
                }

                if (UpgradedNetheriteConfig.EnableReachEffect) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Phantom_Tool2.TT"));
                }
            }
            else if (FeatherUtil.isFeatherRangedWeapon(stack)) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.OnHit.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Feather_Bow.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                addWithArguments(tooltip, "upgradednetherite.Feather_Tool.TT", "§7• ");
            }
            else if (CorruptUtil.isCorruptRangedWeapon(stack)) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Malus.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Corrupt_Bonus2.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Corrupt_Enchant.TT"));
                if (UpgradedNetheriteConfig.EnableDamageBonusCorruptWeapon || UpgradedNetheriteConfig.EnableLootingBonusCorruptWeapon) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Bonus.TT"));
                }

                if (Minecraft.getInstance().player != null && CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) > 0) {
                    if (UpgradedNetheriteConfig.EnableDamageBonusCorruptWeapon) {
                        addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon3.TT", "§6" + CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) * UpgradedNetheriteConfig.DamageBonusCorruptWeapon + "%");
                    }

                    if (UpgradedNetheriteConfig.EnableLootingBonusCorruptWeapon) {
                        addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon4.TT", "§6" + CorruptUtil.intWearingCorruptArmor(Minecraft.getInstance().player, true) * UpgradedNetheriteConfig.LootingBonusCorruptWeapon);
                    }
                }

                EnchantBonus = 0.0F;
                enchantments = EnchantmentHelper.getEnchantments(stack);
                if (!enchantments.isEmpty() && enchantments.containsKey(Enchantments.MOB_LOOTING)) {
                    EnchantLevel = enchantments.get(Enchantments.MOB_LOOTING);
                    EnchantBonus = (float) EnchantLevel;
                }

                if (!(EnchantBonus > 0.0F)) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite.Gold_Bow.TT"));
                }

                if (UpgradedNetheriteConfig.EnableDamageBonusCorruptWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon2.TT", "§d" + UpgradedNetheriteConfig.DamageBonusCorruptWeapon + "%");
                }

                if (UpgradedNetheriteConfig.EnableLootingBonusCorruptWeapon) {
                    addWithArguments(tooltip, "upgradednetherite.Corrupt_Weapon.TT", "§d" + UpgradedNetheriteConfig.LootingBonusCorruptWeapon);
                }
            }
        }

    public void addShieldLines(ItemTooltipEvent event, ItemStack stack) {
        List<ITextComponent> tooltip = event.getToolTip();
        if (GoldUtil.isGoldShield(stack) && UpgradedNetheriteConfig.EnableGoldShield) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.WhenBlocking.TT"));
            addWithArguments(tooltip, "upgradednetherite.Gold_Shield.TT", "§6" + (float) UpgradedNetheriteConfig.DamageBonusGoldWeapon / 20.0F + "%", "§6" + (float) UpgradedNetheriteConfig.DamageBonusGoldWeapon / 2.0F + "%");
        }
        else if (FireUtil.isFireShield(stack) && UpgradedNetheriteConfig.EnableFireShield) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.WhenBlocking.TT"));
            addWithArguments(tooltip, "upgradednetherite.Fire_Shield.TT", "§6" + (float) UpgradedNetheriteConfig.DamageBonusFireWeapon / 20.0F + "%", "§6" + (float) UpgradedNetheriteConfig.DamageBonusFireWeapon / 2.0F + "%");
        }
        else if (EnderUtil.isEnderShield(stack) && UpgradedNetheriteConfig.EnableEnderShield) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.WhenBlocking.TT"));
            addWithArguments(tooltip, "upgradednetherite.Ender_Shield.TT", "§6" + (float) UpgradedNetheriteConfig.DamageBonusEnderWeapon / 20.0F + "%", "§6" + (float) UpgradedNetheriteConfig.DamageBonusEnderWeapon / 2.0F + "%");
        }
        else if (WaterUtil.isWaterShield(stack) && UpgradedNetheriteConfig.EnableWaterShield) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.WhenBlocking.TT"));
            addWithArguments(tooltip, "upgradednetherite.Water_Shield.TT", "§6" + (float) UpgradedNetheriteConfig.DamageBonusWaterWeapon / 20.0F + "%", "§6" + (float) UpgradedNetheriteConfig.DamageBonusWaterWeapon / 2.0F + "%");
        }
        else if (WitherUtil.isWitherShield(stack) && UpgradedNetheriteConfig.EnableWitherShield) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.WhenBlocking.TT"));
            addWithArguments(tooltip, "upgradednetherite.Wither_Shield.TT", "§6" + (float) UpgradedNetheriteConfig.DamageBonusWitherWeapon / 20.0F + "%", "§6" + (float) UpgradedNetheriteConfig.DamageBonusWitherWeapon / 2.0F + "%");
        }
        else if (PoisonUtil.isPoisonShield(stack) && UpgradedNetheriteConfig.EnablePoisonShield) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.WhenBlocking.TT"));
            addWithArguments(tooltip, "upgradednetherite.Poison_Shield.TT", "§6" + (float) UpgradedNetheriteConfig.DamageBonusPoisonWeapon / 20.0F + "%", "§6" + (float) UpgradedNetheriteConfig.DamageBonusPoisonWeapon / 2.0F + "%");
        }
        else if (PhantomUtil.isPhantomShield(stack) && UpgradedNetheriteConfig.EnablePhantomShield) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.WhenBlocking.TT"));
            addWithArguments(tooltip, "upgradednetherite.Phantom_Shield.TT", "§6" + (float) UpgradedNetheriteConfig.DamageBonusPhantomWeapon / 20.0F + "%", "§6" + (float) UpgradedNetheriteConfig.DamageBonusPhantomWeapon / 2.0F + "%");
        }
        else if (FeatherUtil.isFeatherShield(stack) && UpgradedNetheriteConfig.EnableFeatherShield) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.WhenBlocking.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Feather_Shield.TT"));
        }
        else if (CorruptUtil.isCorruptShield(stack) && UpgradedNetheriteConfig.EnableCorruptShield) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.WhenBlocking.TT"));
            addWithArguments(tooltip, "upgradednetherite.Corrupt_Shield.TT", "§6" + (float) UpgradedNetheriteConfig.DamageBonusCorruptWeapon / 20.0F + "%", "§6" + (float) UpgradedNetheriteConfig.DamageBonusCorruptWeapon / 2.0F + "%");
        }
    }

    public void addUltimateLines(ItemTooltipEvent event, ItemStack stack, EffectData effectData) {
        List<ITextComponent> tooltip = event.getToolTip();
        if (!ModList.get().isLoaded("upgradednetherite_ultimate")) {
            tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
            tooltip.add(new TranslationTextComponent("upgradednetherite.Ultimate_Not_Installed.TT"));
        }
        else if (effectData.contains(ItemEffect.get("upgradednetherite:ultimate_both"))) {
             if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold")) || UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire")) || UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder")) || UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater")) || UpgradedNetheriteUltimateConfig.EnableUltimateWitherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWither")) || UpgradedNetheriteUltimateConfig.EnableUltimatePoisonToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePoison")) || UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom")) || UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.BonusFrom.TT"));
                if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Golderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Blazerite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Enderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Prismarite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateWitherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWither"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Witherite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimatePoisonToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePoison"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Spiderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Phanterite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Featherite.TT"));
                }

                if (stack.getTag() != null && stack.getTag().contains("UpgradedNetherite_Tagged") && stack.getTag().getBoolean("UpgradedNetherite_Tagged") && (!stack.hasTag() || !stack.getTag().contains("UpgradedNetherite_DisableEnder"))) {
                    String world = Objects.requireNonNull(event.getPlayer()).level.dimension().location().getPath();
                    if (!world.equals(stack.getTag().getString("UpgradedNetherite_Dimension"))) {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Dim.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.RED))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    } else {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.BLUE))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    }
                }
            } else {
                 tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                 tooltip.add(new TranslationTextComponent("upgradednetherite.Disabled.TT"));
             }
        }
        else if (effectData.contains(ItemEffect.get("upgradednetherite:ultimate_weapon"))) {
            if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold")) || UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire")) || UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder")) || UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater")) || UpgradedNetheriteUltimateConfig.EnableUltimateWitherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWither")) || UpgradedNetheriteUltimateConfig.EnableUltimatePoisonToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePoison")) || UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom")) || UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.BonusFrom.TT"));
                if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Golderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Blazerite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Enderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Prismarite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateWitherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWither"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Witherite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimatePoisonToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePoison"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Spiderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Phanterite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Featherite.TT"));
                }

                if (stack.getTag() != null && stack.getTag().contains("UpgradedNetherite_Tagged") && stack.getTag().getBoolean("UpgradedNetherite_Tagged") && (!stack.hasTag() || !stack.getTag().contains("UpgradedNetherite_DisableEnder"))) {
                    String world = Objects.requireNonNull(event.getPlayer()).level.dimension().location().getPath();
                    if (!world.equals(stack.getTag().getString("UpgradedNetherite_Dimension"))) {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Dim.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.RED))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    } else {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.BLUE))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    }
                }
            } else {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Disabled.TT"));
            }
        }
        else if (effectData.contains(ItemEffect.get("upgradednetherite:ultimate_tool"))) {
            if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold")) || UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire")) || UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder")) || UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater")) || UpgradedNetheriteUltimateConfig.EnableUltimateWitherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWither")) || UpgradedNetheriteUltimateConfig.EnableUltimatePoisonToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePoison")) || UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom")) || UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.BonusFrom.TT"));
                if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Golderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Blazerite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Enderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Prismarite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Phanterite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Featherite.TT"));
                }

                if (stack.getTag() != null && stack.getTag().contains("UpgradedNetherite_Tagged") && stack.getTag().getBoolean("UpgradedNetherite_Tagged") && (!stack.hasTag() || !stack.getTag().contains("UpgradedNetherite_DisableEnder"))) {
                    String world = Objects.requireNonNull(event.getPlayer()).level.dimension().location().getPath();
                    if (!world.equals(stack.getTag().getString("UpgradedNetherite_Dimension"))) {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Dim.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.RED))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    } else {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.BLUE))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    }
                }
            } else {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Disabled.TT"));
            }
        }
        else if (effectData.contains(ItemEffect.get("upgradednetherite:ultimate")) && (stack.getItem() instanceof ModularBowItem || stack.getItem() instanceof ModularCrossbowItem)) {
            if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold")) || UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire")) || UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder")) || UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater")) || UpgradedNetheriteUltimateConfig.EnableUltimateWitherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWither")) || UpgradedNetheriteUltimateConfig.EnableUltimatePoisonToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePoison")) || UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom")) || UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.BonusFrom.TT"));
                if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Golderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Blazerite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Enderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Prismarite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateWitherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWither"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Witherite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimatePoisonToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePoison"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Spiderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Phanterite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Featherite.TT"));
                }

                if (stack.getTag() != null && stack.getTag().contains("UpgradedNetherite_Tagged") && stack.getTag().getBoolean("UpgradedNetherite_Tagged") && (!stack.hasTag() || !stack.getTag().contains("UpgradedNetherite_DisableEnder"))) {
                    String world = Objects.requireNonNull(event.getPlayer()).level.dimension().location().getPath();
                    if (!world.equals(stack.getTag().getString("UpgradedNetherite_Dimension"))) {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Ender_Dim.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.RED))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    } else {
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                        tooltip.add(new TranslationTextComponent("upgradednetherite.Target.TT"));
                        tooltip.add(new StringTextComponent("\u2022").withStyle(TextFormatting.GRAY)
                                .append(new StringTextComponent(stack.getTag().getString("UpgradedNetherite_Dimension")).withStyle(TextFormatting.BLUE))
                                .append(new StringTextComponent(" : ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[0])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[1])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(", ").withStyle(TextFormatting.GRAY))
                                .append(new StringTextComponent(Integer.toString(stack.getTag().getIntArray("UpgradedNetherite_Position")[2])).withStyle(TextFormatting.DARK_AQUA))
                                .append(new StringTextComponent(".").withStyle(TextFormatting.GRAY)));
                    }
                }
            } else {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Disabled.TT"));
            }
        }
        else if (effectData.contains(ItemEffect.get("upgradednetherite:ultimate")) && stack.getItem() instanceof ModularShieldItem) {
            if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold")) || UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire")) || UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder")) || UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater")) || UpgradedNetheriteUltimateConfig.EnableUltimateWitherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWither")) || UpgradedNetheriteUltimateConfig.EnableUltimatePoisonToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePoison")) || UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom")) || UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.BonusFrom.TT"));
                if (UpgradedNetheriteUltimateConfig.EnableUltimateGoldToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableGold"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Golderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFireToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFire"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Blazerite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateEnderToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableEnder"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Enderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateWaterToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWater"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Prismarite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateWitherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableWither"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Witherite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimatePoisonToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePoison"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Spiderite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimatePhantomToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisablePhantom"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Phanterite.TT"));
                }

                if (UpgradedNetheriteUltimateConfig.EnableUltimateFeatherToolEffect && (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains("UpgradedNetherite_DisableFeather"))) {
                    tooltip.add(new TranslationTextComponent("upgradednetherite_ultimate.Featherite.TT"));
                }
            } else {
                tooltip.add(new TranslationTextComponent("upgradednetherite.Blank.TT"));
                tooltip.add(new TranslationTextComponent("upgradednetherite.Disabled.TT"));
            }
        }
    }
    
    private void addWithArguments(List<ITextComponent> tooltip, String line, String... args) {
        IFormattableTextComponent[] text = new TextComponent[args.length];
        int count = 0;

//        TeUpNePa.LOGGER.debug("Attempting to add a line with arguments");

        for (String arg : args) {
//            TeUpNePa.LOGGER.debug("Managing argument '" + arg + "'");
            arg = arg.replace("•", "\u2022");
            if (arg.startsWith("§")) {
                TextFormatting format = TextFormatting.getByCode(arg.charAt(2));
                arg = arg.substring(3);
                text[count] = format == null ? new StringTextComponent(arg) : new StringTextComponent(arg).withStyle(format).withStyle();

//                TeUpNePa.LOGGER.debug("Detected section sign. Applying format " + (format == null ? "null" : format.getName()));
            } else {
                text[count] = new StringTextComponent(arg);
            }
            ++count;
        }

//        TeUpNePa.LOGGER.debug("Adding translation text component " + new TranslationTextComponent(line, (Object[]) text));
        tooltip.add(new TranslationTextComponent(line, (Object[]) text));
    }

}

package com.syric.teupnepa.util;

import com.rolfmao.upgradednetherite.init.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;

public class ItemListUtil {

    public static final ArrayList<Item> gold_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> gold_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> gold_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> gold_netherite_shield = new ArrayList<>();

    public static final ArrayList<Item> fire_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> fire_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> fire_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> fire_netherite_shield = new ArrayList<>();

    public static final ArrayList<Item> ender_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> ender_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> ender_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> ender_netherite_shield = new ArrayList<>();

    public static final ArrayList<Item> water_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> water_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> water_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> water_netherite_shield = new ArrayList<>();

    public static final ArrayList<Item> wither_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> wither_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> wither_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> wither_netherite_shield = new ArrayList<>();

    public static final ArrayList<Item> poison_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> poison_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> poison_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> poison_netherite_shield = new ArrayList<>();

    public static final ArrayList<Item> phantom_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> phantom_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> phantom_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> phantom_netherite_shield = new ArrayList<>();

    public static final ArrayList<Item> feather_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> feather_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> feather_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> feather_netherite_shield = new ArrayList<>();

    public static final ArrayList<Item> corrupt_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> corrupt_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> corrupt_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> corrupt_netherite_shield = new ArrayList<>();

    public static final ArrayList<Item> ultimate_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> ultimate_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> ultimate_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> ultimate_netherite_shield = new ArrayList<>();

    public static void initGold() {
        gold_netherite_tools.add(ModItems.GOLD_UPGRADED_NETHERITE_AXE.get());
        gold_netherite_tools.add(ModItems.GOLD_UPGRADED_NETHERITE_PICKAXE.get());
        gold_netherite_tools.add(ModItems.GOLD_UPGRADED_NETHERITE_SHOVEL.get());
        gold_netherite_weapons.add(ModItems.GOLD_UPGRADED_NETHERITE_AXE.get());
        gold_netherite_weapons.add(ModItems.GOLD_UPGRADED_NETHERITE_SWORD.get());
        gold_netherite_ranged.add(ModItems.GOLD_UPGRADED_NETHERITE_CROSSBOW.get());
        gold_netherite_ranged.add(ModItems.GOLD_UPGRADED_NETHERITE_BOW.get());
        gold_netherite_shield.add(ModItems.GOLD_UPGRADED_NETHERITE_SHIELD.get());
    }

    public static void initFire() {
        fire_netherite_tools.add(ModItems.FIRE_UPGRADED_NETHERITE_AXE.get());
        fire_netherite_tools.add(ModItems.FIRE_UPGRADED_NETHERITE_PICKAXE.get());
        fire_netherite_tools.add(ModItems.FIRE_UPGRADED_NETHERITE_SHOVEL.get());
        fire_netherite_weapons.add(ModItems.FIRE_UPGRADED_NETHERITE_AXE.get());
        fire_netherite_weapons.add(ModItems.FIRE_UPGRADED_NETHERITE_SWORD.get());
        fire_netherite_ranged.add(ModItems.FIRE_UPGRADED_NETHERITE_CROSSBOW.get());
        fire_netherite_ranged.add(ModItems.FIRE_UPGRADED_NETHERITE_BOW.get());
        fire_netherite_shield.add(ModItems.FIRE_UPGRADED_NETHERITE_SHIELD.get());
    }

    public static void initEnder() {
        ender_netherite_tools.add(ModItems.ENDER_UPGRADED_NETHERITE_AXE.get());
        ender_netherite_tools.add(ModItems.ENDER_UPGRADED_NETHERITE_PICKAXE.get());
        ender_netherite_tools.add(ModItems.ENDER_UPGRADED_NETHERITE_SHOVEL.get());
        ender_netherite_weapons.add(ModItems.ENDER_UPGRADED_NETHERITE_AXE.get());
        ender_netherite_weapons.add(ModItems.ENDER_UPGRADED_NETHERITE_SWORD.get());
        ender_netherite_ranged.add(ModItems.ENDER_UPGRADED_NETHERITE_CROSSBOW.get());
        ender_netherite_ranged.add(ModItems.ENDER_UPGRADED_NETHERITE_BOW.get());
        ender_netherite_shield.add(ModItems.ENDER_UPGRADED_NETHERITE_SHIELD.get());
    }

    public static void initWater() {
        water_netherite_tools.add(ModItems.WATER_UPGRADED_NETHERITE_AXE.get());
        water_netherite_tools.add(ModItems.WATER_UPGRADED_NETHERITE_PICKAXE.get());
        water_netherite_tools.add(ModItems.WATER_UPGRADED_NETHERITE_SHOVEL.get());
        water_netherite_weapons.add(ModItems.WATER_UPGRADED_NETHERITE_AXE.get());
        water_netherite_weapons.add(ModItems.WATER_UPGRADED_NETHERITE_SWORD.get());
        water_netherite_ranged.add(ModItems.WATER_UPGRADED_NETHERITE_CROSSBOW.get());
        water_netherite_ranged.add(ModItems.WATER_UPGRADED_NETHERITE_BOW.get());
        water_netherite_shield.add(ModItems.WATER_UPGRADED_NETHERITE_SHIELD.get());
    }

    public static void initWither() {
        wither_netherite_tools.add(ModItems.WITHER_UPGRADED_NETHERITE_AXE.get());
        wither_netherite_weapons.add(ModItems.WITHER_UPGRADED_NETHERITE_AXE.get());
        wither_netherite_weapons.add(ModItems.WITHER_UPGRADED_NETHERITE_SWORD.get());
        wither_netherite_ranged.add(ModItems.WITHER_UPGRADED_NETHERITE_CROSSBOW.get());
        wither_netherite_ranged.add(ModItems.WITHER_UPGRADED_NETHERITE_BOW.get());
        wither_netherite_shield.add(ModItems.WITHER_UPGRADED_NETHERITE_SHIELD.get());
    }

    public static void initPoison() {
        poison_netherite_tools.add(ModItems.POISON_UPGRADED_NETHERITE_AXE.get());
        poison_netherite_weapons.add(ModItems.POISON_UPGRADED_NETHERITE_AXE.get());
        poison_netherite_weapons.add(ModItems.POISON_UPGRADED_NETHERITE_SWORD.get());
        poison_netherite_ranged.add(ModItems.POISON_UPGRADED_NETHERITE_CROSSBOW.get());
        poison_netherite_ranged.add(ModItems.POISON_UPGRADED_NETHERITE_BOW.get());
        poison_netherite_shield.add(ModItems.POISON_UPGRADED_NETHERITE_SHIELD.get());
    }

    public static void initPhantom() {
        phantom_netherite_tools.add(ModItems.PHANTOM_UPGRADED_NETHERITE_AXE.get());
        phantom_netherite_tools.add(ModItems.PHANTOM_UPGRADED_NETHERITE_PICKAXE.get());
        phantom_netherite_tools.add(ModItems.PHANTOM_UPGRADED_NETHERITE_SHOVEL.get());
        phantom_netherite_weapons.add(ModItems.PHANTOM_UPGRADED_NETHERITE_AXE.get());
        phantom_netherite_weapons.add(ModItems.PHANTOM_UPGRADED_NETHERITE_SWORD.get());
        phantom_netherite_ranged.add(ModItems.PHANTOM_UPGRADED_NETHERITE_CROSSBOW.get());
        phantom_netherite_ranged.add(ModItems.PHANTOM_UPGRADED_NETHERITE_BOW.get());
        phantom_netherite_shield.add(ModItems.PHANTOM_UPGRADED_NETHERITE_SHIELD.get());
    }

    public static void initFeather() {
        feather_netherite_tools.add(ModItems.FEATHER_UPGRADED_NETHERITE_AXE.get());
        feather_netherite_tools.add(ModItems.FEATHER_UPGRADED_NETHERITE_PICKAXE.get());
        feather_netherite_tools.add(ModItems.FEATHER_UPGRADED_NETHERITE_SHOVEL.get());
        feather_netherite_weapons.add(ModItems.FEATHER_UPGRADED_NETHERITE_AXE.get());
        feather_netherite_weapons.add(ModItems.FEATHER_UPGRADED_NETHERITE_SWORD.get());
        feather_netherite_ranged.add(ModItems.FEATHER_UPGRADED_NETHERITE_CROSSBOW.get());
        feather_netherite_ranged.add(ModItems.FEATHER_UPGRADED_NETHERITE_BOW.get());
        feather_netherite_shield.add(ModItems.FEATHER_UPGRADED_NETHERITE_SHIELD.get());
    }

    public static void initCorrupt() {
        corrupt_netherite_tools.add(ModItems.CORRUPT_UPGRADED_NETHERITE_AXE.get());
        corrupt_netherite_tools.add(ModItems.CORRUPT_UPGRADED_NETHERITE_PICKAXE.get());
        corrupt_netherite_tools.add(ModItems.CORRUPT_UPGRADED_NETHERITE_SHOVEL.get());
        corrupt_netherite_weapons.add(ModItems.CORRUPT_UPGRADED_NETHERITE_AXE.get());
        corrupt_netherite_weapons.add(ModItems.CORRUPT_UPGRADED_NETHERITE_SWORD.get());
        corrupt_netherite_ranged.add(ModItems.CORRUPT_UPGRADED_NETHERITE_CROSSBOW.get());
        corrupt_netherite_ranged.add(ModItems.CORRUPT_UPGRADED_NETHERITE_BOW.get());
        corrupt_netherite_shield.add(ModItems.CORRUPT_UPGRADED_NETHERITE_SHIELD.get());
    }

    public static void initUltimate() {
        ModList list = ModList.get();
        if (list != null) {
            if (ModList.get().getModContainerById("upgradednetherite_ultimate").isPresent()) {
                ultimate_netherite_tools.add(com.rolfmao.upgradednetherite_ultimate.init.ModItems.ULTIMATE_UPGRADED_NETHERITE_AXE.get());
                ultimate_netherite_tools.add(com.rolfmao.upgradednetherite_ultimate.init.ModItems.ULTIMATE_UPGRADED_NETHERITE_PICKAXE.get());
                ultimate_netherite_tools.add(com.rolfmao.upgradednetherite_ultimate.init.ModItems.ULTIMATE_UPGRADED_NETHERITE_SHOVEL.get());
                ultimate_netherite_weapons.add(com.rolfmao.upgradednetherite_ultimate.init.ModItems.ULTIMATE_UPGRADED_NETHERITE_AXE.get());
                ultimate_netherite_weapons.add(com.rolfmao.upgradednetherite_ultimate.init.ModItems.ULTIMATE_UPGRADED_NETHERITE_SWORD.get());
                ultimate_netherite_ranged.add(com.rolfmao.upgradednetherite_ultimate.init.ModItems.ULTIMATE_UPGRADED_NETHERITE_CROSSBOW.get());
                ultimate_netherite_ranged.add(com.rolfmao.upgradednetherite_ultimate.init.ModItems.ULTIMATE_UPGRADED_NETHERITE_BOW.get());
                ultimate_netherite_shield.add(com.rolfmao.upgradednetherite_ultimate.init.ModItems.ULTIMATE_UPGRADED_NETHERITE_SHIELD.get());
            }
        }
    }


}

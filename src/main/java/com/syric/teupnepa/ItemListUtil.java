package com.syric.teupnepa;

import com.rolfmao.upgradednetherite.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;

public class ItemListUtil {

    public static final ArrayList<Item> fire_netherite_tools = new ArrayList<>();
    public static final ArrayList<Item> fire_netherite_weapons = new ArrayList<>();
    public static final ArrayList<Item> fire_netherite_ranged = new ArrayList<>();
    public static final ArrayList<Item> fire_netherite_shield = new ArrayList<>();

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

}

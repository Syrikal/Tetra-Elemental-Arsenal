package com.syric.teupnepa.registry;

import com.syric.teupnepa.TeUpNePa;
import net.minecraft.world.item.*;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(
        modid = "aetheric_tetranomicon",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class TUNPItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TeUpNePa.MODID);
    public static final RegistryObject<Item> CORRUPT_UPGRADED_NETHERITE_INGOT = ITEMS.register("corrupt_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENDER_UPGRADED_NETHERITE_INGOT = ITEMS.register("ender_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FEATHER_UPGRADED_NETHERITE_INGOT = ITEMS.register("feather_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRE_UPGRADED_NETHERITE_INGOT = ITEMS.register("fire_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_UPGRADED_NETHERITE_INGOT = ITEMS.register("gold_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PHANTOM_UPGRADED_NETHERITE_INGOT = ITEMS.register("phantom_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POISON_UPGRADED_NETHERITE_INGOT = ITEMS.register("poison_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ULTIMATE_UPGRADED_NETHERITE_INGOT = ITEMS.register("ultimate_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATER_UPGRADED_NETHERITE_INGOT = ITEMS.register("water_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WITHER_UPGRADED_NETHERITE_INGOT = ITEMS.register("wither_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));

    public TUNPItems() {
    }

    @SubscribeEvent
    public static void buildCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab.getIconItem().is(Items.DIAMOND)) {
            event.getEntries().putAfter(new ItemStack(Items.DIAMOND), new ItemStack(CORRUPT_UPGRADED_NETHERITE_INGOT.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

}
package com.syric.elementalarsenal.registry;

import com.syric.elementalarsenal.ElementalArsenal;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class EAItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ElementalArsenal.MODID);
    public static final RegistryObject<Item> CORRUPT_IMBUED_NETHERITE_INGOT = ITEMS.register("corrupt_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ECHO_IMBUED_NETHERITE_INGOT = ITEMS.register("echo_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENDER_IMBUED_NETHERITE_INGOT = ITEMS.register("ender_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FEATHER_IMBUED_NETHERITE_INGOT = ITEMS.register("feather_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRE_IMBUED_NETHERITE_INGOT = ITEMS.register("fire_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FROST_IMBUED_NETHERITE_INGOT = ITEMS.register("frost_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIGHTNING_IMBUED_NETHERITE_INGOT = ITEMS.register("lightning_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_IMBUED_NETHERITE_INGOT = ITEMS.register("gold_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PHANTOM_IMBUED_NETHERITE_INGOT = ITEMS.register("phantom_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POISON_IMBUED_NETHERITE_INGOT = ITEMS.register("poison_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RADIANT_IMBUED_NETHERITE_INGOT = ITEMS.register("radiant_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATER_IMBUED_NETHERITE_INGOT = ITEMS.register("water_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WITHER_IMBUED_NETHERITE_INGOT = ITEMS.register("wither_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ULTIMATE_IMBUED_NETHERITE_INGOT = ITEMS.register("ultimate_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AETHERIC_IMBUED_NETHERITE_INGOT = ITEMS.register("aetheric_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FORGOTTEN_IMBUED_NETHERITE_INGOT = ITEMS.register("forgotten_imbued_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ARCANE_IMBUED_NETHERITE_INGOT = ITEMS.register("arcane_imbued_netherite_ingot", () -> new Item(new Item.Properties()));

    @SubscribeEvent
    public static void buildCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().getNamespace().equals("tetra") && event.getTabKey().location().getPath().equals("default")) {
            event.accept(ECHO_IMBUED_NETHERITE_INGOT);
            event.accept(ENDER_IMBUED_NETHERITE_INGOT);
            event.accept(FEATHER_IMBUED_NETHERITE_INGOT);
            event.accept(FIRE_IMBUED_NETHERITE_INGOT);
            event.accept(FROST_IMBUED_NETHERITE_INGOT);
            event.accept(LIGHTNING_IMBUED_NETHERITE_INGOT);
            event.accept(GOLD_IMBUED_NETHERITE_INGOT);
            event.accept(PHANTOM_IMBUED_NETHERITE_INGOT);
            event.accept(POISON_IMBUED_NETHERITE_INGOT);
            event.accept(RADIANT_IMBUED_NETHERITE_INGOT);
            event.accept(WATER_IMBUED_NETHERITE_INGOT);
            event.accept(WITHER_IMBUED_NETHERITE_INGOT);
            event.accept(AETHERIC_IMBUED_NETHERITE_INGOT);
            event.accept(FORGOTTEN_IMBUED_NETHERITE_INGOT);
            event.accept(ARCANE_IMBUED_NETHERITE_INGOT);
        }
    }

}
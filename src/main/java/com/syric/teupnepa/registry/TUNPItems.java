package com.syric.teupnepa.registry;

import com.syric.teupnepa.TeUpNePa;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(
        modid = TeUpNePa.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class TUNPItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TeUpNePa.MODID);
    public static final RegistryObject<Item> CORRUPT_UPGRADED_NETHERITE_INGOT = ITEMS.register("corrupt_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ECHO_UPGRADED_NETHERITE_INGOT = ITEMS.register("echo_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENDER_UPGRADED_NETHERITE_INGOT = ITEMS.register("ender_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FEATHER_UPGRADED_NETHERITE_INGOT = ITEMS.register("feather_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRE_UPGRADED_NETHERITE_INGOT = ITEMS.register("fire_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_UPGRADED_NETHERITE_INGOT = ITEMS.register("gold_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PHANTOM_UPGRADED_NETHERITE_INGOT = ITEMS.register("phantom_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POISON_UPGRADED_NETHERITE_INGOT = ITEMS.register("poison_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RADIANT_UPGRADED_NETHERITE_INGOT = ITEMS.register("radiant_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATER_UPGRADED_NETHERITE_INGOT = ITEMS.register("water_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WITHER_UPGRADED_NETHERITE_INGOT = ITEMS.register("wither_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ULTIMATE_UPGRADED_NETHERITE_INGOT = ITEMS.register("ultimate_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AETHERIC_UPGRADED_NETHERITE_INGOT = ITEMS.register("aetheric_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FORGOTTEN_UPGRADED_NETHERITE_INGOT = ITEMS.register("forgotten_upgraded_netherite_ingot", () -> new Item(new Item.Properties()));

    public TUNPItems() {
    }

    @SubscribeEvent
    public static void buildCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(CORRUPT_UPGRADED_NETHERITE_INGOT);
            event.accept(ECHO_UPGRADED_NETHERITE_INGOT);
            event.accept(ENDER_UPGRADED_NETHERITE_INGOT);
            event.accept(FEATHER_UPGRADED_NETHERITE_INGOT);
            event.accept(FIRE_UPGRADED_NETHERITE_INGOT);
            event.accept(GOLD_UPGRADED_NETHERITE_INGOT);
            event.accept(PHANTOM_UPGRADED_NETHERITE_INGOT);
            event.accept(POISON_UPGRADED_NETHERITE_INGOT);
            event.accept(RADIANT_UPGRADED_NETHERITE_INGOT);
            event.accept(WATER_UPGRADED_NETHERITE_INGOT);
            event.accept(WITHER_UPGRADED_NETHERITE_INGOT);
            event.accept(ULTIMATE_UPGRADED_NETHERITE_INGOT);
            event.accept(AETHERIC_UPGRADED_NETHERITE_INGOT);
            event.accept(FORGOTTEN_UPGRADED_NETHERITE_INGOT);
        }
    }

}
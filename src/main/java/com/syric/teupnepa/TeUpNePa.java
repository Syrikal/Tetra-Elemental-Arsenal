package com.syric.teupnepa;

import com.syric.teupnepa.events.AddArrowTags;
import com.syric.teupnepa.registry.TUNPItems;
import com.syric.teupnepa.registry.TUNPLootModifiers;
import com.syric.teupnepa.registry.TUNPMobEffects;
import com.syric.teupnepa.registry.TUNPSounds;
import com.syric.teupnepa.tetra_effects.Effects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TeUpNePa.MODID)
public class TeUpNePa {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "teupnepa";
//    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public TeUpNePa(FMLJavaModLoadingContext context) {
        // Register the setup method for modloading
        context.getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        context.getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Effects());
//        MinecraftForge.EVENT_BUS.register(new SetEnderTags());

        MinecraftForge.EVENT_BUS.register(new AddArrowTags());

        IEventBus modEventBus = context.getModEventBus();
        // Register the Deferred Register to the mod event bus so items get registered
        TUNPItems.ITEMS.register(modEventBus);
        TUNPMobEffects.MOB_EFFECTS.register(modEventBus);
        TUNPSounds.SOUND_EVENTS.register(modEventBus);
        TUNPLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

}

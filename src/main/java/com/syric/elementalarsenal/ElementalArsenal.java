package com.syric.elementalarsenal;

import com.syric.elementalarsenal.events.AddArrowTags;
import com.syric.elementalarsenal.compat.IFCompat;
import com.syric.elementalarsenal.registry.EAItems;
import com.syric.elementalarsenal.registry.EALootModifiers;
import com.syric.elementalarsenal.registry.EAMobEffects;
import com.syric.elementalarsenal.registry.EASounds;
import com.syric.elementalarsenal.tetra_effects.Effects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ElementalArsenal.MODID)
public class ElementalArsenal {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "elementalarsenal";
//    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public ElementalArsenal(FMLJavaModLoadingContext context) {
        // Register the doClientStuff method for modloading
        context.getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Effects());

        MinecraftForge.EVENT_BUS.register(new AddArrowTags());

        IEventBus modEventBus = context.getModEventBus();
        // Register the Deferred Register to the mod event bus so items get registered
        EAItems.ITEMS.register(modEventBus);
        EAMobEffects.MOB_EFFECTS.register(modEventBus);
        EASounds.SOUND_EVENTS.register(modEventBus);
        EALootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        if (ModList.get().isLoaded("iceandfire") && !ModList.get().isLoaded("amm")) {
            IFCompat.addBars(event);
        }

    }

}

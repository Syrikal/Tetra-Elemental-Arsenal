package com.syric.teupnepa;

import com.syric.teupnepa.effects.Effects;
import com.syric.teupnepa.events.AddTooltips;
import com.syric.teupnepa.events.SetEnderTags;
import com.syric.teupnepa.predicates.NetheriteUpgradedPredicate;
import com.syric.teupnepa.registry.TUNPItems;
import com.syric.teupnepa.util.ArrowUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.items.modular.ItemPredicateModular;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TeUpNePa.MODID)
public class TeUpNePa {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "teupnepa";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public TeUpNePa() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Effects());
        MinecraftForge.EVENT_BUS.register(new SetEnderTags());
        MinecraftForge.EVENT_BUS.register(new ArrowUtil());

        ItemPredicateModular.register(new ResourceLocation("tetra:upgraded_netherite"), NetheriteUpgradedPredicate::new);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the Deferred Register to the mod event bus so items get registered
        TUNPItems.ITEMS.register(modEventBus);
//        modEventBus.addListener(this::addBars);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new AddTooltips());
    }

}

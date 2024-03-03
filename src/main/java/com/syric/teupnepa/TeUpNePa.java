package com.syric.teupnepa;

import com.syric.teupnepa.effects.Effects;
import com.syric.teupnepa.events.AddTooltips;
import com.syric.teupnepa.events.SetEnderTags;
import com.syric.teupnepa.predicates.NetheriteUpgradedPredicate;
import com.syric.teupnepa.util.ArrowUtil;
import com.syric.teupnepa.util.ItemListUtil;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("teupnepa")
public class TeUpNePa
{
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

        ItemPredicate.register(new ResourceLocation("tetra:upgraded_netherite"), NetheriteUpgradedPredicate::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ItemListUtil.initGold();
        ItemListUtil.initFire();
        ItemListUtil.initEnder();
        ItemListUtil.initWater();
        ItemListUtil.initWither();
        ItemListUtil.initPoison();
        ItemListUtil.initPhantom();
        ItemListUtil.initFeather();
        ItemListUtil.initCorrupt();
        ItemListUtil.initUltimate();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new AddTooltips());
    }

}

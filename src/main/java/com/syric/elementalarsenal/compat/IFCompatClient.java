package com.syric.elementalarsenal.compat;

import com.github.alexthe666.iceandfire.entity.DragonType;
import com.syric.elementalarsenal.tetra_effects.DragonProtectionStatGetter;
import com.syric.elementalarsenal.tetra_effects.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterPercentage;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

@OnlyIn(Dist.CLIENT)
public class IFCompatClient {

    public static void addBars(FMLClientSetupEvent event) {
        handleBars(DragonType.FIRE, "fire", Effects.FIRE_DRAGON_PROTECTION);
        handleBars(DragonType.ICE, "ice", Effects.ICE_DRAGON_PROTECTION);
        handleBars(DragonType.LIGHTNING, "lightning", Effects.LIGHTNING_DRAGON_PROTECTION);
    }

    private static void handleBars(DragonType type, String type_name, ItemEffect effect) {
        IStatGetter statGetter = new DragonProtectionStatGetter(type, effect);
        GuiStatBar protectionBar = new GuiStatBar(0, 0, 59,
                "tetra.stats." + type_name + "_dragon_protection",
                0.0, 100.0, false, statGetter,
                LabelGetterBasic.noLabel,
                new TooltipGetterPercentage("tetra.stats." + type_name + "_dragon_protection.tooltip", statGetter));

        WorkbenchStatsGui.addBar(protectionBar);
        HoloStatsGui.addBar(protectionBar);
    }

}

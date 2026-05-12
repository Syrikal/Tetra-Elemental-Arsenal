package com.syric.elementalarsenal.compat;

import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.tetra_effects.DragonProtectionStatGetter;
import com.syric.elementalarsenal.tetra_effects.Effects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterPercentage;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

public class IFCompat {

    //Reduces dragon damage
    public static void dragonscaleShieldCheck(LivingHurtEvent event) {
        if ((event.getSource().is(IafDamageRegistry.DRAGON_FIRE_TYPE)
                || event.getSource().is(IafDamageRegistry.DRAGON_ICE_TYPE)
                || event.getSource().is(IafDamageRegistry.DRAGON_LIGHTNING_TYPE))
                && event.getEntity() instanceof Player player) {
            double dragonscale_protection = 0;

            if (event.getEntity().getMainHandItem().getItem() instanceof ModularShieldItem) {
                dragonscale_protection = getProtectionLevel(player, event.getEntity().getMainHandItem(), event.getSource());
            }
            if (event.getEntity().getOffhandItem().getItem() instanceof ModularShieldItem) {
                dragonscale_protection = Math.max(dragonscale_protection, getProtectionLevel(player, event.getEntity().getOffhandItem(), event.getSource()));
            }

            if (dragonscale_protection > 0) {
                ElementalArsenal.LOGGER.debug("Dragonscale protection reduced damage from {} to {}", event.getAmount(), (event.getAmount() * (1 - dragonscale_protection)));
                event.setAmount((float) (event.getAmount() * (1 - dragonscale_protection)));
            }
        }
    }

    //Gets the appropriate level of protection.
    private static double getProtectionLevel(Player player, ItemStack stack, DamageSource source) {
        DragonProtectionStatGetter dragonProtectionStatGetter = null;
        if (stack.getItem() instanceof ModularShieldItem) {
            if (source.is(IafDamageRegistry.DRAGON_FIRE_TYPE)) {
                dragonProtectionStatGetter = new DragonProtectionStatGetter(DragonType.FIRE, Effects.FIRE_DRAGON_PROTECTION);
            } else if (source.is(IafDamageRegistry.DRAGON_ICE_TYPE)) {
                dragonProtectionStatGetter = new DragonProtectionStatGetter(DragonType.ICE, Effects.ICE_DRAGON_PROTECTION);
            } else if (source.is(IafDamageRegistry.DRAGON_LIGHTNING_TYPE)) {
                dragonProtectionStatGetter = new DragonProtectionStatGetter(DragonType.LIGHTNING, Effects.LIGHTNING_DRAGON_PROTECTION);
            }
        }
        if (dragonProtectionStatGetter == null) {
            return 0;
        } else {
            return Math.min(0.3, 0.01 * dragonProtectionStatGetter.getValue(player, stack));
        }
    }


    @OnlyIn(Dist.CLIENT)
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

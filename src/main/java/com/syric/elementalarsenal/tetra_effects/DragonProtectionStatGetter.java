package com.syric.elementalarsenal.tetra_effects;

import com.github.alexthe666.iceandfire.entity.DragonType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;

public class DragonProtectionStatGetter implements IStatGetter {
    protected final DragonType type;
    protected final ItemEffect protectionEffect;

    public DragonProtectionStatGetter(DragonType type, ItemEffect protectionEffect) {
        this.type = type;
        this.protectionEffect = protectionEffect;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return CastOptional.cast(itemStack.getItem(), IModularItem.class)
                .map((item) -> (double)item.getEffectEfficiency(itemStack, this.protectionEffect))
                .orElse((double)0.0F);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return CastOptional.cast(itemStack.getItem(), IModularItem.class)
                .map((item) -> item.getModuleFromSlot(itemStack, slot))
                .map((module) -> (double)module.getEffectEfficiency(itemStack, this.protectionEffect))
                .orElse((double)0.0F);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return CastOptional.cast(itemStack.getItem(), IModularItem.class)
                .flatMap((item) -> CastOptional.cast(item.getModuleFromSlot(itemStack, slot), ItemModuleMajor.class))
                .map((module) -> module.getImprovement(itemStack, improvement))
                .map((improvementData) -> improvementData.effects)
                .map((effects) -> (double)effects.getEfficiency(this.protectionEffect))
                .orElse((double)0.0F);
    }

}

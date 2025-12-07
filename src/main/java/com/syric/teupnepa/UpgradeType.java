package com.syric.teupnepa;

import com.syric.teupnepa.effects.Effects;
import se.mickelus.tetra.effect.ItemEffect;

public enum UpgradeType {
    CORRUPT(Effects.corrupt, Effects.corrupt_tool, Effects.corrupt_weapon, Effects.corrupt_both),
    ENDER(Effects.ender, Effects.ender_tool, Effects.ender_weapon, Effects.ender_both),
    FEATHER(Effects.feather, Effects.feather_tool, Effects.feather_weapon, Effects.feather_both),
    FIRE(Effects.fire, Effects.fire_tool, Effects.fire_weapon, Effects.fire_both),
    GOLD(Effects.gold, Effects.gold_tool, Effects.gold_weapon, Effects.gold_both),
    PHANTOM(Effects.phantom, Effects.phantom_tool, Effects.phantom_weapon, Effects.phantom_both),
    POISON(Effects.poison, Effects.poison_tool, Effects.poison_weapon, Effects.poison_both),
    ULTIMATE(Effects.ultimate, Effects.ultimate_tool, Effects.ultimate_weapon, Effects.ultimate_both),
    WATER(Effects.water, Effects.water_tool, Effects.water_weapon, Effects.water_both),
    WITHER(Effects.wither, Effects.wither_tool, Effects.wither_weapon, Effects.wither_both);

    public final ItemEffect base;
    public final ItemEffect tool;
    public final ItemEffect weapon;
    public final ItemEffect both;
    UpgradeType(ItemEffect base, ItemEffect tool, ItemEffect weapon, ItemEffect both) {
        this.base = base;
        this.tool = tool;
        this.weapon = weapon;
        this.both = both;
    }
}

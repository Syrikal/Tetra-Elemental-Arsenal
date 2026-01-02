package com.syric.teupnepa.enums;

import com.syric.teupnepa.effects.Effects;
import se.mickelus.tetra.effect.ItemEffect;

public enum UpgradeType {
    CORRUPT("corrupt", Effects.corrupt, Effects.corrupt_tool, Effects.corrupt_weapon, Effects.corrupt_both),
    ECHO("echo", Effects.echo, Effects.echo_tool, Effects.echo_weapon, Effects.echo_both),
    ENDER("ender", Effects.ender, Effects.ender_tool, Effects.ender_weapon, Effects.ender_both),
    FEATHER("feather", Effects.feather, Effects.feather_tool, Effects.feather_weapon, Effects.feather_both),
    FIRE("fire", Effects.fire, Effects.fire_tool, Effects.fire_weapon, Effects.fire_both),
    GOLD("gold", Effects.gold, Effects.gold_tool, Effects.gold_weapon, Effects.gold_both),
    PHANTOM("phantom", Effects.phantom, Effects.phantom_tool, Effects.phantom_weapon, Effects.phantom_both),
    POISON("poison", Effects.poison, Effects.poison_tool, Effects.poison_weapon, Effects.poison_both),
    RADIANT("radiant", Effects.radiant, Effects.radiant_tool, Effects.radiant_weapon, Effects.radiant_both),
    WATER("water", Effects.water, Effects.water_tool, Effects.water_weapon, Effects.water_both),
    WITHER("wither", Effects.wither, Effects.wither_tool, Effects.wither_weapon, Effects.wither_both),
    ULTIMATE("ultimate", Effects.ultimate, Effects.ultimate_tool, Effects.ultimate_weapon, Effects.ultimate_both),
    AETHERIC("aetheric", Effects.aetheric, Effects.aetheric_tool, Effects.aetheric_weapon, Effects.aetheric_both),
    FORGOTTEN("forgotten", Effects.forgotten, Effects.forgotten_tool, Effects.forgotten_weapon, Effects.forgotten_both);

    public final String name;
    public final ItemEffect base;
    public final ItemEffect tool;
    public final ItemEffect weapon;
    public final ItemEffect both;
    UpgradeType(String name, ItemEffect base, ItemEffect tool, ItemEffect weapon, ItemEffect both) {
        this.name = name;
        this.base = base;
        this.tool = tool;
        this.weapon = weapon;
        this.both = both;
    }
}

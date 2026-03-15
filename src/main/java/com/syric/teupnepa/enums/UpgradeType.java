package com.syric.teupnepa.enums;

import com.syric.teupnepa.tetra_effects.Effects;
import se.mickelus.tetra.effect.ItemEffect;

public enum UpgradeType {
    CORRUPT("corrupt", Effects.corrupt, Effects.corrupt_tool, Effects.corrupt_weapon, Effects.corrupt_both, "blood"),
    ECHO("echo", Effects.echo, Effects.echo_tool, Effects.echo_weapon, Effects.echo_both, "eldritch"),
    ENDER("ender", Effects.ender, Effects.ender_tool, Effects.ender_weapon, Effects.ender_both, "ender"),
    FEATHER("feather", Effects.feather, Effects.feather_tool, Effects.feather_weapon, Effects.feather_both, "evocation"),
    FIRE("fire", Effects.fire, Effects.fire_tool, Effects.fire_weapon, Effects.fire_both, "fire"),
    FROST("frost", Effects.frost, Effects.frost_tool, Effects.frost_weapon, Effects.frost_both, "ice"),
    LIGHTNING("lightning", Effects.lightning, Effects.lightning_tool, Effects.lightning_weapon, Effects.lightning_both, "lightning"),
    GOLD("gold", Effects.gold, Effects.gold_tool, Effects.gold_weapon, Effects.gold_both, "evocation"),
    PHANTOM("phantom", Effects.phantom, Effects.phantom_tool, Effects.phantom_weapon, Effects.phantom_both, "evocation"),
    POISON("poison", Effects.poison, Effects.poison_tool, Effects.poison_weapon, Effects.poison_both, "nature"),
    RADIANT("radiant", Effects.radiant, Effects.radiant_tool, Effects.radiant_weapon, Effects.radiant_both, "holy"),
    WATER("water", Effects.water, Effects.water_tool, Effects.water_weapon, Effects.water_both, "lightning"),
    WITHER("wither", Effects.wither, Effects.wither_tool, Effects.wither_weapon, Effects.wither_both, "blood"),
    ULTIMATE("ultimate", Effects.ultimate, Effects.ultimate_tool, Effects.ultimate_weapon, Effects.ultimate_both, ""),
    AETHERIC("aetheric", Effects.aetheric, Effects.aetheric_tool, Effects.aetheric_weapon, Effects.aetheric_both, "lightning"),
    FORGOTTEN("forgotten", Effects.forgotten, Effects.forgotten_tool, Effects.forgotten_weapon, Effects.forgotten_both, "nature"),
    ARCANE("arcane", Effects.arcane, Effects.arcane_tool, Effects.arcane_weapon, Effects.arcane_both, "arcane");

    public final String name;
    public final ItemEffect base;
    public final ItemEffect tool;
    public final ItemEffect weapon;
    public final ItemEffect both;
    public final String spell_school;
    UpgradeType(String name, ItemEffect base, ItemEffect tool, ItemEffect weapon, ItemEffect both, String spell_school) {
        this.name = name;
        this.base = base;
        this.tool = tool;
        this.weapon = weapon;
        this.both = both;
        this.spell_school = spell_school;
    }
}

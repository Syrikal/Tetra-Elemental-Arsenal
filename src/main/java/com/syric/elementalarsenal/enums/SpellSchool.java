package com.syric.elementalarsenal.enums;

public enum SpellSchool {
    BLOOD("blood"),
    ELDRITCH("eldritch"),
    ENDER("ender"),
    EVOCATION("evocation"),
    FIRE("fire"),
    HOLY("holy"),
    ICE("ice"),
    LIGHTNING("lightning"),
    NATURE("nature"),
    ARCANE("arcane");

    public final String name;
    SpellSchool(String name) {
        this.name = name;
    }
}

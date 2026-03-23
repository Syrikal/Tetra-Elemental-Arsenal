package com.syric.elementalarsenal.enums;

import java.util.ArrayList;
import java.util.List;

public enum ModularItemCategory {
    BOW("bow"),
    CROSSBOW("crossbow"),
    DOUBLE("double"),
    SHIELD("shield"),
    SINGLE("single"),
    SWORD("sword");

    public final String id;
    ModularItemCategory(String name) {
        this.id = name;
    }

    public List<ModuleType> getModules() {
        List<ModuleType> output = new ArrayList<>();
        for (ModuleType type : ModuleType.values()) {
            if (type.category == this) {
                output.add(type);
            }
        }
        return output;
    }
}

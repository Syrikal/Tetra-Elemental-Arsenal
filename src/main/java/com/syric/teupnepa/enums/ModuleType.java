package com.syric.teupnepa.enums;

public enum ModuleType {
    ADZE("adze", ModularItemCategory.DOUBLE, UpgradeCategory.TOOL),
    BASIC_AXE("basic_axe", ModularItemCategory.DOUBLE, UpgradeCategory.BOTH),
    BASIC_BLADE("basic_blade", ModularItemCategory.SWORD, UpgradeCategory.WEAPON),
    BASIC_HAMMER("basic_hammer", ModularItemCategory.DOUBLE, UpgradeCategory.TOOL),
    BASIC_PICKAXE("basic_pickaxe", ModularItemCategory.DOUBLE, UpgradeCategory.TOOL),
    BASIC_SHOVEL("basic_shovel", ModularItemCategory.SINGLE, UpgradeCategory.TOOL),
    BASIC_STOCK("basic_stock", ModularItemCategory.CROSSBOW, UpgradeCategory.BOW),
    BUCKLER("buckler", ModularItemCategory.SHIELD, UpgradeCategory.SHIELD),
    BUTT("butt", ModularItemCategory.DOUBLE, UpgradeCategory.NONE),
    CLAW("claw", ModularItemCategory.DOUBLE, UpgradeCategory.TOOL),
    HEATER("heater", ModularItemCategory.SHIELD, UpgradeCategory.SHIELD),
    HEAVY_BLADE("heavy_blade", ModularItemCategory.SWORD, UpgradeCategory.WEAPON),
    HOE("hoe", ModularItemCategory.DOUBLE, UpgradeCategory.TOOL),
    LONG_STAVE("long_stave", ModularItemCategory.BOW, UpgradeCategory.BOW),
    MACHETE("machete", ModularItemCategory.SWORD, UpgradeCategory.TOOL),
    RECURVE_STAVE("recurve_stave", ModularItemCategory.BOW, UpgradeCategory.BOW),
    SHORT_BLADE("short_blade", ModularItemCategory.SWORD, UpgradeCategory.WEAPON),
    SICKLE("sickle", ModularItemCategory.DOUBLE, UpgradeCategory.TOOL),
    SPEARHEAD("spearhead", ModularItemCategory.SINGLE, UpgradeCategory.WEAPON),
    STRAIGHT_STAVE("straight_stave", ModularItemCategory.BOW, UpgradeCategory.BOW),
    THROWING_KNIFE("throwing_knife", ModularItemCategory.SWORD, UpgradeCategory.WEAPON),
    TOWER("tower", ModularItemCategory.SHIELD, UpgradeCategory.SHIELD);

    public final String id;
    public final ModularItemCategory category;
    public final UpgradeCategory upgradeCategory;
    ModuleType(String id, ModularItemCategory category, UpgradeCategory upgradeCategory) {
        this.id = id;
        this.category = category;
        this.upgradeCategory = upgradeCategory;
    }

}

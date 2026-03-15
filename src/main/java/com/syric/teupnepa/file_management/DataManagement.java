package com.syric.teupnepa.file_management;

import com.syric.teupnepa.enums.*;
import net.minecraftforge.fml.common.Mod;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.nio.file.Path;

public class DataManagement {

    public static void main(String[] args) {

        for (ModuleType type : ModuleType.values()) {
            writeModulesFile(type);
        }

//        writeAestheticImprovementFile(ModuleType.ADZE, UpgradeType.CORRUPT);
        writeAllImprovementFiles();

        writeAllSchematics();

        printAllImprovements();

    }

    private static void printAllImprovements() {
        for (UpgradeType upgradeType : UpgradeType.values()) {

            String[] suffixes = new String[] {
                    "both",
                    "tool",
                    "weapon",
                    "bow",
                    "shield",
                    "aesthetic"
            };

            for (String suffix : suffixes) {
                System.out.println("\"upgradednetherite/" + upgradeType.name + "_" + suffix + "\",");
            }
        }
    }

    private static Path modules = Path.of("src\\generated\\resources\\data\\tetra\\modules");

    private static void writeModulesFile(ModuleType moduleType) {
        Path filepath = Path.of(modules.toString(), "\\", moduleType.category.id, "\\", moduleType.id + ".json");

        if (!filepath.getParent().toFile().exists()) {
            filepath.getParent().toFile().mkdirs();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("replace", false);
        ArrayNode improvementsArray = rootNode.putArray("improvements");

        for (UpgradeType type : UpgradeType.values()) {
            if (moduleType != ModuleType.BUTT) {
                improvementsArray.add("tetra:upgradednetherite/" + type.name + "/" + moduleType.id);
            }
            improvementsArray.add("tetra:upgradednetherite/" + type.name + "/" + moduleType.id + "_aesthetic");
//            if (moduleType.category == ModularItemCategory.DOUBLE) {
//                improvementsArray.add("tetra:upgradednetherite/" + type.name + "/" + moduleType.id + "_aesthetic");
//            }
        }
        for (SpellSchool school : SpellSchool.values()) {
            if (moduleType != ModuleType.BUTT && moduleType.category != ModularItemCategory.SHIELD) {
                improvementsArray.add("tetra:upgradednetherite/irons_spells/" + school.name + "_spell_power");
            } else if (moduleType.category == ModularItemCategory.SHIELD) {
                improvementsArray.add("tetra:upgradednetherite/irons_spells/" + school.name + "_magic_shield");
            }
        }
        File file = filepath.toFile();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        mapper.writer().with(prettyPrinter).writeValue(file, rootNode);
    }

    private static Path improvements = Path.of("src\\generated\\resources\\data\\tetra\\improvements\\upgradednetherite");

    private static void writeAllImprovementFiles() {
        for (UpgradeType upgradeType : UpgradeType.values()) {
            for (ModuleType moduleType : ModuleType.values()) {
                if (moduleType != ModuleType.BUTT) {
                    writeImprovementFile(moduleType, upgradeType);
                }
                writeAestheticImprovementFile(moduleType, upgradeType);
//                if (moduleType.category == ModularItemCategory.DOUBLE) {
//                    writeAestheticImprovementFile(moduleType, upgradeType);
//                }
            }
        }
    }

    private static void writeImprovementFile(ModuleType moduleType, UpgradeType upgradeType) {
        Path filepath = Path.of(improvements.toString(), "\\", upgradeType.name, "\\", moduleType.id + ".json");

        if (!filepath.getParent().toFile().exists()) {
            filepath.getParent().toFile().mkdirs();
        }

        if ((upgradeType == UpgradeType.POISON || upgradeType == UpgradeType.WITHER) && moduleType.upgradeCategory == UpgradeCategory.TOOL) {
            return;
        }

        String key_suffix = switch (moduleType.upgradeCategory) {
            case BOTH -> "_both";
            case TOOL -> "_tool";
            case WEAPON -> "_weapon";
            case BOW -> "_bow";
            case SHIELD -> "_shield";
            case NONE -> "";
        };

        String effect_suffix = switch (moduleType.upgradeCategory) {
            case BOTH -> "_both";
            case TOOL -> "_tool";
            case WEAPON -> "_weapon";
            default -> "";
        };

        if (moduleType == ModuleType.BASIC_HAMMER && upgradeType == UpgradeType.LIGHTNING) {
            key_suffix = "_weapon";
            effect_suffix = "_weapon";
        }

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode outer_array = mapper.createArrayNode();
        ObjectNode main_node = mapper.createObjectNode()
                .put("key", "upgradednetherite/" + upgradeType.name + key_suffix)
                .put("integrity", -1)
                .put("group", "netherite_runes");
        outer_array.add(main_node);
        ObjectNode aspectNode = mapper.createObjectNode().put("netherite_upgraded", 1);
        main_node.set("aspects", aspectNode);
        ObjectNode effectsNode = mapper.createObjectNode().put("upgradednetherite:" + upgradeType.name + effect_suffix, 1);
        main_node.set("effects", effectsNode);
        main_node.set("models", modelsNode(moduleType, upgradeType, false));


        if (upgradeType == UpgradeType.AETHERIC) {
            effectsNode.put("aetheric_tetranomicon:aetheric", 1);
            main_node.set("conditions",
                    mapper.createArrayNode().add(mapper.createObjectNode()
                        .put("type", "forge:or")
                            .set("values", mapper.createArrayNode()
                                    .add(mapper.createObjectNode()
                                            .put("type", "forge:mod_loaded")
                                            .put("modid", "aether_treasure_reforging"))
                                    .add(mapper.createObjectNode()
                                            .put("type", "forge:mod_loaded")
                                            .put("modid", "aetheric_tetranomicon"))))
            );
        }

        if (upgradeType == UpgradeType.FORGOTTEN) {
            if (moduleType.upgradeCategory == UpgradeCategory.TOOL || moduleType.upgradeCategory == UpgradeCategory.BOTH) {
                effectsNode.put("undergardenpatch:undermine", 1);
            }
            if (moduleType.upgradeCategory == UpgradeCategory.WEAPON || moduleType.upgradeCategory == UpgradeCategory.BOTH) {
                effectsNode.put("undergardenpatch:threnody", 1);
            }
            main_node.set("conditions",
                    mapper.createArrayNode().add(mapper.createObjectNode()
                            .put("type", "forge:mod_loaded")
                            .put("modid", "undergardenpatch"))
            );
        }

        if (upgradeType == UpgradeType.ARCANE) {
            main_node.set("conditions",
                    mapper.createArrayNode().add(mapper.createObjectNode()
                            .put("type", "forge:mod_loaded")
                            .put("modid", "irons_spellbooks"))
            );
        }

        if (upgradeType == UpgradeType.PHANTOM) {
            ObjectNode attribute_node = mapper.createObjectNode();
            attribute_node.put("generic.block_interaction_range", 1.5);
            attribute_node.put("generic.entity_interaction_range", 1.5);
            main_node.set("attributes", attribute_node);
        }

        File file = filepath.toFile();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, outer_array);

    }

    private static void writeAestheticImprovementFile(ModuleType moduleType, UpgradeType upgradeType) {
        Path filepath = Path.of(improvements.toString(), "\\", upgradeType.name, "\\", moduleType.id + "_aesthetic.json");

        if (!filepath.getParent().toFile().exists()) {
            filepath.getParent().toFile().mkdirs();
        }
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode outer_array = mapper.createArrayNode();

        outer_array.add(mapper.createObjectNode()
                .put("key", "upgradednetherite/" + upgradeType.name + "_aesthetic")
                .set("aspects", mapper.createObjectNode().put("netherite_upgraded", 1))
                .put("group", "aesthetic_netherite_runes")
                .set("models", modelsNode(moduleType, upgradeType, true))
        );

        File file = filepath.toFile();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, outer_array);

    }

    private static ArrayNode modelsNode(ModuleType moduleType, UpgradeType upgradeType, boolean aesthetic) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode outer_array = mapper.createArrayNode();
        String layer = aesthetic ? "higher" : "high";
        if (moduleType == ModuleType.BASIC_STOCK) {
            layer = aesthetic ? "highest" : "higher";
        }

        if (moduleType == ModuleType.LONG_STAVE || moduleType == ModuleType.STRAIGHT_STAVE) {
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, ""));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "_0").put("type", "draw_0"));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "_1").put("type", "draw_1"));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "_2").put("type", "draw_2"));
        } else if (moduleType == ModuleType.RECURVE_STAVE) {
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, ""));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "").put("type", "draw_0"));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "_1").put("type", "draw_1"));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "_1").put("type", "draw_2"));
        } else if (moduleType == ModuleType.BASIC_STOCK) {
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, ""));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "").put("type", "draw_0"));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "").put("type", "draw_1"));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "").put("type", "draw_2"));
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "").put("type", "loaded"));
        } else if (moduleType == ModuleType.TOWER) {
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "").put("type", "tetra:plate/tower"));
        } else if (moduleType == ModuleType.HEATER) {
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "").put("type", "tetra:plate/heater"));
        } else if (moduleType == ModuleType.BUCKLER) {
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, "").put("type", "tetra:plate/buckler"));
        } else {
            outer_array.add(modelNode(mapper, layer, upgradeType, moduleType, ""));
        }

        return outer_array;

    }
    
    private static ObjectNode modelNode(ObjectMapper mapper, String layer, UpgradeType upgradeType, ModuleType moduleType, String suffix) {
        return mapper.createObjectNode().put("renderLayer", layer).put("emission", getEmissionFromType(upgradeType))
                .put("location", "tetra:item/module/upgradednetherite/" + upgradeType.name + "/" + moduleType.id + suffix);
    }

    private static int getEmissionFromType(UpgradeType upgradeType) {
        switch (upgradeType) {
            case RADIANT, ULTIMATE -> {
                return 16;
            }
            case FIRE, ARCANE, ENDER -> {
                return 12;
            }
            case PHANTOM, FEATHER, WITHER, CORRUPT -> {
                return 6;
            }
            default -> {
                return 8;
            }
        }
    }

    private static Path schematics = Path.of("src\\generated\\resources\\data\\tetra\\schematics");

    private static void writeAllSchematics() {
        writeSchematicFiles(ModularItemCategory.BOW, false, false);
        writeSchematicFiles(ModularItemCategory.CROSSBOW, false, false);
        writeSchematicFiles(ModularItemCategory.DOUBLE, false, false);
        writeSchematicFiles(ModularItemCategory.DOUBLE, false, true);
        writeSchematicFiles(ModularItemCategory.SHIELD, false, false);
        writeSchematicFiles(ModularItemCategory.SINGLE, false, false);
        writeSchematicFiles(ModularItemCategory.SWORD, false, false);
        writeSchematicFiles(ModularItemCategory.BOW, true, false);
        writeSchematicFiles(ModularItemCategory.CROSSBOW, true, false);
        writeSchematicFiles(ModularItemCategory.DOUBLE, true, false);
        writeSchematicFiles(ModularItemCategory.DOUBLE, true, true);
        writeSchematicFiles(ModularItemCategory.SHIELD, true, false);
        writeSchematicFiles(ModularItemCategory.SINGLE, true, false);
        writeSchematicFiles(ModularItemCategory.SWORD, true, false);
    }

    private static void writeSchematicFiles(ModularItemCategory category, boolean aesthetic, boolean right_side) {

        String filename = aesthetic ? "z_aesthetic_netherite.json" : "upgrade_netherite.json";

        if (category == ModularItemCategory.DOUBLE) {
            String suffix = right_side ? "_right" : "_left";
            filename = aesthetic ? "z_aesthetic_netherite" + suffix + ".json" : "upgrade_netherite" + suffix + ".json";
        }

        Path filepath = switch (category) {
            case BOW -> Path.of(schematics.toString(), "\\bow\\stave\\" + filename);
            case CROSSBOW -> Path.of(schematics.toString(), "\\crossbow\\stock\\" + filename);
            case DOUBLE -> Path.of(schematics.toString(), "\\double\\shared_head\\" + filename);
            case SHIELD -> Path.of(schematics.toString(), "\\shield\\plate\\" + filename);
            case SINGLE -> Path.of(schematics.toString(), "\\single\\head\\" + filename);
            case SWORD -> Path.of(schematics.toString(), "\\sword\\shared_blade\\" + filename);
        };
        if (!filepath.getParent().toFile().exists()) {
            filepath.getParent().toFile().mkdirs();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("replace", true);
        rootNode.set("slots", mapper.createArrayNode().add(
                switch (category) {
                    case BOW -> "bow/stave";
                    case CROSSBOW -> "crossbow/stock";
                    case DOUBLE -> "double/head_" + (right_side ? "right" : "left");
                    case SHIELD -> "shield/plate";
                    case SINGLE -> "single/head";
                    case SWORD -> "sword/blade";
                }
        ));
        rootNode.put("materialSlotCount", 1);
        rootNode.put("rarity", "hone");
        rootNode.put("displayType", "major");
        rootNode.put("materialRevealSlot", 0);
        rootNode.set("glyph", mapper.createObjectNode()
                .put("textureX", 32)
                .put("textureY", 240));

//        if (category == ModularItemCategory.DOUBLE) {
//            ObjectNode requirementNode = mapper.createObjectNode();
//            requirementNode.put("type", "tetra:slot");
//            requirementNode.put("slot", "double/head_" + (right_side ? "right" : "left"));
//            rootNode.set("requirement", requirementNode);
//        }

        ArrayNode outcomesArray = mapper.createArrayNode();
        rootNode.set("outcomes", outcomesArray);

        for (UpgradeType upgradeType : UpgradeType.values()) {
            ObjectNode outcomeNode = mapper.createObjectNode();
            outcomeNode.set("material", mapper.createObjectNode()
                    .set("items", mapper.createArrayNode().add("teupnepa:" + upgradeType.name + "_upgraded_netherite_ingot")));
            outcomeNode.set("requiredTools", mapper.createObjectNode()
                    .put("hammer_dig", "minecraft:diamond"));

            ObjectNode improvements = mapper.createObjectNode();
            if (aesthetic) {
                improvements.put("upgradednetherite/" + upgradeType.name + "_aesthetic", 0);
            } else {
                switch (category) {
                    case BOW, CROSSBOW -> {
                        improvements.put("upgradednetherite/" + upgradeType.name + "_bow", 0);
                        if (!upgradeType.spell_school.isBlank()) {
                            improvements.put("upgradednetherite/" + upgradeType.spell_school + "_spell_power", 0);
                        }
                    }
                    case DOUBLE -> {
                        improvements.put("upgradednetherite/" + upgradeType.name, 0);
                        improvements.put("upgradednetherite/" + upgradeType.name + "_tool", 0);
                        improvements.put("upgradednetherite/" + upgradeType.name + "_weapon", 0);
                        improvements.put("upgradednetherite/" + upgradeType.name + "_both", 0);
                        if (!upgradeType.spell_school.isBlank()) {
                            improvements.put("upgradednetherite/" + upgradeType.spell_school + "_spell_power", 0);
                        }
                    }
                    case SHIELD -> {
                        improvements.put("upgradednetherite/" + upgradeType.name + "_shield", 0);
                        if (!upgradeType.spell_school.isBlank()) {
                            improvements.put("upgradednetherite/" + upgradeType.spell_school + "_magic_shield", 0);
                        }
                    }
                    case SINGLE, SWORD -> {
                        improvements.put("upgradednetherite/" + upgradeType.name + "_tool", 0);
                        improvements.put("upgradednetherite/" + upgradeType.name + "_weapon", 0);
                        if (!upgradeType.spell_school.isBlank()) {
                            improvements.put("upgradednetherite/" + upgradeType.spell_school + "_spell_power", 0);
                        }
                    }
                }
            }

            outcomeNode.set("improvements", improvements);
            outcomesArray.add(outcomeNode);
        }

        File file = filepath.toFile();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

    }

}

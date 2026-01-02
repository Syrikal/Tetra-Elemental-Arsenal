package com.syric.teupnepa.file_management;

import com.syric.teupnepa.enums.ModularItemCategory;
import com.syric.teupnepa.enums.ModuleType;
import com.syric.teupnepa.enums.UpgradeCategory;
import com.syric.teupnepa.enums.UpgradeType;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.nio.file.Path;

public class DataManagement {

    public static void main(String[] args) {

//        for (ModuleType type : ModuleType.values()) {
//            writeModulesFile(type);
//        }

//        writeAestheticImprovementFile(ModuleType.ADZE, UpgradeType.CORRUPT);
        writeAllImprovementFiles();

//        writeAllSchematics();

//        printAllImprovements();

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

    private static Path modules = Path.of("src\\main\\resources\\data\\tetra\\modules");

    private static void writeModulesFile(ModuleType moduleType) {
        Path filepath = Path.of(modules.toString(), "\\", moduleType.category.id, "\\", moduleType.id + ".json");

        if (!filepath.getParent().toFile().exists()) {
            filepath.getParent().toFile().mkdirs();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("replace", false);
        ArrayNode improvementsArray = rootNode.putArray("improvements");

        //TODO Check if it needs ".json" in the array values!
        for (UpgradeType type : UpgradeType.values()) {
            if (moduleType != ModuleType.BUTT) {
                improvementsArray.add("tetra:upgradednetherite/" + type.name + "/" + moduleType.id);
            }
            if (moduleType.category == ModularItemCategory.DOUBLE) {
                improvementsArray.add("tetra:upgradednetherite/" + type.name + "/" + moduleType.id + "_aesthetic");
            }
        }
        File file = filepath.toFile();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        mapper.writer().with(prettyPrinter).writeValue(file, rootNode);
    }

    private static Path improvements = Path.of("src\\main\\resources\\data\\tetra\\improvements\\upgradednetherite");

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
        ArrayNode modelsNode = mapper.createArrayNode().add(mapper.createObjectNode()
                .put("location", "tetra:item/module/upgradednetherite/" + upgradeType.name + "/" + moduleType.id)
                .put("renderLayer", "high"));
        main_node.set("models", modelsNode);

        if (upgradeType == UpgradeType.AETHERIC) {
            effectsNode.put("aetheric_tetranomicon:aetheric", 1);
            ObjectNode improvement_node = (ObjectNode) outer_array.get(0);
            improvement_node.set("conditions",
                    mapper.createArrayNode().add(mapper.createObjectNode()
                        .put("type", "forge:mod_loaded")
                        .put("modid", "aetheric_tetranomicon"))
            );
        }

        if (upgradeType == UpgradeType.FORGOTTEN) {
            if (moduleType.upgradeCategory == UpgradeCategory.TOOL || moduleType.upgradeCategory == UpgradeCategory.BOTH) {
                effectsNode.put("undergardenpatch:undermine", 1);
            }
            if (moduleType.upgradeCategory == UpgradeCategory.WEAPON || moduleType.upgradeCategory == UpgradeCategory.BOTH) {
                effectsNode.put("undergardenpatch:threnody", 1);
            }
            ObjectNode improvement_node = (ObjectNode) outer_array.get(0);
            improvement_node.set("conditions",
                    mapper.createArrayNode().add(mapper.createObjectNode()
                            .put("type", "forge:mod_loaded")
                            .put("modid", "undergardenpatch"))
            );
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
                .set("models", mapper.createArrayNode().add(mapper.createObjectNode()
                        .put("location", "tetra:item/module/upgradednetherite/" + upgradeType.name + "/" + moduleType.id)
                        .put("renderLayer", "higher")))
        );

        File file = filepath.toFile();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, outer_array);

    }

    private static Path schematics = Path.of("src\\main\\resources\\data\\tetra\\schematics");

    private static void writeAllSchematics() {
        writeSchematicFiles(ModularItemCategory.BOW, false, false);
        writeSchematicFiles(ModularItemCategory.CROSSBOW, false, false);
        writeSchematicFiles(ModularItemCategory.DOUBLE, false, false);
        writeSchematicFiles(ModularItemCategory.DOUBLE, false, true);
        writeSchematicFiles(ModularItemCategory.DOUBLE, true, false);
        writeSchematicFiles(ModularItemCategory.SHIELD, false, false);
        writeSchematicFiles(ModularItemCategory.SINGLE, false, false);
        writeSchematicFiles(ModularItemCategory.SWORD, false, false);
    }

    private static void writeSchematicFiles(ModularItemCategory category, boolean aesthetic, boolean right_side) {

        Path filepath = switch (category) {
            case BOW -> Path.of(schematics.toString(), "\\bow\\stave\\upgrade_netherite.json");
            case CROSSBOW -> Path.of(schematics.toString(), "\\crossbow\\stock\\upgrade_netherite.json");
            case DOUBLE -> Path.of(schematics.toString(), "\\double\\shared_head\\" + (aesthetic ? "z_aesthetic_netherite.json" : "upgrade_netherite_" + (right_side ? "right.json" : "left.json")));
            case SHIELD -> Path.of(schematics.toString(), "\\shield\\plate\\upgrade_netherite.json");
            case SINGLE -> Path.of(schematics.toString(), "\\single\\head\\upgrade_netherite.json");
            case SWORD -> Path.of(schematics.toString(), "\\sword\\shared_blade\\upgrade_netherite.json");
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
                    case DOUBLE -> "double/head_" + ((aesthetic || right_side) ? "right" : "left");
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

        if (category == ModularItemCategory.DOUBLE) {
            ObjectNode requirementNode = mapper.createObjectNode();
            if (aesthetic) {
                requirementNode.put("type", "tetra:or");
                requirementNode.set("requirements", mapper.createArrayNode()
                        .add(mapper.createObjectNode()
                                .put("type", "tetra:module")
                                .put("module", "double/butt_right"))
                        .add(mapper.createObjectNode()
                                .put("type", "tetra:module")
                                .put("module", "double/basic_pickaxe_right")));
            } else {
                requirementNode.put("type", "tetra:slot");
                requirementNode.put("slot", "double/head_" + (right_side ? "right" : "left"));
            }
            rootNode.set("requirement", requirementNode);
        }

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
                    }
                    case DOUBLE -> {
                        improvements.put("upgradednetherite/" + upgradeType.name, 0);
                        improvements.put("upgradednetherite/" + upgradeType.name + "_tool", 0);
                        improvements.put("upgradednetherite/" + upgradeType.name + "_weapon", 0);
                        improvements.put("upgradednetherite/" + upgradeType.name + "_both", 0);
                    }
                    case SHIELD -> {
                        improvements.put("upgradednetherite/" + upgradeType.name + "_shield", 0);
                    }
                    case SINGLE, SWORD -> {
                        improvements.put("upgradednetherite/" + upgradeType.name + "_tool", 0);
                        improvements.put("upgradednetherite/" + upgradeType.name + "_weapon", 0);
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

package com.syric.elementalarsenal.file_management;

import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class TagManagement {
    private static final Path entity_tags = Path.of("src\\generated\\resources\\data\\elementalarsenal\\tags\\entity_types");
    private static final Path mob_effect_tags = Path.of("src\\generated\\resources\\data\\elementalarsenal\\tags\\mob_effect");

    public static void main(String[] args) {

        printEntityTagFile("gold_damaged", new Tag()
                .addVanillaList(
                        "hoglin",
                        "piglin",
                        "zombified_piglin",
                        "zoglin",
                        "piglin_brute")
                .addModded("alexsmobs",
                        "tusklin")
                .addModded("betternether",
                        "flying_pig"));
        
        printEntityTagFile("sculk", new Tag()
                .addVanilla("warden")
                .addModdedTag("sculkhorde", "sculk_entity")
                .addModdedList("deeperdarker",     
                        "angler_fish",
                        "sculk_centipede",
                        "sculk_leech",
                        "sculk_snapper",
                        "shriek_worm",
                        "sludge",
                        "shattered",
                        "stalker")
                .addModded("alexsmobs", "skreecher"));

        printEntityTagFile("frost_damaged", new Tag()
                .addVanillaTag("freeze_hurts_extra_types")
                .addVanillaList(
                        "blaze",
                        "magma_cube")
                .addModdedTag("spore", "fungus_entities")
                .addModdedTag("aether", "fire_mob")
                .addModdedList("alexsmobs",
                        "laviathan",
                        "sunbird",
                        "bone_serpent",
                        "bone_serpent_part")
                .addModdedList("betternether",
                        "naga",
                        "skull"));

        printEntityTagFile("fungal", new Tag()
                .addModdedTag("spore", "fungus_entities"));

        printEntityTagFile("phantom", new Tag()
                .addVanilla("phantom")
                .addModded("alexsmobs", "spectre")
                .addModded("sculkhorde", "sculk_phantom"));

        printEntityTagFile("end_native", new Tag()
                .addVanillaList(
                        "enderman",
                        "endermite",
                        "ender_dragon")
                .addModdedList("mofus_better_end_",
                        "greater_endermitee",
                        "end_manta",
                        "eye_guardian",
                        "eye_guardian_seeker",
                        "eye_guardian_ground_seeker",
                        "zombified_enderman",
                        "endermite_guardian",
                        "eye_guardian_crawler",
                        "eye_guardian_tower",
                        "guardian_golem",
                        "endermite_queen",
                        "void_jelly",
                        "meteor_crab",
                        "sun_glider",
                        "axefish",
                        "forgotten",
                        "forgotten_litch",
                        "reborn_litch",
                        "luminion_golem",
                        "great_fin_leviathan",
                        "nomouth_enderman",
                        "bloodworm",
                        "starfly",
                        "blackwasp",
                        "starflyshelless",
                        "endermite_swarmer",
                        "crown_squid",
                        "alpha_crown_squid",
                        "eye_guardian_machine",
                        "eye_guardian_walker",
                        "crestguin",
                        "crestling",
                        "crestguin_tamable",
                        "eye_guardian_golem")
                .addModdedList("betterend",
                        "dragonfly",
                        "end_slime",
                        "end_fish",
                        "shadow_walker",
                        "cubozoa",
                        "silk_moth")
                .addModdedList("alexsmobs",
                        "endergrade",
                        "enderiophage",
                        "void_worm",
                        "void_worm_part",
                        "cosmaw",
                        "cosmic_cod",
                        "spectre"));

        printEntityTagFile("wither", new Tag()
                .addVanillaList("wither", "wither_skeleton")
                .addModdedList("betternether",
                        "naga",
                        "skull"));

        printEntityTagFile("aether_native", new Tag()
                .addModdedTag("aether", "treated_as_aether_entity"));

        printMobEffectTagFile("radiant_reduces_strong", new Tag()
                .addVanillaList("wither", "poison")
                .addModdedList("sculkhorde", "sculk_infected")
                .addModdedList("spore", "mycelium_ef")
                .addModdedList("undergarden", "virulence")
                .addModdedList("alexscaves", "irradiated"));

        printMobEffectTagFile("radiant_reduces", new Tag()
                .addVanillaList("blindness", "darkness", "hunger", "mining_fatigue", "nausea", "slowness", "unluck", "weakness")
                .addModdedList("sculkhorde", "corroded", "sculk_lure", "rooted", "sculk_fog")
                .addModdedList("spore", "corrosion", "madness", "marker")
                .addModdedList("aether", "inebriation")
                .addModdedList("alexsmobs", "debilitating_sting", "ender_flu", "exsanguination", "fear")
                .addModdedList("irons_spellbooks", "blight", "slowed")
                .addModdedList("tetra", "bleeding", "earthbound", "exhausted", "punctured", "severed", "stun")
                .addModdedList("undergarden", "brittleness")
                .addModdedList("alexscaves", "stunned"));
        
    }

    private static void printEntityTagFile(String tagName, Tag tag) {
        Path filepath = Path.of(entity_tags.toString(), tagName + ".json");
        if (!filepath.getParent().toFile().exists()) {
            filepath.getParent().toFile().mkdirs();
        }
        ObjectMapper mapper = new ObjectMapper();
        File file = filepath.toFile();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        mapper.writer().with(prettyPrinter).writeValue(file, tag.getTagNode());
    }

    private static void printMobEffectTagFile(String tagName, Tag tag) {
        Path filepath = Path.of(mob_effect_tags.toString(), tagName + ".json");
        if (!filepath.getParent().toFile().exists()) {
            filepath.getParent().toFile().mkdirs();
        }
        ObjectMapper mapper = new ObjectMapper();
        File file = filepath.toFile();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        mapper.writer().with(prettyPrinter).writeValue(file, tag.getTagNode());
    }

    private static class Tag {

        final ObjectNode node;
        final ArrayNode values;
        final ObjectMapper mapper;

        private Tag() {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            ArrayNode values = mapper.createArrayNode();
            node.set("values", values);
            this.node = node;
            this.values = values;
            this.mapper = mapper;
        }

        private ObjectNode getTagNode() {
            return node;
        }

        private Tag addVanilla(String targetID) {
            values.add("minecraft:" + targetID);
            return this;
        }

        private Tag addVanillaTag(String tagID) {
            values.add("#minecraft:" + tagID);
            return this;
        }
        
        private Tag addVanillaList(String... targetIDs) {
            Arrays.stream(targetIDs).forEach(this::addVanilla);
            return this;
        }

        private Tag addModded(String modID, String targetID) {
            ObjectNode newNode = mapper.createObjectNode();
            newNode.put("required", false);
            newNode.put("id", modID + ":" + targetID);
            values.add(newNode);
            return this;
        }

        private Tag addModdedTag(String modID, String tagID) {
            ObjectNode newNode = mapper.createObjectNode();
            newNode.put("required", false);
            newNode.put("id", "#" + modID + ":" + tagID);
            values.add(newNode);
            return this;
        }

        private Tag addModdedList(String modID, String... targetIDs) {
            Arrays.stream(targetIDs).forEach(id -> addModded(modID, id));
            return this;
        }
    }

}

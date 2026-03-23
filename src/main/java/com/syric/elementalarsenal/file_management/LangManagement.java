package com.syric.elementalarsenal.file_management;

import com.syric.elementalarsenal.enums.UpgradeCategory;
import com.syric.elementalarsenal.enums.UpgradeType;

public class LangManagement {

    public static void main(String[] args) {
        generateImprovementLangLines();
        generateAestheticImprovementLangLines();
    }

    private static void generateImprovementLangLines() {
        for (UpgradeType upgradeType : UpgradeType.values()) {
            for (UpgradeCategory category : UpgradeCategory.values()) {
                if (category == UpgradeCategory.NONE) {
                    continue;
                }

                String suffix =  "_" + category.name().toLowerCase();
                String fancyName = switch (upgradeType) {
                    case CORRUPT -> "Corrupted";
                    case ECHO -> "Resonant";
                    case ENDER -> "Voidwalker";
                    case FEATHER -> "Skyseeker";
                    case FIRE -> "Magmatic";
                    case FROST -> "Frigid";
                    case LIGHTNING -> "Fulminous";
                    case GOLD -> "Gilded";
                    case PHANTOM -> "Phantasmal";
                    case POISON -> "Vile";
                    case RADIANT -> "Radiant";
                    case WATER -> "Abyssal";
                    case WITHER -> "Blighted";
                    case ULTIMATE -> "Paragon";
                    case AETHERIC -> "Aetheric";
                    case FORGOTTEN -> "Forgotten";
                    case ARCANE -> "Arcane";
                };

                String improvement_name_line = "\"tetra.improvement.elementalarsenal/" + upgradeType.name + suffix + ".name\": \"" + fancyName + " Runes\",";
                System.out.println(improvement_name_line);

                String improvement_description_line = "\"tetra.improvement.elementalarsenal/" + upgradeType.name + suffix + ".description\": \"\",";
                System.out.println(improvement_description_line);
            }
        }
    }

    private static void generateAestheticImprovementLangLines() {
        for (UpgradeType upgradeType : UpgradeType.values()) {
            String fancyName = switch (upgradeType) {
                case CORRUPT -> "Corrupted";
                case ECHO -> "Resonant";
                case ENDER -> "Voidwalker";
                case FEATHER -> "Skyseeker";
                case FIRE -> "Magmatic";
                case FROST -> "Frigid";
                case LIGHTNING -> "Fulminous";
                case GOLD -> "Gilded";
                case PHANTOM -> "Phantasmal";
                case POISON -> "Vile";
                case RADIANT -> "Radiant";
                case WATER -> "Abyssal";
                case WITHER -> "Blighted";
                case ULTIMATE -> "Paragon";
                case AETHERIC -> "Aetheric";
                case FORGOTTEN -> "Forgotten";
                case ARCANE -> "Arcane";
            };

            String improvement_name_line = "\"tetra.improvement.elementalarsenal/" + upgradeType.name + "_aesthetic" + "\": \"" + fancyName + " Runes (Aesthetic)\",";
            System.out.println(improvement_name_line);
            String improvement_description_line = "\"tetra.improvement.elementalarsenal/" + upgradeType.name + "_aesthetic" + ".description\": \"Aesthetic enhancement, no effect\",";
            System.out.println(improvement_description_line);

        }
    }

}

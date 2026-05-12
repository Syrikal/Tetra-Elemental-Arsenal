package com.syric.elementalarsenal.file_management;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TextureManagement {

    static final String texturesFolder = "src\\generated\\resources\\assets\\tetra\\textures\\item\\module_old_manual\\elementalarsenal";
    static final String destinationFolder = "src\\generated\\resources\\assets\\tetra\\textures\\item\\module\\elementalarsenal";
    static final String ingotDestinationFolder = "src\\generated\\resources\\assets\\elementalarsenal\\textures\\item";

    static final String corruptTextures = "src\\main\\resources\\assets\\tetra\\textures\\item\\module\\elementalarsenal\\corrupt";
    static final String corruptIngotTexture = "src\\main\\resources\\assets\\elementalarsenal\\textures\\item\\corrupt_imbued_netherite_ingot.png";

    static final Color[] echoColors = new Color[] {
            new Color(17, 27, 33),
            new Color(2, 41, 51),
            new Color(0, 79, 97),
            new Color(0, 144, 150),
            new Color(0, 221, 237)
    };

    static final Color[] radiantColors = new Color[] {
            new Color(247, 179, 82),
            new Color(247, 201, 82),
            new Color(255, 249, 146),
            new Color(250, 246, 185),
            new Color(250, 248, 214)
    };

    static final Color[] forgottenColors = new Color[] {
            new Color(47, 76, 76),
            new Color(41, 123, 103),
            new Color(58, 186, 140),
            new Color(81, 216, 164),
            new Color(123, 255, 189)
    };

    static final Color[] aethericColors = new Color[]{
            new Color(73, 84, 84),
            new Color(101, 115, 115),
            new Color(227, 243, 243),
            new Color(238, 255, 255),
            new Color(255, 255, 255)
    };

    static final Color[] frostColors = new Color[]{
            new Color(106,204,230),
            new Color(128,229,239),
            new Color(168,247,255),
            new Color(200,250,255),
            new Color(225,252,255)
    };

    static final Color[] arcaneColors = new Color[]{
            new Color(100,24,154),
            new Color(153,56,195),
            new Color(150,139,226),
            new Color(113,187,226),
            new Color(95,209,226)
    };

    static final Color[] lightningColors = new Color[] {
            new Color(56, 196, 224),
            new Color(152, 217, 235),
            new Color(195, 234, 246),
            new Color(241, 250, 254),
            new Color(251, 254, 254)
    };

    static final Color[] witherColors = new Color[] {
            new Color(17, 17, 17),
            new Color(27, 27, 27),
            new Color(52, 52, 52),
            new Color(60, 65, 65),
            new Color(81, 83, 83)
    };

    static final Color[] waterColors = new Color[] {
            new Color(72, 100, 92),
            new Color(111, 163, 152),
            new Color(142, 196, 182),
            new Color(178, 216, 201),
            new Color(223, 233, 219)
    };

    static final Color[] ultimateColors = new Color[] {
            new Color(42, 2, 0),
            new Color(64, 5, 0),
            new Color(112, 11, 0),
            new Color(160, 27, 0),
            new Color(253, 51, 5)
    };

    static final Color[] poisonColors = new Color[] {
            new Color(0, 19, 19),
            new Color(10, 27, 24),
            new Color(17, 38, 32),
            new Color(56, 68, 56),
            new Color(70, 82, 70)
    };

    static final Color[] phantomColors = new Color[] {
            new Color(99, 78, 66),
            new Color(149, 141, 119),
            new Color(169, 156, 133),
            new Color(195, 186, 159),
            new Color(220, 218, 190)
    };

    static final Color[] goldColors = new Color[] {
            new Color(70, 29, 0),
            new Color(128, 72, 0),
            new Color(222, 154, 0),
            new Color(235, 181, 0),
            new Color(251, 217, 37)
    };

    static final Color[] fireColors = new Color[] {
            new Color(97, 10, 0),
            new Color(141, 56, 0),
            new Color(211, 124, 0),
            new Color(255, 155, 0),
            new Color(255, 217, 0)
    };

    static final Color[] featherColors = new Color[] {
            new Color(150, 150, 150),
            new Color(190, 190, 190),
            new Color(211, 211, 211),
            new Color(239, 239, 239),
            new Color(255, 255, 255)
    };

    static final Color[] enderColors = new Color[] {
            new Color(1, 38, 32),
            new Color(1, 57, 49),
            new Color(3, 93, 81),
            new Color(25, 131, 115),
            new Color(4, 204, 176)
    };


    public static void main(String[] args) throws IOException {

//        reorganizeTextures();

//        makeNewTextures("radiant", radiantColors, true);
//        makeNewTextures("echo", echoColors, true);
//        makeNewTextures("forgotten", forgottenColors, true);
//        makeNewTextures("aetheric", aethericColors, true);
//        makeNewTextures("frost", frostColors, true);
//        makeNewTextures("arcane", arcaneColors, true);
//        makeNewTextures("lightning", lightningColors, true);
        
        makeIngotTexture("radiant", radiantColors);
        makeIngotTexture("echo", echoColors);
        makeIngotTexture("forgotten", forgottenColors);
        makeIngotTexture("aetheric", aethericColors);
        makeIngotTexture("frost", frostColors);
        makeIngotTexture("arcane", arcaneColors);
        makeIngotTexture("lightning", lightningColors);
        makeIngotTexture("wither", witherColors);
        makeIngotTexture("water", waterColors);
        makeIngotTexture("ultimate", ultimateColors);
        makeIngotTexture("poison", poisonColors);
        makeIngotTexture("phantom", phantomColors);
        makeIngotTexture("gold", goldColors);
        makeIngotTexture("fire", fireColors);
        makeIngotTexture("feather", featherColors);
        makeIngotTexture("ender", enderColors);

    }

    //This was used to rearrange the textures folder. It shouldn't be used again.
    private static void reorganizeTextures() {
        try (Stream<Path> pathStream = Files.walk(Path.of(texturesFolder))) {
            pathStream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".png"))
                    .forEach(file_path -> {
                            String module = file_path.getParent().getFileName().toString();
                            String[] filename = StringUtils.removeEnd(file_path.getFileName().toString(), ".png").split("_");
                            String upgrade_type = filename[0];
                            String suffix = filename.length > 1 ? "_" + filename[1] : "";
                            Path destination = Path.of(destinationFolder, upgrade_type, module + suffix + ".png");
                            System.out.println(destination);

                            if (!destination.toFile().exists()) {
                                if (!destination.getParent().toFile().exists()) {
                                    destination.getParent().toFile().mkdirs();
                                }
                                try {
                                    Files.copy(file_path, destination, StandardCopyOption.REPLACE_EXISTING);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makeNewTextures(String upgrade_type, Color[] colors, boolean doIngotTexture) throws IOException {

        Path destination_folder = Path.of(destinationFolder, upgrade_type);
        if (!destination_folder.toFile().exists()) {
            destination_folder.toFile().mkdirs();
        }

        if (colors.length != 5) {
            throw new IllegalArgumentException("makeNewTextures requires 5 colors");
        }
        Map<Color, Color> colorMap = new HashMap<>();
        colorMap.put(new Color(29, 1, 8), colors[0]);
        colorMap.put(new Color(41, 8, 16), colors[1]);
        colorMap.put(new Color(72, 10, 25), colors[2]);
        colorMap.put(new Color(100, 26, 44), colors[3]);
        colorMap.put(new Color(111, 33, 52), new Color(
                (int) (0.45 * colors[3].getRed() + 0.55 * colors[4].getRed()),
                (int) (0.45 * colors[3].getGreen() + 0.55 * colors[4].getGreen()),
                (int) (0.45 * colors[3].getBlue() + 0.55 * colors[4].getBlue())));
        colorMap.put(new Color(117, 37, 57), colors[4]);

        File[] textures = new File(corruptTextures).listFiles();

        for (File file : textures) {
            Path destination = Path.of(destinationFolder, upgrade_type, file.getName());
            BufferedImage original_image = ImageIO.read(file);
            BufferedImage output_image = new BufferedImage(original_image.getWidth(), original_image.getHeight(), BufferedImage.TYPE_INT_ARGB);

            for (int x = 0; x < original_image.getWidth(); x++) {
                for (int y = 0; y < original_image.getHeight(); y++) {
                    Color original_color = new Color(original_image.getRGB(x, y), true);
                    boolean color_matched = colorMap.containsKey(original_color);
                    if (!color_matched && original_color.getAlpha() > 0) {
                        System.out.println("Color " + original_color + " with transparency " + original_color.getAlpha() + " in file " + file.toPath().getFileName() + " not matched");
                    }
                    Color output_color = colorMap.getOrDefault(original_color, original_color);
                    output_image.setRGB(x, y, output_color.getRGB());
                }
            }

            ImageIO.write(output_image, "png", destination.toFile());
        }

        if (doIngotTexture) {
            Path ingot_destination = Path.of(ingotDestinationFolder, upgrade_type + "_imbued_netherite_ingot.png");
            BufferedImage original_image = ImageIO.read(Path.of(corruptIngotTexture).toFile());
            BufferedImage output_image = new BufferedImage(original_image.getWidth(), original_image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < original_image.getWidth(); x++) {
                for (int y = 0; y < original_image.getHeight(); y++) {
                    Color original_color = new Color(original_image.getRGB(x, y), true);
                    Color output_color = colorMap.getOrDefault(original_color, original_color);
                    output_image.setRGB(x, y, output_color.getRGB());
                }
            }

            ImageIO.write(output_image, "png", ingot_destination.toFile());
        }


    }

    private static void makeIngotTexture(String upgrade_type, Color[] colors) throws IOException {

        Path destination_folder = Path.of(destinationFolder, upgrade_type);
        if (!destination_folder.toFile().exists()) {
            destination_folder.toFile().mkdirs();
        }

        if (colors.length != 5) {
            throw new IllegalArgumentException("makeNewTextures requires 5 colors");
        }
        Map<Color, Color> colorMap = new HashMap<>();
        colorMap.put(new Color(29, 1, 8), colors[0]);
        colorMap.put(new Color(41, 8, 16), colors[1]);
        colorMap.put(new Color(72, 10, 25), colors[2]);
        colorMap.put(new Color(100, 26, 44), colors[3]);
        colorMap.put(new Color(111, 33, 52), new Color(
                (int) (0.45 * colors[3].getRed() + 0.55 * colors[4].getRed()),
                (int) (0.45 * colors[3].getGreen() + 0.55 * colors[4].getGreen()),
                (int) (0.45 * colors[3].getBlue() + 0.55 * colors[4].getBlue())));
        colorMap.put(new Color(117, 37, 57), colors[4]);

        Path ingot_destination = Path.of(ingotDestinationFolder, upgrade_type + "_imbued_netherite_ingot.png");
        BufferedImage original_image = ImageIO.read(Path.of(corruptIngotTexture).toFile());
        BufferedImage output_image = new BufferedImage(original_image.getWidth(), original_image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < original_image.getWidth(); x++) {
            for (int y = 0; y < original_image.getHeight(); y++) {
                Color original_color = new Color(original_image.getRGB(x, y), true);
                Color output_color = colorMap.getOrDefault(original_color, original_color);
                output_image.setRGB(x, y, output_color.getRGB());
            }
        }

        ImageIO.write(output_image, "png", ingot_destination.toFile());


    }

}

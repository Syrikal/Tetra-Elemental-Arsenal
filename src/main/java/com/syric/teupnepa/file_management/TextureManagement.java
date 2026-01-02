package com.syric.teupnepa.file_management;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TextureManagement {

    static String texturesFolder = "src\\main\\resources\\assets\\tetra\\textures\\item\\module\\upgradednetherite";
    static String destinationFolder = "src\\main\\resources\\assets\\tetra\\textures\\item\\module";

    static String corruptTextures = "src\\main\\resources\\assets\\tetra\\textures\\item\\module\\corrupt";
    static String corruptIngotTexture = "src\\main\\resources\\assets\\teupnepa\\textures\\item\\corrupt_upgraded_netherite_ingot.png";

    static Color[] echoColors = new Color[] {
            new Color(17, 27, 33),
            new Color(2, 41, 51),
            new Color(0, 79, 97),
            new Color(0, 144, 150),
            new Color(0, 221, 237)
    };

    static Color[] radiantColors = new Color[] {
            new Color(247, 179, 82),
            new Color(247, 201, 82),
            new Color(255, 249, 146),
            new Color(250, 246, 185),
            new Color(250, 248, 214)
    };

    static Color[] forgottenColors = new Color[] {
            new Color(47, 76, 76),
            new Color(41, 123, 103),
            new Color(58, 186, 140),
            new Color(81, 216, 164),
            new Color(123, 255, 189)
    };

    static Color[] aethericColors = new Color[] {

    };


    public static void main(String[] args) throws IOException {

//        reorganizeTextures();
        makeNewTextures("radiant", radiantColors, false);
        makeNewTextures("echo", echoColors, false);
        makeNewTextures("forgotten", forgottenColors, true);

    }

    //This was used to rearrange the textures folder. It shouldn't be used again.
    private static void reorganizeTextures() {
//        try (Stream<Path> pathStream = Files.walk(Path.of(texturesFolder))) {
//            pathStream.filter(Files::isRegularFile)
//                    .filter(p -> p.getFileName().toString().endsWith(".png"))
//                    .forEach(file_path -> {
//                            String module = file_path.getParent().getFileName().toString();
//                            String[] filename = StringUtils.removeEnd(file_path.getFileName().toString(), ".png").split("_");
//                            String upgrade_type = filename[0];
//                            String suffix = filename.length > 1 ? "_" + filename[1] : "";
//                            Path destination = Path.of(destinationFolder, upgrade_type, module + suffix + ".png");
//                            System.out.println(destination);
//
//                            if (!destination.toFile().exists()) {
//                                if (!destination.getParent().toFile().exists()) {
//                                    destination.getParent().toFile().mkdirs();
//                                }
//                                try {
//                                    Files.copy(file_path, destination, StandardCopyOption.REPLACE_EXISTING);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
//                    );
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void makeNewTextures(String upgrade_type, Color[] colors, boolean doIngotTexture) throws IOException {

        Path destination_folder = Path.of(corruptTextures, "..\\", upgrade_type);
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
            Path destination = Path.of(file.toString().replace("corrupt", upgrade_type));
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
            Path ingot_destination = Path.of(corruptIngotTexture.replace("corrupt", upgrade_type));
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

}

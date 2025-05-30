package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm{
    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int grayValue = (int) (0.21 * r + 0.72 * g + 0.07 * b);

                // Compose grayscale pixel (no alpha, so use TYPE_INT_RGB)
                int grayRgb = (grayValue << 16) | (grayValue << 8) | grayValue;
                gray.setRGB(x, y, grayRgb);
            }
        }

        return gray;
    }
}

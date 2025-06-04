package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm{

    private static final int[][] gx = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    private static final int[][] gy = {
            {1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1}
    };

    private final ImageAlgorithm grayscaleAlgorithm;

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm)  {
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }

        BufferedImage greyImage = grayscaleAlgorithm.process(image);

        int width = greyImage.getWidth();
        int height = greyImage.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int pixelX = 0;
                int pixelY = 0;

                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        int rgb = greyImage.getRGB(x + i, y + j);
                        int gray = rgb & 0xFF; // Since it's grayscale, R=G=B

                        pixelX += gray * gx[j + 1][i + 1];
                        pixelY += gray * gy[j + 1][i + 1];
                    }
                }

                int magnitude = (int) Math.min(255, Math.hypot(pixelX, pixelY));
                int edgeRgb = (magnitude << 16) | (magnitude << 8) | magnitude;
                output.setRGB(x, y, edgeRgb);
            }
        }

        return output;
    }
}

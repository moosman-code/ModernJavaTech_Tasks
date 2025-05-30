package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.GrayscaleAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SobelEdgeDetectionTest {

    @Test
    public void testCorrectEdgeHighlight() throws IOException {
        BufferedImage image = createTestImageWithVerticalEdge();
        EdgeDetectionAlgorithm sobelAlgorithm = new SobelEdgeDetection(new LuminosityGrayscale());
        BufferedImage result = sobelAlgorithm.process(image);

// Expect the center column to have higher intensity (white/gray)
        int centerPixel = result.getRGB(1, 1) & 0xFF;
        assertTrue(centerPixel > 100, "Expected strong edge at center");

// Expect outer pixels to remain dark
        int leftPixel = result.getRGB(0, 1) & 0xFF;
        int rightPixel = result.getRGB(2, 1) & 0xFF;
        assertTrue(leftPixel < 50 && rightPixel < 50, "Expected no edge at sides");

    }

    public static BufferedImage createTestImageWithVerticalEdge() {
        int width = 3;
        int height = 3;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Fill left column (x = 0) with black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 1, height);

        // Center column (x = 1) will stay default (black or whatever it is)

        // Fill right column (x = 2) with white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(2, 0, 1, height);

        g2d.dispose();

        return image;
    }
}

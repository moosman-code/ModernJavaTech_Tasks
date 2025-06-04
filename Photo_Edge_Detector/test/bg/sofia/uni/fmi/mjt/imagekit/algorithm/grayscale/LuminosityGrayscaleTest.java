package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LuminosityGrayscaleTest {
    @Test
    public void testIfAllPixelsAreShadedOfGray() throws IOException {

        BufferedImage image = ImageIO.read(new File("test/bg/sofia/uni/fmi/mjt/imagekit/images/kitten.jpg"));
        GrayscaleAlgorithm grayscale = new LuminosityGrayscale();
        BufferedImage greyImage = grayscale.process(image);

        int width = greyImage.getWidth();
        int height = greyImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = greyImage.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                assertTrue(r == g && g == b, String.format(
                        "Pixel at (%d, %d) is not grayscale: R=%d, G=%d, B=%d", x, y, r, g, b));
            }
        }
    }
}

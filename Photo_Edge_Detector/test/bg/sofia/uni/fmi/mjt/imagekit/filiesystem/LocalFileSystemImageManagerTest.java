package bg.sofia.uni.fmi.mjt.imagekit.filiesystem;

import bg.sofia.uni.fmi.mjt.imagekit.filesystem.FileSystemImageManager;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class LocalFileSystemImageManagerTest {

    private final FileSystemImageManager fileManager = new LocalFileSystemImageManager();

    private LocalFileSystemImageManager manager;

    @BeforeEach
    public void setUp() {
        manager = new LocalFileSystemImageManager();
    }

    // loadImage Tests
    @Test
    public void testNullImageThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            fileManager.loadImage(null);
        });
    }

    @Test
    public void testNonExistentImageThrowsIllegalArgumentException() {
        File nonExistent = new File("path/to/nonexistent/image.png");

        assertThrows(IllegalArgumentException.class, () -> {
            fileManager.loadImage(nonExistent);
        });
    }

    @Test
    public void testIncorrectImageFormatIOException() {
        File image = new File("test/bg/sofia/uni/fmi/mjt/imagekit/images/cat.avif");

        assertThrows(IOException.class, () -> {
            fileManager.loadImage(image);
        });
    }

    // loadImagesFromDirectory Tests
    @Test
    public void testNullDirectoryThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            fileManager.loadImagesFromDirectory(null);
        });
    }

    @Test
    public void testNonExistentDirectoryThrowsIllegalArgumentException() {
        File nonExistent = new File("path/to/nonexistent");

        assertThrows(IllegalArgumentException.class, () -> {
            fileManager.loadImagesFromDirectory(nonExistent);
        });
    }

    // saveImage Tests
    @Test
    public void testSaveImageThrowsOnNullImage() {
        File imageFile = Mockito.mock(File.class);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.saveImage(null, imageFile);
        });
    }

    @Test
    public void testSaveImageThrowsOnNullFile() {
        BufferedImage image = Mockito.mock(BufferedImage.class);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.saveImage(image, null);
        });
    }

    @Test
    public void testSaveImageThrowsIfFileAlreadyExists() {
        BufferedImage image = Mockito.mock(BufferedImage.class);
        File file = Mockito.mock(File.class);
        when(file.exists()).thenReturn(true);

        assertThrows(IOException.class, () -> {
            manager.saveImage(image, file);
        });
    }

    @Test
    public void testSaveImageThrowsIfParentDirectoryMissing() {
        BufferedImage image = Mockito.mock(BufferedImage.class);
        File file = Mockito.mock(File.class);
        File parent = Mockito.mock(File.class);

        when(file.exists()).thenReturn(false);
        when(file.getParentFile()).thenReturn(parent);
        when(parent.exists()).thenReturn(false);

        assertThrows(IOException.class, () -> {
            manager.saveImage(image, file);
        });
    }
}

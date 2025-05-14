package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class LocalFileSystemImageManager implements FileSystemImageManager{

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null || !imageFile.exists()) {
            throw new IllegalArgumentException("Image file cannot be null and must exist");
        }

        try {
            checkImageFormat(imageFile);

            BufferedImage originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                throw new IOException("Unsupported image format or corrupted file");
            }

            return originalImage;
        } catch (IOException e) {
            throw e;
        }
    }

    private void checkImageFormat(File file) throws IOException {
        String format = getImageFormat(file);
        if (!"png".equals(format) && !"jpeg".equals(format)) {
            throw new IOException("Illegal image format, acceptable formats are png and jpg");
        }
    }

    private static String getImageFormat(File file) throws IOException {
        try (ImageInputStream iis = ImageIO.createImageInputStream(file)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                return reader.getFormatName().toLowerCase(); // "png", "jpeg"
            }
        } catch (IOException e) {
            throw e;
        }

        return null;
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null || !imagesDirectory.exists()) {
            throw new IllegalArgumentException("Directory of images cannot be null and must exist");
        }

        List<BufferedImage> result = new ArrayList<>();

        try {
            if (!imagesDirectory.isDirectory()) {
                throw new IOException("Given argument is not a directory");
            }

            File[] files = imagesDirectory.listFiles();
            if (files == null) {
                throw new IOException("Failed to list files in directory: " + imagesDirectory.getAbsolutePath());
            }

            for (File image : files) {
                File source = new File(image.getPath());

                if (source.isDirectory()) {
                    result.addAll(loadImagesFromDirectory(source));
                } else {
                    checkImageFormat(source);

                    BufferedImage originalImage = ImageIO.read(source);
                    if (originalImage == null) {
                        throw new IOException("Unsupported image format or corrupted file");
                    }

                    result.add(originalImage);
                }
            }

            return List.copyOf(result);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Image and image file cannot be null");
        } else if (imageFile.exists()) {
            throw new IOException("Image file already exist");
        }else if (imageFile.getParentFile() != null && !imageFile.getParentFile().exists()) {
            throw new IOException("Parent directory does not exist");
        }

        try {
            if (!ImageIO.write(image, "png", imageFile)) {
                throw new IOException("No appropriate writer found for image format");
            }
        } catch (IOException e) {
            throw e;
        }
    }
}

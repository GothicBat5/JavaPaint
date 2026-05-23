import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverter 
{
    private static final String ASCII_CHARS = "@#S%?*+;:,. ";

    public static BufferedImage loadImage(File file) 
    {
        try {
            return ImageIO.read(file);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertToASCII(BufferedImage image) 
    {
        if (image == null) return "Error: Image not loaded";

        int width = 120;
        double aspectCorrection = 0.47;
        int height = (int)((image.getHeight() * width * aspectCorrection) / image.getWidth());
        if (height < 1) height = 1;

        Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();

        StringBuilder ascii = new StringBuilder();
        
        for (int y = 0; y < height; y++) 
        {
            for (int x = 0; x < width; x++) 
            {
                int rgb = resized.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                int brightness = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                int index = (brightness * (ASCII_CHARS.length() - 1)) / 255;
                ascii.append(ASCII_CHARS.charAt(index));
            }
            ascii.append("\n");
        }
        return ascii.toString();
    }
}

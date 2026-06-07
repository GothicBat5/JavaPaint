import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileSave 
{

    private final JFrame parent;
    private File currentFile = null;

    public FileSave(JFrame parent) 
    {
        this.parent = parent;
    }

    public BufferedImage openFile() 
    {
        JFileChooser fc = buildChooser();
        fc.setDialogTitle("Open");
        int result = fc.showOpenDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) return null;

        File f = fc.getSelectedFile();

        try {
            BufferedImage img = ImageIO.read(f);
            if (img == null) { error("Unsupported image format."); return null; }
            currentFile = f;
            updateTitle(f.getName());
            return img;
        } 
        catch (IOException e) 
        {
            error("Could not open file:\n" + e.getMessage());
            return null;
        }
    }

    public void saveFile(BufferedImage image) 
    {
        if (currentFile != null) writeImage(image, currentFile);
        else saveFileAs(image);
    }

    public void saveFileAs(BufferedImage image) 
    {
        JFileChooser fc = buildChooser();
        fc.setDialogTitle("Save As");
        int result = fc.showSaveDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File f = fc.getSelectedFile();
        String name = f.getName().toLowerCase();

        // auto-append extension from filter if missing
        String fmt = "png";
        if (name.endsWith(".bmp")) fmt = "bmp";
        else if (name.endsWith(".jpg") || name.endsWith(".jpeg")) fmt = "jpg";
        else if (!name.endsWith(".png")) f = new File(f.getAbsolutePath() + ".png");

        writeImage(image, f);
        currentFile = f;
        updateTitle(f.getName());
    }

    private void writeImage(BufferedImage image, File f) 
    {

        String fmt = getFormat(f.getName());
        try {
            ImageIO.write(image, fmt, f);
        } 
        catch (IOException e) 
        {
            error("Could not save file:\n" + e.getMessage());
        }
    }

    private String getFormat(String name) 
    {
        name = name.toLowerCase();
        if (name.endsWith(".bmp"))  return "bmp";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "jpg";
        return "png";
    }

    private JFileChooser buildChooser() 
    {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image (*.png)", "png"));
        fc.addChoosableFileFilter(new FileNameExtensionFilter("BMP Image (*.bmp)", "bmp"));
        fc.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image (*.jpg)", "jpg", "jpeg"));
        return fc;
    }

    private void updateTitle(String filename) 
    {
        parent.setTitle(filename + " - Paint");
    }

    private void error(String msg) 
    {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void resetFile() 
    {
        currentFile = null;
        parent.setTitle("untitled - Paint");
    }
}

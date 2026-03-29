package paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SavingThis
{
    private SavingThis()
    {

    };

    public static void saveImage(BufferedImage image, JFrame parent)
    {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Save Image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();

            try
            {
                //save images to PNG
                ImageIO.write(image, "png", new File(fileToSave + ".png"));
                JOptionPane.showMessageDialog(parent, "Saved successfully!");
            }
            catch (IOException ex)
            {
                //upt
                JOptionPane.showMessageDialog(parent, "Error saving file: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE); 
            }
        }
    }
}

package paint; 

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SavingThis
{
    public static void saveImage(BufferedImage image, JFrame parent)
    {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Save Image");

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
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error saving file.");
            }
        }
    }
}

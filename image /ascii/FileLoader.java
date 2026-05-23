import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileLoader 
{
    public static File chooseImageFile(java.awt.Component parent) 
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select an Image");
        chooser.setFileFilter(new FileNameExtensionFilter("Image Files (jpg, png, jpeg, bmp)", "jpg", "jpeg", "png", "bmp"));
        
        int result = chooser.showOpenDialog(parent);
        return (result == JFileChooser.APPROVE_OPTION) ? chooser.getSelectedFile() : null;
    }
}

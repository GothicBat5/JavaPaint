import javax.swing.*;
import java.io.File;

public class FileLoader {

    public static File chooseImageFile() {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Select an Image");

        chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Image Files (jpg, png, jpeg, bmp)",
                        "jpg", "jpeg", "png", "bmp"
                )
        );

        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;
    }
}
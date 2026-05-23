import javax.swing.*;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(ImageFrame::new);
    }
}

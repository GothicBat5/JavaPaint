package imcool;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Light Bulb Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new BulbSimulatorPanel());
            frame.setMinimumSize(new java.awt.Dimension(380, 520));
            frame.setSize(420, 580);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

import javax.swing.*;

public class Main 
{
    public static void main(String[] args) 
    {

        try {

            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            MetalTheme();
        } 
        catch (Exception ignored) 
        {

        }

        SwingUtilities.invokeLater(() -> new PaintApp().setVisible(true));
    }

    private static void MetalTheme() 
    {
        UIManager.put("control", new java.awt.Color(192, 192, 192));
        UIManager.put("controlHighlight", new java.awt.Color(255, 255, 255));
        UIManager.put("controlDkShadow", new java.awt.Color(64, 64, 64));
        UIManager.put("controlShadow", new java.awt.Color(128, 128, 128));
        UIManager.put("controlLtHighlight", new java.awt.Color(223, 223, 223));
    }
}

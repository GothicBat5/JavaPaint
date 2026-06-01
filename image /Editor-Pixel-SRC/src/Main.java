package candy_rush;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main
{
    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ignored) 
        {
          // File manager:: Native OS opne ?? try      
        }
        System.out.println("\nProgram Started\n");
        SwingUtilities.invokeLater(ScreenX::new);
    }
}

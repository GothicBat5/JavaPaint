package candy_rush;

import javax.swing.SwingUtilities;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(ScreenX::new);

        System.out.println("\nProgram Started.");
    }
}

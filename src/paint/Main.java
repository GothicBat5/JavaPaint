package paint;

import javax.swing.SwingUtilities;

public class Main
{
    public static void main(String[] args)
    {
                /*
        * The main execution block as we all know
        * Place where the main execution code locks in
        * */
        
        SwingUtilities.invokeLater(PaintF::new);
    }
}

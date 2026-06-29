import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Self
{
    private static void birth()
    {
        long seed = System.nanoTime() ^ (System.currentTimeMillis() * 0x9E3779B97F4A7C15L);
        Random spawner = new Random(seed);

        int count = 2 + spawner.nextInt(3);
        List<ENIAC> minds = new ArrayList<>();

        for (int i = 0; i < count; i++)
        {
            long childSeed = seed ^ (spawner.nextLong() * (i + 1));
            minds.add(new ENIAC(childSeed));
        }

        CanvasX world = new CanvasX(960, 680, minds);

        JFrame frame = new JFrame("Self — life seed " + seed);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(world);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        world.startLife();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(Self::birth);
    }
}

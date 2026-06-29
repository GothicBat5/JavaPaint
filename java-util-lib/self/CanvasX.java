import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

public class CanvasX extends JPanel
{
    private final int width;
    private final int height;

    private final BufferedImage buffer;
    private final Graphics2D g2;

    private final List<ENIAC> minds;
    private Timer heartbeat;

    public CanvasX(int width, int height, List<ENIAC> minds)
    {
        this.width = width;
        this.height = height;
        this.minds = minds;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2.setColor(minds.get(0).backgroundColor());
        g2.fillRect(0, 0, width, height);
    }

    public void startLife()
    {
        heartbeat = new Timer(30, e ->
        {
            for (ENIAC mind : minds)
            {
                mind.think(this);
            }
            repaint();
        });
        heartbeat.start();
    }

    public int worldWidth()
    {
        return width;
    }

    public int worldHeight()
    {
        return height;
    }

    public void drawDot(double x, double y, double radius, Color color)
    {
        double r = Math.max(0.3, radius);
        g2.setColor(color);
        g2.fillOval((int) Math.round(x - r), (int) Math.round(y - r),
                (int) Math.round(r * 2), (int) Math.round(r * 2));
    }

    public void drawLine(double x1, double y1, double x2, double y2, Color color, float thickness)
    {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(Math.max(0.4f, thickness),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine((int) Math.round(x1), (int) Math.round(y1),
                (int) Math.round(x2), (int) Math.round(y2));
    }

    public void fadeHistory(int alpha)
    {
        int a = Math.max(0, Math.min(255, alpha));
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setColor(new Color(0, 0, 0, a));
        g2.fillRect(0, 0, width, height);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);
    }
}

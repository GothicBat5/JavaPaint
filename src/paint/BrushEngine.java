package paint;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class BrushEngine
{
    public enum BrushType
    {
        ROUND, SQUARE, KNIFE, SPRAY, CALLIGRAPHY
    }

    private BrushType currentBrush = BrushType.ROUND;

    public void setBrush(BrushType brush)
    {
        currentBrush = brush;
    }

    public BrushType getCurrentBrush()
    {
        return currentBrush;
    }

    public void drawStroke(Graphics2D g2, int x1, int y1, int x2, int y2, int size, Color color)
    {
        g2.setColor(color);

        switch (currentBrush)
        {
            case ROUND:
                drawRound(g2, x1, y1, x2, y2, size);
                break;
            case SQUARE:
                drawSquare(g2, x1, y1, x2, y2, size);
                break;
            case KNIFE:
                drawKnife(g2, x1, y1, x2, y2, size);
                break;
            case SPRAY:
                drawSpray(g2, x2, y2, size);
                break;
            case CALLIGRAPHY:
                drawCalligraphy(g2, x1, y1, x2, y2, size);
                break;
        }
    }

    public void drawDot(Graphics2D g2, int x, int y, int size, Color color)
    {
        g2.setColor(color);

        switch (currentBrush)
        {
            case ROUND:
                g2.fillOval(x - size / 2, y - size / 2, size, size);
                break;
            case SQUARE:
                g2.fillRect(x - size / 2, y - size / 2, size, size);
                break;
            case KNIFE:
                drawKnife(g2, x, y, x + 1, y + 1, size);
                break;
            case SPRAY:
                drawSpray(g2, x, y, size);
                break;
            case CALLIGRAPHY:
                drawCalligraphy(g2, x, y, x + 1, y + 1, size);
                break;
        }
    }
//Round
    private void drawRound(Graphics2D g2, int x1, int y1, int x2, int y2, int size)
    {
        g2.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x1, y1, x2, y2);
    }

//Pixel Square
    private void drawSquare(Graphics2D g2, int x1, int y1, int x2, int y2, int size)
    {
        int steps = Math.max(1, (int) distance(x1, y1, x2, y2));

        for (int i = 0; i <= steps; i++)
        {
            float t = (float) i / steps;
            int px = (int) (x1 + t * (x2 - x1));
            int py = (int) (y1 + t * (y2 - y1));
            g2.fillRect(px - size / 2, py - size / 2, size, size);
        }
    }

  //KNIFE???
    private double prevAngle = 0; //keeps stroke continuity

    private void drawKnife(Graphics2D g2, int x1, int y1, int x2, int y2, int size)
    {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dist = distance(x1, y1, x2, y2);

        if (dist == 0) return;
//Dir
        double angle = Math.atan2(dy, dx);

//smooth
        angle = lerp(prevAngle, angle, 0.25);
        prevAngle = angle;

        //pressure ---
        float speedFactor = Math.min(1f, (float) dist / 20f);

        int baseLength = Math.max(6, size * 2);
        int baseWidth = Math.max(1, size / 5);

        // Slower = thicker, Faster = thinner
        int dynamicWidth = (int)(baseWidth * (1.8f - speedFactor));
        int dynamicLength = (int)(baseLength * (1.2f - speedFactor * 0.4f));

        //Reduce stamping density
        int steps = Math.max(1, (int)(dist / 2));

        Graphics2D g2c = (Graphics2D) g2.create();
        g2c.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2c.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

        for (int i = 0; i <= steps; i++)
        {
            float t = (float) i / steps;
            int px = (int) (x1 + t * dx);
            int py = (int) (y1 + t * dy);

            //Taper
            float taper = 1.0f - Math.abs(0.5f - t) * 2;
            int w = Math.max(1, (int)(dynamicWidth * (0.6f + taper)));
            int l = Math.max(2, (int)(dynamicLength * (0.8f + taper * 0.4f)));

            //Controlled jitter
            double jitter = (Math.random() - 0.5) * 0.15;

            AffineTransform at = AffineTransform.getTranslateInstance(px, py);
            at.rotate(angle + jitter);
            g2c.setTransform(at);

            //Drag offset
            int offset = w / 2;

            g2c.fillRect(-l / 2, -offset, l, w);
        }

        g2c.dispose();
    }

    private double lerp(double a, double b, double t)
    {
        return a + (b - a) * t;
    }
//Sprayyyy!
    private void drawSpray(Graphics2D g2, int cx, int cy, int size)
    {
        int radius = size * 2;
        int density = size * 3;          // dots per event
        double r2 = (double) radius * radius;

        for (int i = 0; i < density; i++)
        {
            double angle = Math.random() * 2 * Math.PI;
            double dist = Math.random() * radius;
            int px = (int) (cx + dist * Math.cos(angle));
            int py = (int) (cy + dist * Math.sin(angle));

            // Only paint inside the circle
            double dx = px - cx, dy = py - cy;
            if (dx * dx + dy * dy <= r2)
            {
                g2.fillRect(px, py, 1, 1);
            }
        }
    }

    //CALLIGRAPHY
    private void drawCalligraphy(Graphics2D g2, int x1, int y1, int x2, int y2, int size)
    {
        int steps = Math.max(1, (int) distance(x1, y1, x2, y2));
        int wide = Math.max(4, size);  // width of the nib
        int thin = Math.max(1, size / 4);  // thickness of the nib

        Graphics2D g2c = (Graphics2D) g2.create();
        g2c.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i <= steps; i++)
        {
            float t  = (float) i / steps;
            int px = (int) (x1 + t * (x2 - x1));
            int py = (int) (y1 + t * (y2 - y1));

            AffineTransform at = AffineTransform.getTranslateInstance(px, py);
            at.rotate(Math.toRadians(45)); // fixed 45° nib angle
            g2c.setTransform(at);
            g2c.fillOval(-wide / 2, -thin / 2, wide, thin);
        }
        g2c.dispose();
    }

    //Uti
    private double distance(int x1, int y1, int x2, int y2)
    {
        double dx = x2 - x1, dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

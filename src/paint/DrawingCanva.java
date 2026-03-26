package paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DrawingCanva extends JPanel
{

    private BufferedImage canvas;
    private Graphics2D g2;
    private int prevX, prevY;
    private boolean dragging;
    private Color drawColor = Color.BLACK;
    private int brushSize = 5;

    public DrawingCanva()
    {
        setBackground(Color.WHITE);
        setDoubleBuffered(false);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                prevX = e.getX();
                prevY = e.getY();
                dragging = true;
                drawDot(prevX, prevY);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                dragging = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (dragging && g2 != null)
                {
                    int x = e.getX();
                    int y = e.getY();

                    g2.setStroke(new BasicStroke(
                            brushSize,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    ));

                    g2.drawLine(prevX, prevY, x, y);
                    prevX = x;
                    prevY = y;
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (canvas == null)
        {
            canvas = new BufferedImage(
                    getWidth(), getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            g2 = canvas.createGraphics();
            g2.setRenderingHint
            (
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setPaint(drawColor);
        }
        g.drawImage(canvas, 0, 0, null);
    }

    public void clear()
    {
        if (g2 != null)
        {
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setPaint(drawColor);
            repaint();
        }
    }

    public void setColor(Color c)
    {
        drawColor = c;
        if (g2 != null) g2.setPaint(drawColor);
    }

    public void setBrushSize(int size)
    {
        brushSize = size;
    }

    private void drawDot(int x, int y)
    {
        if (g2 != null)
        {
            g2.fillOval(
                    x - brushSize / 2,
                    y - brushSize / 2,
                    brushSize,
                    brushSize );
            repaint();
        }
    }
}

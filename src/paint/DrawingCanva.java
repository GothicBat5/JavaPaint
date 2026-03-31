package paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DrawingCanva extends JPanel
{

    private BufferedImage canvas;
    private BufferedImage overlayCanvas;
    
    private Graphics2D g2;
    private int prevX, prevY;
    private boolean dragging;
    private Color drawColor = Color.BLACK;
    
    private int brushSize = 5;
    private boolean erasing = false;
    private int startX, startY;

    private final int canvasWidth;
    private final int canvasHeight;

    public void setErasing(boolean e)
    {
        erasing = e;
    }

    public DrawingCanva(int canvasWidth, int canvasHeight)
    {
        this.canvasWidth  = canvasWidth;
        this.canvasHeight = canvasHeight;

        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        setBackground(Color.LIGHT_GRAY);
        setDoubleBuffered(true);

        addMouseListener(new MouseAdapter()
        {
            
            @Override
            public void mousePressed(MouseEvent e)
            {
                startX = prevX = e.getX();
                startY = prevY = e.getY();
                dragging = true;
                if (currentTool == Tool.BRUSH) drawDot(prevX, prevY);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                
                dragging = false;
                int endX = e.getX();
                int endY = e.getY();
                if (g2 == null) return;

                g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                switch (currentTool)
                {
                    case RECT:
                        g2.drawRect(Math.min(startX, endX), Math.min(startY, endY),
                                Math.abs(endX - startX), Math.abs(endY - startY));
                        break;
                    case OVAL:
                        g2.drawOval(Math.min(startX, endX), Math.min(startY, endY),
                                Math.abs(endX - startX), Math.abs(endY - startY));
                        break;
                    case LINE:
                        g2.drawLine(startX, startY, endX, endY);
                        break;
                }

                overlayCanvas = null;
                
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (!dragging || g2 == null) return;
                int x = e.getX();
                int y = e.getY();

                if (currentTool == Tool.BRUSH)
                {
                    g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(prevX, prevY, x, y);
                    prevX = x;
                    prevY = y;
                    overlayCanvas = null;
                }
                else
                {
                    overlayCanvas = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D og = overlayCanvas.createGraphics();
                    og.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    og.setColor(drawColor);
                    og.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                    switch (currentTool)
                    {
                        case RECT:
                            og.drawRect(Math.min(startX, x), Math.min(startY, y),
                                    Math.abs(x - startX), Math.abs(y - startY));
                            break;
                        case OVAL:
                            og.drawOval(Math.min(startX, x), Math.min(startY, y),
                                    Math.abs(x - startX), Math.abs(y - startY));
                            break;
                        case LINE:
                            og.drawLine(startX, startY, x, y);
                            break;
                    }
                    og.dispose();
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (canvas == null)
        {
            canvas = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
            g2 = canvas.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, canvasWidth, canvasHeight);
            g2.setPaint(erasing ? Color.WHITE : drawColor);
        }

        g.drawImage(canvas, 0, 0, null);

        if (overlayCanvas != null)
        {
            g.drawImage(overlayCanvas, 0, 0, null);
        }
    }

    public void clear()
    {
        if (g2 != null)
        {
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, canvasWidth, canvasHeight);
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
            g2.fillOval(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
            repaint();
        }
    }

    public enum Tool {

        BRUSH, RECT, OVAL, LINE

    }

    private Tool currentTool = Tool.BRUSH;

    public void setTool(Tool tool)
    {
        currentTool = tool;
    }

    public BufferedImage getImage()
    {
        return canvas;
    }
}

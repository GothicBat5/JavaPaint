import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ZoomaX extends JPanel {
    private BufferedImage image;
    private double zoom = 1.0;
    private int panX = 0, panY = 0;
    private int lastMouseX, lastMouseY;
    private boolean dragging = false;

    public ZoomaX() 
    {
        setBackground(new Color(18, 18, 24));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseWheelListener(e -> {
            double delta = e.getPreciseWheelRotation();
            double factor = (delta < 0) ? 1.1 : 0.9;
            zoom = Math.max(0.1, Math.min(zoom * factor, 20.0));
            repaint();
        });

        MouseAdapter ma = new MouseAdapter() {
            public void mousePressed(MouseEvent e) 
            {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                dragging = true;
            }
            
            public void mouseReleased(MouseEvent e) 
            { 
                dragging = false; 
            }
            
            public void mouseDragged(MouseEvent e) 
            {
                if (dragging) 
                {
                    panX += e.getX() - lastMouseX;
                    panY += e.getY() - lastMouseY;
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                    repaint();
                }
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public void setImage(BufferedImage img) 
    {
        this.image = img;
        zoom = 1.0;
        panX = 0;
        panY = 0;
        repaint();
    }

    public void resetView() 
    {
        zoom = 1.0;
        panX = 0;
        panY = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (image == null) 
        {
            g.setColor(new Color(80, 80, 100));
            g.setFont(new Font("SansSerif", Font.PLAIN, 13));
            String hint = "Original image will appear here";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(hint, (getWidth() - fm.stringWidth(hint)) / 2, getHeight() / 2);
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int imgW = (int)(image.getWidth() * zoom);
        int imgH = (int)(image.getHeight() * zoom);
        int drawX = (getWidth() - imgW) / 2 + panX;
        int drawY = (getHeight() - imgH) / 2 + panY;
        g2d.drawImage(image, drawX, drawY, imgW, imgH, null);

        g2d.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g2d.setColor(new Color(200, 200, 220, 180));
        String zoomStr = String.format("%.0f%%", zoom * 100);
        g2d.drawString(zoomStr, 10, getHeight() - 10);
    }
}

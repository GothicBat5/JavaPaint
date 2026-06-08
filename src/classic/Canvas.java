import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel 
{

    private BufferedImage image;
    private BufferedImage previewLayer; // for shape preview
    private Graphics2D g2d;
    private final Brushes brushes;
    private Color fgColor = Color.BLACK;
    private Color bgColor = Color.WHITE;
    private int lastX, lastY;
    private boolean drawing = false;
    private StatusListener statusListener;

    public interface StatusListener 
    {
        void onCursorMove(int x, int y);
    }

    public Canvas(Brushes brushes) 
    {
        this.brushes = brushes;
        setPreferredSize(new Dimension(600, 450));
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        initImage(600, 450);

        MouseAdapter ma = new MouseAdapter() 
        {
            public void mousePressed(MouseEvent e)  
            { 
                handlePress(e); 
            }
            public void mouseDragged(MouseEvent e)  
            { 
                handleDrag(e); 
            }
            public void mouseReleased(MouseEvent e) 
            { 
                handleRelease(e); 
            }
            public void mouseMoved(MouseEvent e) 
            {
                if (statusListener != null) statusListener.onCursorMove(e.getX(), e.getY());
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    private void initImage(int w, int h) 
    {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        previewLayer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
    }

    private void handlePress(MouseEvent e) 
    {
        int x = e.getX(), y = e.getY();
        boolean right = SwingUtilities.isRightMouseButton(e);
        Color active = right ? bgColor : fgColor;

        if (brushes.getTool() == Brushes.Tool.FILL) 
        {
            brushes.fill(image, x, y, active);
            repaint();
            return;
        }

        brushes.setStart(x, y);
        lastX = x; lastY = y;
        drawing = true;

        if (!brushes.isShapeTool()) 
        {
            Graphics2D ig = image.createGraphics();
            brushes.draw(ig, x, y, x, y, active);
            ig.dispose();
            repaint();
        }
    }

    private void handleDrag(MouseEvent e) 
    {
        if (!drawing) return;

        int x = e.getX(), y = e.getY();
        boolean right = SwingUtilities.isRightMouseButton(e);
        Color active = right ? bgColor : fgColor;

        if (statusListener != null) statusListener.onCursorMove(x, y);

        if (brushes.isShapeTool()) 
        {
            // clear preview layer and redraw shape outline
            Graphics2D pg = previewLayer.createGraphics();
            pg.setComposite(AlphaComposite.Clear);
            pg.fillRect(0, 0, previewLayer.getWidth(), previewLayer.getHeight());
            pg.setComposite(AlphaComposite.SrcOver);
            brushes.drawShape(pg, brushes.getStartX(), brushes.getStartY(), x, y, active);
            pg.dispose();
        } 
        else {
            Graphics2D ig = image.createGraphics();
            brushes.draw(ig, lastX, lastY, x, y, active);
            ig.dispose();
            lastX = x; lastY = y;
        }
        repaint();
    }

    private void handleRelease(MouseEvent e) 
    {
        if (!drawing) return;
        int x = e.getX(), y = e.getY();
        boolean right = SwingUtilities.isRightMouseButton(e);
        Color active = right ? bgColor : fgColor;

        if (brushes.isShapeTool()) 
        {
            Graphics2D ig = image.createGraphics();
            brushes.drawShape(ig, brushes.getStartX(), brushes.getStartY(), x, y, active);
            ig.dispose();
            // clear preview
            Graphics2D pg = previewLayer.createGraphics();
            pg.setComposite(AlphaComposite.Clear);
            pg.fillRect(0, 0, previewLayer.getWidth(), previewLayer.getHeight());
            pg.dispose();
        }
        drawing = false;
        repaint();
    }

    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
        g.drawImage(previewLayer, 0, 0, null);
    }

    public void newCanvas() 
    {
        initImage(getWidth(), getHeight());
        repaint();
    }

    public void resizeCanvas(int w, int h) 
    {
        BufferedImage old = image;
        initImage(w, h);
        g2d.drawImage(old, 0, 0, null);
        setPreferredSize(new Dimension(w, h));
        revalidate();
        repaint();
    }

    public BufferedImage getImage() { return image; }
    public void setFgColor(Color c) { fgColor = c; }
    public void setBgColor(Color c) { bgColor = c; }
    public void setStatusListener(StatusListener l) { statusListener = l; }

    public void loadImage(BufferedImage img) 
    {
        initImage(img.getWidth(), img.getHeight());
        g2d.drawImage(img, 0, 0, null);
        setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        revalidate();
        repaint();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton
{
    final private Color bgColor;
    final private Color hoverColor;
    final private Color pressColor;
    final private Color glowColor;

    private boolean hovered = false;
    private boolean pressed = false;
    final private int radius = 14;

    // Glow border strength (0 = none)
    private int glowStrength = 0;

    public RoundedButton(String text, Color bg, Color glow)
    {
        super(text);
        this.bgColor = bg;
        this.glowColor = glow;
        this.hoverColor = brighten(bg, 30);
        this.pressColor = darken(bg, 20);

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFont(new Font("Consolas", Font.BOLD, 17));
        setForeground(Pallete.TEXT_BRIGHT);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter()
        {

            public void mouseEntered(MouseEvent e)
            {
                hovered = true;  
                glowStrength = 3; 
                repaint();
            }

            public void mouseExited (MouseEvent e)
            {
                hovered = false; 
                glowStrength = 0; 
                repaint();
            }

            public void mousePressed(MouseEvent e)
            {
                pressed = true;  
                repaint();
            }

            public void mouseReleased(MouseEvent e)
            {
                pressed = false; 
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fill = pressed ? pressColor : (hovered ? hoverColor : bgColor);

        // Glow layers
        if (hovered && glowColor != null)
        {
            for (int i = glowStrength; i >= 1; i--)
            {
                g2.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 30 / i));
                g2.fillRoundRect(-i, -i, getWidth() + i*2, getHeight() + i*2, radius + i, radius + i);
            }
        }

        g2.setColor(fill);

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.setColor(hovered ? glowColor : Pallete.BORDER);

        g2.setStroke(new BasicStroke(hovered ? 1.5f : 1.0f));

        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) { /* handled above */ }


    private static Color brighten(Color c, int amt)
    {
        return new Color(Math.min(255, c.getRed() + amt), Math.min(255, c.getGreen() + amt), Math.min(255, c.getBlue() + amt));
    }

    private static Color darken(Color c, int amt)
    {
        return new Color(Math.max(0, c.getRed()   - amt), Math.max(0, c.getGreen() - amt), Math.max(0, c.getBlue() - amt));
    }
}

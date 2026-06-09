import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Paint extends JPanel 
{

    private Color foreground = Color.BLACK;
    private Color background = Color.WHITE;

    private final ColorBox fgBox;
    private final ColorBox bgBox;

    private ColorChangeListener listener;

    public interface ColorChangeListener 
    {
        void onForegroundChanged(Color c);
        void onBackgroundChanged(Color c);
    }

    private static final Color[] PALETTE = {
        Color.BLACK, new Color(128,0,0), new Color(0,128,0), new Color(0,0,128),
        new Color(128,128,0), new Color(0,128,128), new Color(128,0,128), Color.DARK_GRAY,
        Color.GRAY, Color.RED, Color.GREEN, Color.BLUE,
        Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.WHITE,
        new Color(255,128,0), new Color(128,255,0), new Color(0,255,128), new Color(0,128,255),
        new Color(128,0,255), new Color(255,0,128), new Color(192,192,192), new Color(64,64,64),
        new Color(255,200,150), new Color(150,255,200), new Color(200,150,255), new Color(255,255,200)
    };

    public Paint() 
    {
        setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
        setBackground(new Color(192, 192, 192));

        // fg/bg stacked boxes
        JPanel colorBoxPanel = new JPanel(null) 
        {
            { 
                setPreferredSize(new Dimension(40, 36)); 
                setOpaque(false); 
            }
        };

        bgBox = new ColorBox(background, false);
        bgBox.setBounds(14, 10, 22, 22);
        fgBox = new ColorBox(foreground, true);
        fgBox.setBounds(4, 4, 22, 22);
        colorBoxPanel.add(bgBox);
        colorBoxPanel.add(fgBox);
        add(colorBoxPanel);

        // palette swatches
        JPanel palettePanel = new JPanel(new GridLayout(2, PALETTE.length / 2, 1, 1));
        palettePanel.setBackground(new Color(192, 192, 192));

        for (Color c : PALETTE) 
        {
            Swatch sw = new Swatch(c);
            sw.addMouseListener(new MouseAdapter() 
            {
                public void mousePressed(MouseEvent e) 
                {
                    if (SwingUtilities.isLeftMouseButton(e)) setForeground(c);
                    else setBackground2(c);
                }
            });
            palettePanel.add(sw);
        }
        add(palettePanel);

        // "more colors" button
        JButton more = new JButton("...");
        more.setPreferredSize(new Dimension(28, 36));
        more.setFont(new Font("Arial", Font.BOLD, 10));
        more.setToolTipText("More colors");
        more.addActionListener(e -> openColorPicker());
        add(more);
    }

    private void openColorPicker() 
    {
        Color c = JColorChooser.showDialog(this, "Pick Color", foreground);
        if (c != null) setForeground(c);
    }

    public void setForeground(Color c) 
    {
        foreground = c;
        fgBox.setColor(c);
        if (listener != null) listener.onForegroundChanged(c);
        repaint();
    }

    public void setBackground2(Color c) 
    {
        background = c;
        bgBox.setColor(c);
        if (listener != null) listener.onBackgroundChanged(c);
        repaint();
    }

    public Color getForeground() { return foreground; }
    public Color getBackground2() { return background; }
    public void setColorChangeListener(ColorChangeListener l) { listener = l; }

    // small colored square swatch
    private static class Swatch extends JPanel 
    {
        Swatch(Color c) 
        {
            setBackground(c);
            setPreferredSize(new Dimension(14, 14));
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            setToolTipText(String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue()));
        }
    }

    // fg/bg indicator box with beveled border
    private static class ColorBox extends JPanel 
    {
        private Color color;
        ColorBox(Color c, boolean raised) 
        {
            color = c;
            setBorder(BorderFactory.createBevelBorder(raised ? javax.swing.border.BevelBorder.RAISED : javax.swing.border.BevelBorder.LOWERED));
        }

        void setColor(Color c) 
        { 
            color = c; 
            repaint(); 
        }

        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            g.setColor(color);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

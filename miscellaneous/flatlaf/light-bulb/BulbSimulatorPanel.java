package imcool;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BulbSimulatorPanel extends JPanel
{

    private final BulbView bulbView = new BulbView();
    private final PowerButton powerButton = new PowerButton();

    public BulbSimulatorPanel()
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(bulbView, BorderLayout.CENTER);
        add(buildControls(), BorderLayout.SOUTH);

        powerButton.setOnChange(() -> bulbView.setOn(powerButton.isOn()));
    }

    private JComponent buildControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel switchRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        JLabel switchLabel = new JLabel("Power");
        switchLabel.setFont(switchLabel.getFont().deriveFont(Font.BOLD, 14f));
        switchRow.add(switchLabel);
        switchRow.add(powerButton);
        panel.add(switchRow);

        JSlider brightness = new JSlider(10, 100, 100);
        ChangeListener onChange = e -> bulbView.setBrightness(brightness.getValue() / 100f);
        brightness.addChangeListener(onChange);
        JPanel brightnessRow = new JPanel(new BorderLayout(10, 0));
        brightnessRow.add(new JLabel("Brightness"), BorderLayout.WEST);
        brightnessRow.add(brightness, BorderLayout.CENTER);
        brightnessRow.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        panel.add(brightnessRow);

        JPanel colorRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        Color[] presets = {
                new Color(255, 214, 140),
                Color.WHITE,
                new Color(255, 80, 80),
                new Color(80, 200, 120),
                new Color(90, 140, 255),
                new Color(190, 100, 230),
                new Color(255, 150, 50)
        };

        for (Color c : presets)
        {
            JButton swatch = new JButton();
            swatch.setBackground(c);
            swatch.setPreferredSize(new Dimension(28, 28));
            swatch.setFocusPainted(false);
            swatch.setToolTipText("Set bulb color");
            swatch.addActionListener(e -> bulbView.setColor(c));
            colorRow.add(swatch);
        }
        JButton custom = new JButton("Custom...");
        custom.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Pick a bulb color", bulbView.getColor());
            if (chosen != null) bulbView.setColor(chosen);
        });
        colorRow.add(custom);
        panel.add(colorRow);

        return panel;
    }
}

class BulbView extends JPanel
{

    private boolean on = false;
    private Color color = new Color(255, 214, 140);
    private float brightness = 1.0f;

    BulbView()
    {
        setPreferredSize(new Dimension(300, 320));
    }

    void setOn(boolean value)
    {
        on = value;
        repaint();
    }

    void setColor(Color c)
    {
        color = c;
        repaint();
    }

    void setBrightness(float b)
    {
        brightness = b;
        repaint();
    }

    Color getColor()
    {
        return color;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2 - 20;
        int bulbR = Math.min(w, h) / 4;

        if (on)
        {
            float alpha = 0.55f * brightness;
            int glowR = bulbR * 3;
            RadialGradientPaint glow = new RadialGradientPaint(new Point(cx, cy), glowR, new float[]{0f, 1f},
                    new Color[]{
                            new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255)),
                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)
                    });
            g2.setPaint(glow);
            g2.fillOval(cx - glowR, cy - glowR, glowR * 2, glowR * 2);
        }

        Color glass = on ? blendWithBrightness(color, brightness) : new Color(210, 210, 210);
        g2.setColor(glass);
        g2.fillOval(cx - bulbR, cy - bulbR, bulbR * 2, bulbR * 2);
        g2.setColor(new Color(90, 90, 90));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(cx - bulbR, cy - bulbR, bulbR * 2, bulbR * 2);

        int baseW = bulbR;
        int baseH = bulbR / 2;
        int baseX = cx - baseW / 2;
        int baseY = cy + bulbR - 6;
        g2.setColor(new Color(160, 160, 160));
        g2.fillRect(baseX, baseY, baseW, baseH);
        g2.setColor(new Color(100, 100, 100));

        for (int i = 0; i < 4; i++)
        {
            int lineY = baseY + (baseH / 4) * i + 6;
            g2.drawLine(baseX, lineY, baseX + baseW, lineY);
        }

        g2.setColor(new Color(70, 70, 70));
        g2.fillRoundRect(cx - 8, baseY + baseH, 16, 14, 4, 4);

        g2.dispose();
    }

    private Color blendWithBrightness(Color c, float b)
    {
        int r = (int) (c.getRed() * (0.4f + 0.6f * b));
        int g = (int) (c.getGreen() * (0.4f + 0.6f * b));
        int bl = (int) (c.getBlue() * (0.4f + 0.6f * b));
        return new Color(clamp(r), clamp(g), clamp(bl));
    }

    private int clamp(int v)
    {
        return Math.max(0, Math.min(255, v));
    }
}

class PowerButton extends JComponent
{

    private boolean on = false;
    private boolean pressed = false;
    private double glow = 0;
    private Timer timer;
    private Runnable onChange;

    PowerButton()
    {
        setPreferredSize(new Dimension(64, 64));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                pressed = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                setOn(!on);
            }
        });
    }

    void setOnChange(Runnable r)
    {
        onChange = r;
    }

    boolean isOn()
    {
        return on;
    }

    void setOn(boolean value)
    {
        on = value;
        animateTo(on ? 1.0 : 0.0);
        if (onChange != null) onChange.run();
    }

    private void animateTo(double target)
    {
        if (timer != null && timer.isRunning()) timer.stop();

        timer = new Timer(12, null);
        timer.addActionListener(e -> {
            double step = 0.08;
            if (Math.abs(glow - target) < step)
            {
                glow = target;
                timer.stop();
            }
            else {
                glow += target > glow ? step : -step;
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int d = Math.min(w, h) - 6;
        int x = (w - d) / 2;
        int y = (h - d) / 2;

        if (glow > 0)
        {
            int glowD = (int) (d + d * 0.6 * glow);
            int gx = (w - glowD) / 2;
            int gy = (h - glowD) / 2;
            g2.setColor(new Color(255, 214, 140, (int) (120 * glow)));
            g2.fillOval(gx, gy, glowD, glowD);
        }

        Color base = blend(new Color(90, 90, 90), new Color(255, 193, 79), glow);
        if (pressed) base = base.darker();
        g2.setColor(base);
        g2.fillOval(x, y, d, d);
        g2.setColor(new Color(60, 60, 60));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(x, y, d, d);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int cx = w / 2;
        int cy = h / 2;
        int r = d / 4;
        g2.drawArc(cx - r, cy - r + 3, r * 2, r * 2 - 3, -60, 300);
        g2.drawLine(cx, cy - r - 2, cx, cy - 2);

        g2.dispose();
    }

    private Color blend(Color a, Color b, double t)
    {
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bl);
    }
}

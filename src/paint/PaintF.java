package paint;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.*;

public class PaintF extends JFrame
{
    private final DrawingCanva canvas;
    private Color currentColor = Color.BLACK;

    //Cyberpunk palette
    private static final Color BG_DARK = new Color(14,  6, 28);
    private static final Color BG_PANEL = new Color(22, 10, 42);
    private static final Color BG_HEADER = new Color(18,  4, 36);
    private static final Color MAGENTA = new Color(255,  0, 170);
    private static final Color PINK = new Color(255, 60, 172);
    private static final Color NEON_RED = new Color(255, 45,  85);
    private static final Color BTN_BORDER = new Color(160,  0, 160);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(200, 150, 220);
    private static final Color TEXT_DIM = new Color(130,  90, 160);
    private static final Color ACTIVE_TOOL = new Color(255,  0, 170);   // magenta highlight

    private JButton activeToolButton = null;

    public PaintF(int canvasWidth, int canvasHeight)
    {
        setTitle("JanePaint v2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(canvasWidth + 150, canvasHeight + 80);
        setLocationRelativeTo(null);
        setResizable(false);

        //Dark window background
        getContentPane().setBackground(BG_DARK);

        canvas = new DrawingCanva(canvasWidth, canvasHeight);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topPanel.setBackground(BG_HEADER);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, MAGENTA));

        JButton clearBtn = makeTopButton("Clear", NEON_RED);
        JButton colorBtn = makeTopButton("Choose Color", PINK);
        JButton saveBtn = makeTopButton("Save", MAGENTA);

        JLabel sizeLabel = new JLabel("Brush Size:");
        sizeLabel.setForeground(TEXT_SECONDARY);
        sizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JSlider brushSizeSlider = makeCyberpunkSlider();

        clearBtn.addActionListener(e -> canvas.clear());
        colorBtn.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(this, "Pick a color", currentColor);

            if (selected != null)
            {
                currentColor = selected;
                canvas.setColor(currentColor);
                canvas.setErasing(false);
            }
        });

        saveBtn.addActionListener(e -> SavingThis.saveImage(canvas.getImage(), this));
        brushSizeSlider.addChangeListener(e -> canvas.setBrushSize(brushSizeSlider.getValue()));

        topPanel.add(clearBtn);
        topPanel.add(colorBtn);
        topPanel.add(saveBtn);
        topPanel.add(sizeLabel);
        topPanel.add(brushSizeSlider);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(BG_PANEL);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(60, 0, 80)),
                BorderFactory.createEmptyBorder(16, 8, 16, 8)
        ));

        JLabel toolsLabel = makeToolsHeader("TOOLS");

        JButton brushBtn = makeToolButton("\u270F  Brush", DrawingCanva.Tool.BRUSH);
        JButton rectBtn = makeToolButton("\u25AD  Rect", DrawingCanva.Tool.RECT);
        JButton ovalBtn = makeToolButton("\u25CB  Oval", DrawingCanva.Tool.OVAL);
        JButton lineBtn = makeToolButton("\u2571  Line", DrawingCanva.Tool.LINE);
        JButton eraserBtn = new JButton("\u2327  Eraser");

        styleToolButton(eraserBtn);

        eraserBtn.addActionListener(e -> {
            canvas.setColor(Color.WHITE);
            canvas.setErasing(true);
            setActiveButton(eraserBtn);
        });

        // Brush active on startup
        setActiveButton(brushBtn);

        leftPanel.add(toolsLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(brushBtn);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(rectBtn);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(ovalBtn);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(lineBtn);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(eraserBtn);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(canvas, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton makeTopButton(String label, Color accent)
    {
        JButton btn = new JButton(label);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBackground(new Color(35, 10, 55));
        btn.setForeground(TEXT_SECONDARY);
        btn.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 1, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter()
        {

            @Override 
            public void mouseEntered(MouseEvent e)
            {
                btn.setBackground(new Color(55, 10, 75));
                btn.setForeground(accent);
                btn.setBorder(BorderFactory.createLineBorder(accent, 1, true));
            }

            @Override 
            public void mouseExited(MouseEvent e)
            {
                btn.setBackground(new Color(35, 10, 55));
                btn.setForeground(TEXT_SECONDARY);
                btn.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 1, true));
            }
            
        });
        return btn;
    }

    private JButton makeToolButton(String label, DrawingCanva.Tool tool)
    {
        JButton btn = new JButton(label);
        styleToolButton(btn);
        btn.addActionListener(e -> {
            canvas.setTool(tool);
            canvas.setErasing(false);
            setActiveButton(btn);
        });
        return btn;
    }

    private void styleToolButton(JButton btn)
    {

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(124, 38));
        btn.setPreferredSize(new Dimension(124, 38));
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(new Color(35, 10, 55));
        btn.setForeground(TEXT_SECONDARY);
        btn.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 1, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void setActiveButton(JButton btn)
    {
        if (activeToolButton != null)
        {
            activeToolButton.setBackground(new Color(35, 10, 55));
            activeToolButton.setForeground(TEXT_SECONDARY);
            activeToolButton.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 1, true));
        }
        activeToolButton = btn;
        activeToolButton.setBackground(new Color(80, 0, 100));
        activeToolButton.setForeground(MAGENTA);
        activeToolButton.setBorder(BorderFactory.createLineBorder(MAGENTA, 1, true));
    }

    private JLabel makeToolsHeader(String text)
    {
        JLabel lbl = new JLabel(text);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(PINK);
        return lbl;
    }

    private JSlider makeCyberpunkSlider()
    {

        JSlider slider = new JSlider(1, 50, 5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        slider.setBackground(BG_HEADER);
        slider.setForeground(TEXT_DIM);          // tick label color
        slider.setUI(new BasicSliderUI(slider)
        {

            @Override
            public void paintTrack(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle t = trackRect;
                g2.setColor(new Color(60, 0, 80));
                g2.fillRoundRect(t.x, t.y + t.height / 2 - 3, t.width, 6, 4, 4);
                // filled portion in magenta
                int filled = thumbRect.x - t.x;
                g2.setColor(MAGENTA);
                g2.fillRoundRect(t.x, t.y + t.height / 2 - 3, filled, 6, 4, 4);
            }

            @Override
            public void paintThumb(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MAGENTA);
                g2.fillOval(thumbRect.x, thumbRect.y + 2, thumbRect.width - 2, thumbRect.height - 4);
            }
        });
        return slider;
    }
}

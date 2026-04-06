package paint;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.*;

public class PaintF extends JFrame
{
    private final DrawingCanva canvas;
    private Color currentColor = Color.BLACK;

    private static final Color BG_DARK = new Color(14,  6, 28);
    private static final Color BG_PANEL = new Color(22, 10, 42);
    private static final Color BG_HEADER = new Color(18,  4, 36);
    private static final Color MAGENTA = new Color(255,  0, 170);
    private static final Color PINK = new Color(255, 60, 172);
    private static final Color NEON_RED = new Color(255, 45,  85);
    private static final Color BTN_BORDER = new Color(160,  0, 160);
    private static final Color TEXT_SECONDARY = new Color(200, 150, 220);
    private static final Color TEXT_DIM = new Color(130,  90, 160);
    private static final Color DIVIDER = new Color(60,   0,  80);

    //Tracks the active tool button AND active brush button separately
    private JButton activeToolButton = null;
    private JButton activeBrushButton = null;

    public PaintF(int canvasWidth, int canvasHeight)
    {
        setTitle("JanePaint v2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(canvasWidth + 150, canvasHeight + 80);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);

        canvas = new DrawingCanva(canvasWidth, canvasHeight);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topPanel.setBackground(BG_HEADER);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, MAGENTA));

        JButton clearBtn = makeTopButton("Clear", NEON_RED);
        JButton colorBtn = makeTopButton("Choose Color", PINK);
        JButton saveBtn  = makeTopButton("Save", MAGENTA);

        JLabel sizeLabel = new JLabel("Brush Size:");
        sizeLabel.setForeground(TEXT_SECONDARY);
        sizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JSlider brushSizeSlider = makeCyberpunkSlider();

        clearBtn.addActionListener(e -> canvas.clear());
        colorBtn.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(this, "Pick a color", currentColor)
                
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
        leftPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, DIVIDER), BorderFactory.createEmptyBorder(16, 8, 16, 8)
        ));

        leftPanel.add(makeSectionLabel("TOOLS"));
        leftPanel.add(Box.createVerticalStrut(8));

        JButton brushBtn = makeToolButton("\u270F  Brush", DrawingCanva.Tool.BRUSH);
        JButton rectBtn = makeToolButton("\u25AD  Rect",  DrawingCanva.Tool.RECT);
        JButton ovalBtn = makeToolButton("\u25CB  Oval", DrawingCanva.Tool.OVAL);
        JButton lineBtn = makeToolButton("\u2571  Line", DrawingCanva.Tool.LINE);
        JButton eraserBtn = new JButton("\u2327  Eraser");

        styleButton(eraserBtn);
        eraserBtn.addActionListener(e -> {
            canvas.setColor(Color.WHITE);
            canvas.setErasing(true);
            setActiveToolButton(eraserBtn);
        });

        leftPanel.add(brushBtn);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(rectBtn);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(ovalBtn);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(lineBtn);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(eraserBtn);

        //Divider
        leftPanel.add(Box.createVerticalStrut(14));
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(DIVIDER);
        sep.setMaximumSize(new Dimension(130, 2));
        leftPanel.add(sep);
        leftPanel.add(Box.createVerticalStrut(14));

        //Brushes :-D
        leftPanel.add(makeSectionLabel("BRUSHES"));
        leftPanel.add(Box.createVerticalStrut(8));

        JButton roundBtn = makeBrushButton("\u25CF  Round", BrushEngine.BrushType.ROUND);
        JButton squareBtn = makeBrushButton("\u25A0  Square", BrushEngine.BrushType.SQUARE);
        JButton knifeBtn = makeBrushButton("\u2571  Knife", BrushEngine.BrushType.KNIFE);
        JButton sprayBtn = makeBrushButton("\u2237  Spray", BrushEngine.BrushType.SPRAY);
        JButton caliBtn = makeBrushButton("\u270D  Calligraphy", BrushEngine.BrushType.CALLIGRAPHY);

        leftPanel.add(roundBtn);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(squareBtn);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(knifeBtn);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(sprayBtn);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(caliBtn);

        //efaults
        setActiveToolButton(brushBtn);
        setActiveBrushButton(roundBtn);

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
        styleButton(btn);
        btn.addActionListener(e -> {
            canvas.setTool(tool);
            canvas.setErasing(false);
            setActiveToolButton(btn);
        });

        return btn;
    }

    private JButton makeBrushButton(String label, BrushEngine.BrushType type)
    {
        JButton btn = new JButton(label);
        styleButton(btn);
        btn.addActionListener(e -> {
            canvas.setBrushType(type);
            //Auto switch to Brush tool when a brush type is picked
            canvas.setTool(DrawingCanva.Tool.BRUSH);
            canvas.setErasing(false);
            setActiveBrushButton(btn);
        });
        return btn;
    }

    private void styleButton(JButton btn)
    {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(130, 36));
        btn.setPreferredSize(new Dimension(130, 36));
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(new Color(35, 10, 55));
        btn.setForeground(TEXT_SECONDARY);
        btn.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 1, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void setActiveToolButton(JButton btn)
    {
        if (activeToolButton != null)
        {
            deactivateButton(activeToolButton);
        }
        activeToolButton = btn;
        activateButton(btn);
    }

    private void setActiveBrushButton(JButton btn)
    {
        if (activeBrushButton != null)
        {
            deactivateButton(activeBrushButton);
        }

        activeBrushButton = btn;
        activateButton(btn);
    }

    private void activateButton(JButton btn)
    {
        btn.setBackground(new Color(80, 0, 100));
        btn.setForeground(MAGENTA);
        btn.setBorder(BorderFactory.createLineBorder(MAGENTA, 1, true));
    }

    private void deactivateButton(JButton btn)
    {
        btn.setBackground(new Color(35, 10, 55));
        btn.setForeground(TEXT_SECONDARY);
        btn.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 1, true));
    }

    private JLabel makeSectionLabel(String text)
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
        slider.setForeground(TEXT_DIM);
        slider.setUI(new BasicSliderUI(slider)
        {

            @Override
            public void paintTrack(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle t = trackRect;
                g2.setColor(new Color(60, 0, 80));
                g2.fillRoundRect(t.x, t.y + t.height / 2 - 3, t.width, 6, 4, 4);
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

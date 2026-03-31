package paint;

import javax.swing.*;
import java.awt.*;

public class PaintF extends JFrame
{
    private final DrawingCanva canvas;
    private Color currentColor = Color.BLACK;

    private JButton activeToolButton = null;
    private static final Color ACTIVE_COLOR  = new Color(180, 210, 255);
    private static final Color DEFAULT_COLOR = UIManager.getColor("Button.background");

    //Accepts canvasfrom Launcher
    public PaintF(int canvasWidth, int canvasHeight)
    {
        setTitle("JanePaint v2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(canvasWidth + 140, canvasHeight + 80);
        setLocationRelativeTo(null);
        setResizable(false);

        canvas = new DrawingCanva(canvasWidth, canvasHeight);

        JButton clearBtn = new JButton("Clear");
        JButton colorBtn = new JButton("Choose Color");
        JButton saveBtn  = new JButton("Save");

        JSlider brushSizeSlider = new JSlider(1, 50, 5);
        brushSizeSlider.setPaintTicks(true);
        brushSizeSlider.setPaintLabels(true);
        brushSizeSlider.addChangeListener(e ->
                canvas.setBrushSize(brushSizeSlider.getValue())
        );

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

        JPanel topPanel = new JPanel();
        topPanel.add(clearBtn);
        topPanel.add(colorBtn);
        topPanel.add(new JLabel("Brush Size:"));
        topPanel.add(brushSizeSlider);
        topPanel.add(saveBtn);

        //Left
        JButton brushBtn  = makeToolButton("\u270F  Brush",  DrawingCanva.Tool.BRUSH);
        JButton rectBtn   = makeToolButton("\u25AD  Rect",   DrawingCanva.Tool.RECT);
        JButton ovalBtn   = makeToolButton("\u25CB  Oval",   DrawingCanva.Tool.OVAL);
        JButton lineBtn   = makeToolButton("\u2571  Line",   DrawingCanva.Tool.LINE);
        JButton eraserBtn = new JButton("\u2327  Eraser");

        styleToolButton(eraserBtn);
        eraserBtn.addActionListener(e -> {
            canvas.setColor(Color.WHITE);
            canvas.setErasing(true);
            setActiveButton(eraserBtn);
        });

        setActiveButton(brushBtn);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(12, 6, 12, 6));
        leftPanel.setBackground(new Color(240, 240, 240));

        leftPanel.add(makeLabel("TOOLS"));
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(brushBtn);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(rectBtn);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(ovalBtn);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(lineBtn);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(eraserBtn);

        add(topPanel,  BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(canvas,    BorderLayout.CENTER);
        setVisible(true);
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
        btn.setMaximumSize(new Dimension(120, 38));
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
    }

    private void setActiveButton(JButton btn)
    {
        if (activeToolButton != null)
        {
            activeToolButton.setBackground(DEFAULT_COLOR);
            activeToolButton = btn;
            activeToolButton.setBackground(ACTIVE_COLOR);
        }

    }

    private JLabel makeLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(new Color(100, 100, 100));
        return label;
    }
}

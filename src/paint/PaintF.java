package paint;

import javax.swing.*;
import java.awt.*;

public class PaintF extends JFrame
{

    private final DrawingCanva canvas;
    private Color currentColor = Color.BLACK;

    public PaintF()
    {
        setTitle("JanePaint v2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        canvas = new DrawingCanva();

        JButton clearBtn = new JButton("Clear");
        JButton colorBtn = new JButton("Choose Color");
        JButton eraserBtn = new JButton("Eraser");
        //Just got add
        JButton brushBtn = new JButton("Brush");
        JButton rectBtn = new JButton("Rectangle");
        JButton ovalBtn = new JButton("Oval");
        JButton lineBtn = new JButton("Line");
        //plus
        JButton saveBtn = new JButton("Save");

        brushBtn.addActionListener(e -> canvas.setTool(DrawingCanva.Tool.BRUSH));
        rectBtn.addActionListener(e -> canvas.setTool(DrawingCanva.Tool.RECT));
        ovalBtn.addActionListener(e -> canvas.setTool(DrawingCanva.Tool.OVAL));
        lineBtn.addActionListener(e -> canvas.setTool(DrawingCanva.Tool.LINE));

        saveBtn.addActionListener(e -> SavingThis.saveImage(canvas.getImage(), this));

        JSlider brushSizeSlider = new JSlider(1, 50, 5);
        brushSizeSlider.setPaintTicks(true);
        brushSizeSlider.setPaintLabels(true);
        brushSizeSlider.addChangeListener(e ->
                canvas.setBrushSize(brushSizeSlider.getValue())
        );
        clearBtn.addActionListener(e -> canvas.clear());
        colorBtn.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(
                    this, "Pick a color", currentColor
            );
            if (selected != null)
            {
                currentColor = selected;
                canvas.setColor(currentColor);
                canvas.setErasing(false); //this from Drawing Canva
            }
        });
        eraserBtn.addActionListener(e -> canvas.setColor(Color.WHITE)
        );

        JPanel topPanel = new JPanel();
        topPanel.add(eraserBtn);
        topPanel.add(clearBtn);
        topPanel.add(colorBtn);
        topPanel.add(new JLabel("Brush Size:"));
        topPanel.add(brushSizeSlider);
        //plus
        topPanel.add(brushBtn);
        topPanel.add(rectBtn);
        topPanel.add(ovalBtn);
        topPanel.add(lineBtn);

        topPanel.add(saveBtn);

        add(topPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        setVisible(true);
    }
}
//20 lines just got added

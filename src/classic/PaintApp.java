import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PaintApp extends JFrame 
{

    private final Brushes brushes = new Brushes();
    private final Canvas canvas;
    private final Paint paintPanel;
    private final FileSave fileSave;

    private final JLabel statusLabel = new JLabel("Ready");
    private final JLabel coordLabel = new JLabel("");

    public PaintApp() {
        super("untitled - Paint");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        canvas = new Canvas(brushes);
        paintPanel = new Paint();
        fileSave = new FileSave(this);

        paintPanel.setColorChangeListener(new Paint.ColorChangeListener() {

            public void onForegroundChanged(Color c) 
            { 
                canvas.setFgColor(c); 
            }

            public void onBackgroundChanged(Color c) 
            { 
                canvas.setBgColor(c); 
            }
        });

        canvas.setStatusListener((x, y) -> coordLabel.setText(x + ", " + y));

        setJMenuBar(buildMenuBar());
        add(buildToolbar(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(canvas);
        scroll.setBackground(new Color(128, 128, 128));
        scroll.getViewport().setBackground(new Color(128, 128, 128));
        add(scroll, BorderLayout.CENTER);

        add(buildBottomPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JMenuBar buildMenuBar() 
    {
        JMenuBar bar = new JMenuBar();

        // File
        JMenu file = new JMenu("File");
        addItem(file, "New.", KeyEvent.VK_N, e -> newFile());
        addItem(file, "Open.", KeyEvent.VK_O, e -> openFile());
        file.addSeparator();
        addItem(file, "Save.", KeyEvent.VK_S, e -> fileSave.saveFile(canvas.getImage()));
        addItem(file, "Save As.", -1, e -> fileSave.saveFileAs(canvas.getImage()));
        file.addSeparator();
        addItem(file, "Exit", -1, e -> System.exit(0));

        // Image
        JMenu image = new JMenu("Image.");
        addItem(image, "Canvas Size.", -1, e -> resizeDialog());

        bar.add(file);
        bar.add(new JMenu("Edit."));
        bar.add(new JMenu("View."));
        bar.add(image);
        bar.add(new JMenu("Options."));
        bar.add(new JMenu("Help."));
        return bar;
    }

    private JToolBar buildToolbar() 
    {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setBackground(new Color(192, 192, 192));
        tb.setBorder(new BevelBorder(BevelBorder.RAISED));

        ButtonGroup bg = new ButtonGroup();
        addToolButton(tb, bg, "✏",  "Pencil", () -> brushes.setTool(Brushes.Tool.PENCIL));
        addToolButton(tb, bg, "🖌",  "Brush", () -> brushes.setTool(Brushes.Tool.BRUSH));
        addToolButton(tb, bg, "⬛", "Eraser", () -> brushes.setTool(Brushes.Tool.ERASER));
        addToolButton(tb, bg, "🪣", "Fill", () -> brushes.setTool(Brushes.Tool.FILL));
        tb.addSeparator();
        addToolButton(tb, bg, "╱",  "Line", () -> brushes.setTool(Brushes.Tool.LINE));
        addToolButton(tb, bg, "▭",  "Rectangle", () -> brushes.setTool(Brushes.Tool.RECT));
        addToolButton(tb, bg, "▬",  "Filled Rect", () -> brushes.setTool(Brushes.Tool.FILLED_RECT));
        addToolButton(tb, bg, "◯",  "Ellipse", () -> brushes.setTool(Brushes.Tool.ELLIPSE));
        addToolButton(tb, bg, "⬤",  "Filled Ellipse", () -> brushes.setTool(Brushes.Tool.FILLED_ELLIPSE));
        tb.addSeparator();

        // brush size
        tb.add(new JLabel(" Size: "));
        SpinnerNumberModel sizeModel = new SpinnerNumberModel(1, 1, 20, 1);
        JSpinner sizeSpinner = new JSpinner(sizeModel);
        sizeSpinner.setMaximumSize(new Dimension(55, 28));
        sizeSpinner.setPreferredSize(new Dimension(55, 28));
        sizeSpinner.addChangeListener(e -> brushes.setBrushSize((int) sizeSpinner.getValue()));
        tb.add(sizeSpinner);

        return tb;
    }

    private void addToolButton(JToolBar tb, ButtonGroup bg, String icon, String tip, Runnable action) 
    {
        JToggleButton btn = new JToggleButton(icon);
        btn.setToolTipText(tip);
        btn.setPreferredSize(new Dimension(32, 28));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(192, 192, 192));
        btn.addActionListener(e -> action.run());
        bg.add(btn);
        tb.add(btn);

        // select pencil by default
        if (tip.equals("Pencil")) btn.setSelected(true);
    }

    private JPanel buildBottomPanel() 
    {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(192, 192, 192));
        bottom.setBorder(new BevelBorder(BevelBorder.RAISED));

        paintPanel.setPreferredSize(new Dimension(500, 48));
        bottom.add(paintPanel, BorderLayout.CENTER);

        JPanel status = new JPanel(new BorderLayout());
        status.setBackground(new Color(192, 192, 192));
        status.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 0));
        coordLabel.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 6));
        coordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        status.add(statusLabel, BorderLayout.WEST);
        status.add(coordLabel, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(192, 192, 192));
        wrapper.add(bottom, BorderLayout.CENTER);
        wrapper.add(status, BorderLayout.SOUTH);
        return wrapper;
    }

    private void addItem(JMenu menu, String label, int key, ActionListener action) 
    {
        JMenuItem item = new JMenuItem(label);

        if (key != -1) item.setAccelerator(KeyStroke.getKeyStroke(key, InputEvent.CTRL_DOWN_MASK));
        item.addActionListener(action);
        menu.add(item);
    }

    private void newFile() 
    {
        int r = JOptionPane.showConfirmDialog(this, "Discard current drawing?", "New", JOptionPane.YES_NO_OPTION);

        if (r == JOptionPane.YES_OPTION) 
        {
            canvas.newCanvas();
            fileSave.resetFile();
            statusLabel.setText("Ready");
        }
    }

    private void openFile() 
    {
        java.awt.image.BufferedImage img = fileSave.openFile();

        if (img != null) 
        {
            canvas.loadImage(img);
            statusLabel.setText("Opened");
        }
    }

    private void resizeDialog() 
    {
        JTextField wf = new JTextField(String.valueOf(canvas.getWidth()), 5);
        JTextField hf = new JTextField(String.valueOf(canvas.getHeight()), 5);

        JPanel p = new JPanel();
        p.add(new JLabel("Width:")); p.add(wf);
        p.add(new JLabel("Height:")); p.add(hf);
        int r = JOptionPane.showConfirmDialog(this, p, "Canvas Size", JOptionPane.OK_CANCEL_OPTION);

        if (r == JOptionPane.OK_OPTION) 
        {
            try {
                int w = Integer.parseInt(wf.getText().trim());
                int h = Integer.parseInt(hf.getText().trim());
                if (w > 0 && h > 0) canvas.resizeCanvas(w, h);
            } 
            catch (NumberFormatException ignored) 
            {
                
            }
        }
    }
}

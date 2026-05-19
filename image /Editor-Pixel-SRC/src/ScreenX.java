package candy_rush;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.imageio.ImageIO;

public class ScreenX extends JFrame
{

    private BufferedImage image;
    private final Deque<BufferedImage> undoStack = new ArrayDeque<>();
    private final Deque<BufferedImage> redoStack = new ArrayDeque<>();

    private static final int MAX_HISTORY = 30;

    private double zoomFactor = 1.0;
    private final JLabel imageLabel = new JLabel(placeholderIcon());
    private final JLabel statusLabel = new JLabel("No image loaded");
    private JMenuItem undoItem, redoItem;

    public ScreenX()
    {
        setTitle("ScreenX — Java Image Editor");
        setSize(960, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane scroll = new JScrollPane(imageLabel);
        scroll.getViewport().setBackground(new Color(40, 40, 40));
        add(scroll, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(180, 180, 180)));
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

        createMenu();
        createToolBar();

        setVisible(true);
    }

    private void createMenu()
    {
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        addItem(fileMenu, "Open…", KeyEvent.VK_O, e -> openImage());
        addItem(fileMenu, "Save…", KeyEvent.VK_S, e -> saveImage());
        fileMenu.addSeparator();
        addItem(fileMenu, "Revert", -1, e -> revert());

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        undoItem = addItem(editMenu, "Undo", KeyEvent.VK_Z, e -> undo());
        redoItem = addItem(editMenu, "Redo", KeyEvent.VK_Y, e -> redo());
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);

        JMenu viewMenu = new JMenu("View");
        addItem(viewMenu, "Zoom In  (+)", KeyEvent.VK_EQUALS, e -> zoom(1.25));
        addItem(viewMenu, "Zoom Out (−)", KeyEvent.VK_MINUS,  e -> zoom(0.8));
        addItem(viewMenu, "Fit to Window", -1, e -> fitToWindow());

        addItem(viewMenu, "Reset Zoom", KeyEvent.VK_0, e -> { 
            zoomFactor = 1.0; refreshDisplay(); 
        });

        JMenu fx = new JMenu("Effects");
        fx.setMnemonic('X');

        JMenu colorMenu = new JMenu("Color");
        addEffectItem(colorMenu, "Grayscale", () -> push(Effex.toGrayscale(image)));
        addEffectItem(colorMenu, "Invert", () -> push(Effex.invert(image)));
        addEffectItem(colorMenu, "Sepia", () -> push(Effex.sepia(image)));
        addEffectItem(colorMenu, "Warm Tone", () -> push(Effex.warmth(image)));
        addEffectItem(colorMenu, "Cool Tone", () -> push(Effex.cool(image)));
        addEffectItem(colorMenu, "Posterize…", this::doPosterize);
        addEffectItem(colorMenu, "Solarize", () -> push(Effex.solarize(image, 128)));
        fx.add(colorMenu);

        JMenu lightMenu = new JMenu("Light & Tone");
        addEffectItem(lightMenu, "Brightness / Contrast…", this::doBrightnessContrast);
        addEffectItem(lightMenu, "Vignette", () -> push(Effex.vignette(image, 0.85f)));
        lightMenu.addSeparator();
        addEffectItem(lightMenu, "Color Grading (Curves)…", this::openColorGradeWindow);
        fx.add(lightMenu);

        JMenu blurMenu = new JMenu("Blur & Sharpen");
        addEffectItem(blurMenu, "Blur (light)", () -> push(Effex.blur(image, 1)));
        addEffectItem(blurMenu, "Blur (medium)", () -> push(Effex.blur(image, 3)));
        addEffectItem(blurMenu, "Blur (heavy)", () -> push(Effex.blur(image, 6)));
        addEffectItem(blurMenu, "Sharpen", () -> push(Effex.sharpen(image)));
        fx.add(blurMenu);

        JMenu edgeMenu = new JMenu("Edge & Texture");
        addEffectItem(edgeMenu, "Edge Detect", () -> push(Effex.edgeDetect(image)));
        addEffectItem(edgeMenu, "Emboss", () -> push(Effex.emboss(image)));
        addEffectItem(edgeMenu, "Pixelate…", this::doPixelate);
        fx.add(edgeMenu);

        JMenu geoMenu = new JMenu("Geometry");
        addEffectItem(geoMenu, "Flip Horizontal", () -> push(Effex.flipHorizontal(image)));
        addEffectItem(geoMenu, "Flip Vertical", () -> push(Effex.flipVertical(image)));
        addEffectItem(geoMenu, "Rotate 90° CW", () -> push(Effex.rotate90(image)));
        addEffectItem(geoMenu, "Rotate 90° CCW", () -> push(Effex.rotate90CCW(image)));
        fx.add(geoMenu);

        bar.add(fileMenu);
        bar.add(editMenu);
        bar.add(viewMenu);
        bar.add(fx);
        setJMenuBar(bar);
    }

    private void createToolBar()
    {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        tb.add(toolButton("Open", e -> openImage()));
        tb.add(toolButton("Save", e -> saveImage()));
        tb.addSeparator();
        tb.add(toolButton("Undo", e -> undo()));
        tb.add(toolButton("Redo", e -> redo()));
        tb.addSeparator();
        tb.add(toolButton("Zoom +", e -> zoom(1.25)));
        tb.add(toolButton("Zoom −", e -> zoom(0.8)));
        tb.add(toolButton("Fit", e -> fitToWindow()));
        tb.addSeparator();
        tb.add(toolButton("Grayscale", e -> applyIfLoaded(() -> push(Effex.toGrayscale(image)))));
        tb.add(toolButton("Invert", e -> applyIfLoaded(() -> push(Effex.invert(image)))));
        tb.add(toolButton("Sepia", e -> applyIfLoaded(() -> push(Effex.sepia(image)))));
        tb.add(toolButton("Sharpen", e -> applyIfLoaded(() -> push(Effex.sharpen(image)))));
        tb.add(toolButton("Blur", e -> applyIfLoaded(() -> push(Effex.blur(image, 2)))));
        tb.add(toolButton("Edge", e -> applyIfLoaded(() -> push(Effex.edgeDetect(image)))));
        tb.add(toolButton("Vignette", e -> applyIfLoaded(() -> push(Effex.vignette(image, 0.85f)))));

        add(tb, BorderLayout.NORTH);
    }

    private BufferedImage originalImage;

    private void openImage()
    {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            try {
                BufferedImage loaded = ImageIO.read(chooser.getSelectedFile());

                if (loaded == null) throw new Exception("Unsupported format");
                undoStack.clear();
                redoStack.clear();
                image = originalImage = loaded;
                zoomFactor = 1.0;
                refreshDisplay();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this,
                "Could not open image:\n" + ex.getMessage(), "Open Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveImage()
    {
        if (image == null) 
        {
            warnNoImage(); return; 
        }

        JFileChooser chooser = new JFileChooser();

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File file = chooser.getSelectedFile();
            SavingThis.save(image, file);
            status("Saved → " + file.getName());
        }
    }

    private void revert()
    {
        if (originalImage == null) return;
        pushUndo(image);
        image = originalImage;
        refreshDisplay();
        status("Reverted to original");
    }

    private void push(BufferedImage newImage)
    {
        if (image == null || newImage == null) return;

        pushUndo(image);
        redoStack.clear();
        image = newImage;
        refreshDisplay();
    }

    private void pushUndo(BufferedImage snap)
    {
        undoStack.push(snap);
        if (undoStack.size() > MAX_HISTORY) undoStack.pollLast();
        updateUndoRedo();
    }

    private void undo()
    {
        if (undoStack.isEmpty()) return;

        redoStack.push(image);
        image = undoStack.pop();
        refreshDisplay();
        updateUndoRedo();
        status("Undo");
    }

    private void redo()
    {
        if (redoStack.isEmpty()) return;

        undoStack.push(image);
        image = redoStack.pop();
        refreshDisplay();
        updateUndoRedo();
        status("Redo");
    }

    private void updateUndoRedo()
    {
        undoItem.setEnabled(!undoStack.isEmpty());
        redoItem.setEnabled(!redoStack.isEmpty());
    }

    private void zoom(double factor)
    {
        zoomFactor *= factor;
        zoomFactor = Math.max(0.05, Math.min(zoomFactor, 20.0));
        refreshDisplay();
    }

    private void fitToWindow()
    {
        if (image == null) return;
        Dimension pane = ((JScrollPane) imageLabel.getParent().getParent()).getViewport().getSize();
        double scaleX = pane.getWidth() / (double) image.getWidth();
        double scaleY = pane.getHeight() / (double) image.getHeight();
        zoomFactor = Math.min(scaleX, scaleY);
        refreshDisplay();
    }

    private void refreshDisplay()
    {
        if (image == null) return;
        int sw = (int)(image.getWidth() * zoomFactor);
        int sh = (int)(image.getHeight() * zoomFactor);

        Image scaled = image.getScaledInstance(sw, sh, Image.SCALE_FAST);
        imageLabel.setIcon(new ImageIcon(scaled));

        status(image.getWidth() + " × " + image.getHeight() + " px   |   Zoom: "
                + String.format("%.0f%%", zoomFactor * 100) + "   |   Undo: " + undoStack.size());
    }


    private void doBrightnessContrast()
    {
        if (image == null)
        {
            warnNoImage();
            return;
        }

        JSlider bright = new JSlider(-255, 255, 0);
        JSlider cont = new JSlider(-255, 255, 0);
        bright.setMajorTickSpacing(128);
        bright.setPaintLabels(true);
        cont.setMajorTickSpacing(128);
        cont.setPaintLabels(true);

        JPanel panel = labeledPanel("Brightness:", bright, "Contrast:", cont);

        int result = JOptionPane.showConfirmDialog(this, panel, "Brightness / Contrast", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) push(Effex.brightnessContrast(image, bright.getValue(), cont.getValue()));
    }

    private void doPixelate()
    {
        if (image == null)
        {
            warnNoImage();
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Block size (pixels):", "10");
        if (input == null) return;

        try {
            int size = Integer.parseInt(input.trim());
            if (size < 1) throw new NumberFormatException();
            push(Effex.pixelate(image, size));
        }

        catch (NumberFormatException ex)
        {

            JOptionPane.showMessageDialog(this, "Please enter a positive integer.");
        }
    }

    private void doPosterize()
    {
        if (image == null) { warnNoImage(); return; }
        String input = JOptionPane.showInputDialog(this, "Number of color levels (2–16):", "4");
        if (input == null) return;

        try {
            int levels = Integer.parseInt(input.trim());
            if (levels < 2 || levels > 16) throw new NumberFormatException();
            push(Effex.posterize(image, levels));
        }

        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Please enter a number between 2 and 16.");
        }
    }

    private void applyIfLoaded(Runnable r)
    {
        if (image == null)
        {
            warnNoImage();
            return;
        }
        r.run();
    }

    private void addEffectItem(JMenu menu, String label, Runnable action)
    {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(e -> applyIfLoaded(action));
        menu.add(item);
    }

    private JMenuItem addItem(JMenu menu, String label, int key, ActionListener al)
    {
        JMenuItem item = new JMenuItem(label);
        if (key != -1)
            item.setAccelerator(KeyStroke.getKeyStroke(key, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        item.addActionListener(al);
        menu.add(item);
        return item;
    }

    private JButton toolButton(String label, ActionListener al)
    {
        JButton btn = new JButton(label);
        btn.setFocusable(false);
        btn.addActionListener(al);
        return btn;
    }

    private JPanel labeledPanel(String l1, JComponent c1, String l2, JComponent c2)
    {
        JPanel p = new JPanel(new GridLayout(4, 1, 4, 4));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        p.add(new JLabel(l1));
        p.add(c1);
        p.add(new JLabel(l2));
        p.add(c2);
        return p;
    }

    private ColorGradeWindow colorGradeWindow = null;

    private void openColorGradeWindow()
    {
        if (image == null)
        {
            warnNoImage();
            return;
        }


        if (colorGradeWindow != null && colorGradeWindow.isVisible())
        {
            colorGradeWindow.toFront();
            return;
        }

        final BufferedImage snapshot = image;

        colorGradeWindow = new ColorGradeWindow(snapshot, this::previewImage, () -> {
            BufferedImage result = colorGradeWindow.getLastPreview();
            if (result != null) push(result);
            else previewFn_restore(snapshot);
        }
        );
    }

    private void previewImage(BufferedImage preview)
    {
        if (preview == null) return;
        int sw = (int)(preview.getWidth() * zoomFactor);
        int sh = (int)(preview.getHeight() * zoomFactor);
        imageLabel.setIcon(new ImageIcon(preview.getScaledInstance(sw, sh, Image.SCALE_FAST)));
        status("Color Grade preview — click Apply or Cancel");
    }

    private void previewFn_restore(BufferedImage original)
    {
        image = original;
        refreshDisplay();
    }

    private void warnNoImage()
    {
        JOptionPane.showMessageDialog(this, "No image is open.", "ScreenX", JOptionPane.INFORMATION_MESSAGE);
    }

    private void status(String msg)
    {
        statusLabel.setText(msg);
    }

    private static ImageIcon placeholderIcon()
    {
        BufferedImage ph = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = ph.createGraphics();
        g.setColor(new Color(60, 60, 60));
        g.fillRect(0, 0, 400, 300);
        g.setColor(new Color(120, 120, 120));
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        String msg = "Open an image to get started";
        g.drawString(msg, (400 - fm.stringWidth(msg)) / 2, 158);
        g.dispose();
        return new ImageIcon(ph);
    }
}
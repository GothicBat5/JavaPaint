package candy_rush;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import javax.imageio.ImageIO;

public class ScreenX extends JFrame
{
    private BufferedImage image;
    private BufferedImage originalImage;

    private final Deque<BufferedImage> undoStack = new ArrayDeque<>();
    private final Deque<BufferedImage> redoStack = new ArrayDeque<>();
    private static final int MAX_HISTORY = 30;

    private double zoomFactor = 1.0;
    private boolean cropMode  = false;

    private final JLabel statusLabel = new JLabel("No image loaded");
    private JMenuItem undoItem, redoItem;
    private JToggleButton cropBtn;
    final private JScrollPane scrollPane;
    final private CropCanvas cropCanvas;
    final private JPanel cropBar;
    final private JPanel centerStack;

    private final JLabel imageLabel = new JLabel(placeholderIcon());

    private ColorGradeWindow colorGradeWindow = null;

    public ScreenX()
    {
        setTitle("ScreenX");
        setSize(960, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scrollPane = new JScrollPane(imageLabel);
        scrollPane.getViewport().setBackground(new Color(40, 40, 40));

        cropCanvas = new CropCanvas(this::commitCrop);

        centerStack = new JPanel(new CardLayout());
        centerStack.add(scrollPane,"view");
        centerStack.add(cropCanvas,"crop");
        add(centerStack, BorderLayout.CENTER);

        cropBar = buildCropBar();
        cropBar.setVisible(false);
        add(cropBar, BorderLayout.EAST);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(180, 180, 180)));
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

        createMenu();
        createToolBar();

        setVisible(true);
    }

    private JPanel buildCropBar()
    {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(45, 45, 45));
        p.setBorder(new EmptyBorder(10, 8, 10, 8));
        p.setPreferredSize(new Dimension(110, 0));

        JLabel title = new JLabel("Crop");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyBtn = sideBtn("Apply Crop");
        JButton cancelBtn = sideBtn("Cancel");

        applyBtn.addActionListener(e -> cropCanvas.applyCrop());
        cancelBtn.addActionListener(e -> exitCropMode());

        JLabel hint = new JLabel("<html><center>Drag to<br>select area</center></html>");
        hint.setForeground(new Color(140, 140, 140));
        hint.setFont(new Font("SansSerif", Font.PLAIN, 10));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(title);
        p.add(Box.createVerticalStrut(12));
        p.add(applyBtn);
        p.add(Box.createVerticalStrut(6));
        p.add(cancelBtn);
        p.add(Box.createVerticalGlue());
        p.add(hint);

        return p;
    }

    private JButton sideBtn(String label)
    {
        JButton b = new JButton(label);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(95, 28));
        b.setFocusPainted(false);
        return b;
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
        undoItem = addItem(editMenu,"Undo", KeyEvent.VK_Z, e -> undo());
        redoItem = addItem(editMenu,"Redo", KeyEvent.VK_Y, e -> redo());
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);

        JMenu viewMenu = new JMenu("View");
        addItem(viewMenu, "Zoom In", KeyEvent.VK_EQUALS, e -> zoom(1.25));
        addItem(viewMenu, "Zoom Out", KeyEvent.VK_MINUS, e -> zoom(0.8));
        addItem(viewMenu, "Fit to Window", -1, e -> fitToWindow());
        addItem(viewMenu, "Reset Zoom", KeyEvent.VK_0, e -> { zoomFactor = 1.0; refreshDisplay(); });

        JMenu fx = new JMenu("Effects");
        fx.setMnemonic('X');

        JMenu colorMenu = new JMenu("Color");
        addEffectItem(colorMenu,"Grayscale", () -> push(Effex.toGrayscale(image)));
        addEffectItem(colorMenu,"Invert", () -> push(Effex.invert(image)));
        addEffectItem(colorMenu,"Sepia", () -> push(Effex.sepia(image)));
        addEffectItem(colorMenu,"Warm Tone", () -> push(Effex.warmth(image)));
        addEffectItem(colorMenu,"Cool Tone", () -> push(Effex.cool(image)));
        addEffectItem(colorMenu,"Posterize…", this::doPosterize);
        addEffectItem(colorMenu,"Solarize…", this::doSolarize);
        fx.add(colorMenu);

        JMenu lightMenu = new JMenu("Light & Tone");
        addEffectItem(lightMenu,"Brightness / Contrast…", this::doBrightnessContrast);
        addEffectItem(lightMenu,"Vignette…", this::doVignette);
        lightMenu.addSeparator();
        addEffectItem(lightMenu, "Color Grading (Curves)…", this::openColorGradeWindow);
        fx.add(lightMenu);

        JMenu blurMenu = new JMenu("Blur & Sharpen");
        addEffectItem(blurMenu,"Blur…", this::doBlur);
        addEffectItem(blurMenu,"Sharpen", () -> push(Effex.sharpen(image)));
        fx.add(blurMenu);

        JMenu edgeMenu = new JMenu("Edge & Texture");
        addEffectItem(edgeMenu,"Edge Detect", () -> push(Effex.edgeDetect(image)));
        addEffectItem(edgeMenu,"Emboss", () -> push(Effex.emboss(image)));
        addEffectItem(edgeMenu,"Pixelate…", this::doPixelate);
        fx.add(edgeMenu);

        JMenu geoMenu = new JMenu("Geometry");
        addEffectItem(geoMenu, "Crop", this::enterCropMode);
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

        tb.add(toolButton("Open",e -> openImage()));
        tb.add(toolButton("Save",e -> saveImage()));
        tb.addSeparator();
        tb.add(toolButton("Undo",e -> undo()));
        tb.add(toolButton("Redo",e -> redo()));
        tb.addSeparator();
        tb.add(toolButton("Zoom +",e -> zoom(1.25)));
        tb.add(toolButton("Zoom −",e -> zoom(0.8)));
        tb.add(toolButton("Fit",e -> fitToWindow()));
        tb.addSeparator();

        cropBtn = new JToggleButton("Crop");
        cropBtn.setFocusable(false);
        cropBtn.addActionListener(e -> {
            if (cropBtn.isSelected()) enterCropMode();
            else exitCropMode();
        });
        tb.add(cropBtn);
        tb.addSeparator();

        tb.add(toolButton("Grayscale",e -> applyIfLoaded(() -> push(Effex.toGrayscale(image)))));
        tb.add(toolButton("Invert",e -> applyIfLoaded(() -> push(Effex.invert(image)))));
        tb.add(toolButton("Sepia",e -> applyIfLoaded(() -> push(Effex.sepia(image)))));
        tb.add(toolButton("B/C…",e -> applyIfLoaded(this::doBrightnessContrast)));
        tb.add(toolButton("Blur…",e -> applyIfLoaded(this::doBlur)));
        tb.add(toolButton("Sharpen",e -> applyIfLoaded(() -> push(Effex.sharpen(image)))));
        tb.add(toolButton("Edge",e -> applyIfLoaded(() -> push(Effex.edgeDetect(image)))));
        tb.add(toolButton("Vignette…",e -> applyIfLoaded(this::doVignette)));

        add(tb, BorderLayout.NORTH);
    }

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
                exitCropMode();
                image = originalImage = loaded;
                zoomFactor = 1.0;
                refreshDisplay();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this, "Could not open:\n" + ex.getMessage(),
                        "Open Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveImage()
    {
        if (image == null) { warnNoImage(); return; }
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            SavingThis.save(image, chooser.getSelectedFile());
            status("Saved → " + chooser.getSelectedFile().getName());
        }
    }

    private void revert()
    {
        if (originalImage == null) return;
        pushUndo(image);
        image = originalImage;
        exitCropMode();
        refreshDisplay();
        status("Reverted to original");
    }

    private void push(BufferedImage next)
    {
        if (image == null || next == null) return;
        pushUndo(image);
        redoStack.clear();
        image = next;
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
        Dimension pane = scrollPane.getViewport().getSize();
        zoomFactor = Math.min(pane.getWidth() / (double) image.getWidth(),
                pane.getHeight() / (double) image.getHeight());
        refreshDisplay();
    }

    private void refreshDisplay()
    {
        if (image == null) return;
        int sw = (int)(image.getWidth() * zoomFactor);
        int sh = (int)(image.getHeight() * zoomFactor);
        imageLabel.setIcon(new ImageIcon(image.getScaledInstance(sw, sh, Image.SCALE_FAST)));
        if (cropMode) cropCanvas.setImage(image, zoomFactor);
        status(image.getWidth() + " × " + image.getHeight() + " px  |  Zoom: "
                + String.format("%.0f%%", zoomFactor * 100)
                + "  |  Undo: " + undoStack.size());
    }

    private void enterCropMode()
    {
        if (image == null) 
        { 
            warnNoImage(); 
            if (cropBtn != null) cropBtn.setSelected(false); 
            return; 
        }
        cropMode = true;
        cropCanvas.setImage(image, zoomFactor);
        ((CardLayout) centerStack.getLayout()).show(centerStack, "crop");
        cropBar.setVisible(true);
        if (cropBtn != null) cropBtn.setSelected(true);
        status("Crop — drag to select, then Apply Crop");
    }

    private void exitCropMode()
    {
        cropMode = false;
        cropCanvas.cancelSel();
        ((CardLayout) centerStack.getLayout()).show(centerStack, "view");
        cropBar.setVisible(false);
        if (cropBtn != null) cropBtn.setSelected(false);
        if (image != null) refreshDisplay();
    }

    private void commitCrop(java.awt.Rectangle r)
    {
        if (image == null) return;
        BufferedImage sub = image.getSubimage(r.x, r.y, r.width, r.height);
        BufferedImage copy = new BufferedImage(r.width, r.height, BufferedImage.TYPE_INT_ARGB);
        copy.createGraphics().drawImage(sub, 0, 0, null);
        exitCropMode();
        push(copy);
        status("Cropped to " + r.width + " × " + r.height);
    }

    private void doBrightnessContrast()
    {
        if (image == null) { warnNoImage(); return; }
        SliderPreview dlg = new SliderPreview(
                this, "Brightness / Contrast", image,
                List.of(new SliderPreview.Param("Brightness",-255, 255, 0),
                        new SliderPreview.Param("Contrast",-255, 255, 0)),
                v -> Effex.brightnessContrast(image, v[0], v[1]));
        dlg.setVisible(true);
        if (dlg.isConfirmed())
        {
            int[] v = dlg.values();
            push(Effex.brightnessContrast(image, v[0], v[1]));
        }
    }

    private void doBlur()
    {
        if (image == null) { warnNoImage(); return; }
        SliderPreview dlg = new SliderPreview(
                this, "Blur", image,
                List.of(new SliderPreview.Param("Radius", 1, 15, 2)),
                v -> Effex.blur(image, v[0]));
        dlg.setVisible(true);
        if (dlg.isConfirmed()) push(Effex.blur(image, dlg.values()[0]));
    }

    private void doPixelate()
    {
        if (image == null)
        {
            warnNoImage();
            return;
        }
        SliderPreview dlg = new SliderPreview(this, "Pixelate", image,
                List.of(new SliderPreview.Param("Block Size", 2, 80, 10)),
                v -> Effex.pixelate(image, v[0]));
        dlg.setVisible(true);
        if (dlg.isConfirmed()) push(Effex.pixelate(image, dlg.values()[0]));
    }

    private void doPosterize()
    {
        if (image == null) { warnNoImage(); return; }
        SliderPreview dlg = new SliderPreview(this, "Posterize", image,
                List.of(new SliderPreview.Param("Levels", 2, 16, 4)),
                v -> Effex.posterize(image, v[0]));
        dlg.setVisible(true);
        if (dlg.isConfirmed()) push(Effex.posterize(image, dlg.values()[0]));
    }

    private void doSolarize()
    {
        if (image == null) { warnNoImage(); return; }
        SliderPreview dlg = new SliderPreview(this, "Solarize", image,
                List.of(new SliderPreview.Param("Threshold", 0, 255, 128)),
                v -> Effex.solarize(image, v[0]));
        dlg.setVisible(true);
        if (dlg.isConfirmed()) push(Effex.solarize(image, dlg.values()[0]));
    }

    private void doVignette()
    {
        if (image == null) { warnNoImage(); return; }
        SliderPreview dlg = new SliderPreview(this, "Vignette", image,
                List.of(new SliderPreview.Param("Strength", 0, 100, 85)),
                v -> Effex.vignette(image, v[0] / 100.0f));
        dlg.setVisible(true);
        if (dlg.isConfirmed()) push(Effex.vignette(image, dlg.values()[0] / 100.0f));
    }

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
        colorGradeWindow = new ColorGradeWindow(
                snapshot,
                this::previewImage,
                () -> {
                    BufferedImage result = colorGradeWindow.getLastPreview();
                    if (result != null) push(result);
                    else { image = snapshot; refreshDisplay(); }
                });
    }

    private void previewImage(BufferedImage preview)
    {
        if (preview == null) return;
        int sw = (int)(preview.getWidth()  * zoomFactor);
        int sh = (int)(preview.getHeight() * zoomFactor);
        imageLabel.setIcon(new ImageIcon(preview.getScaledInstance(sw, sh, Image.SCALE_FAST)));
        status("Color Grade preview — Apply or Cancel");
    }

    private void applyIfLoaded(Runnable r)
    {
        if (image == null) { warnNoImage(); return; }
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
            item.setAccelerator(KeyStroke.getKeyStroke(key,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
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

    private void warnNoImage()
    {
        JOptionPane.showMessageDialog(this, "No image is open.", "ScreenX",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void status(String msg) { statusLabel.setText(msg); }

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

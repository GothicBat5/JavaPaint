import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageFrame extends JFrame {

    private static final Color BG_DARK = new Color(13, 13, 18);
    private static final Color BG_PANEL = new Color(20, 20, 28);
    private static final Color BG_CARD = new Color(28, 28, 38);
    private static final Color ACCENT = new Color(108, 92, 231);
    private static final Color ACCENT_HOVER = new Color(130, 116, 245);
    private static final Color TEXT_PRIMARY = new Color(230, 228, 255);
    private static final Color TEXT_MUTED = new Color(120, 118, 150);
    private static final Color BORDER = new Color(45, 44, 65);

    private BufferedImage currentImage;
    private String asciiText;
    private boolean showingOriginal = false;

    private JLabel statusLabel;
    private JLabel fileNameLabel;
    private JTextArea asciiArea;
    private ZoomaX imagePanel;
    private CardLayout viewCards;
    private JPanel viewContainer;
    private JButton toggleBtn;
    private JButton openBtn;
    private JPanel dropZone;

    public ImageFrame() 
    {
        setTitle("ASCII Art Converter");
        setSize(1100, 780);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));

        buildUI();
        setVisible(true);
    }

    private void buildUI() 
    {
        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildSidebar() 
    {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(BG_PANEL);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));
        sidebar.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PANEL);
        header.setBorder(new EmptyBorder(24, 20, 20, 20));

        JLabel logo = new JLabel("ASCII ART");
        logo.setFont(new Font("Monospaced", Font.BOLD, 15));
        logo.setForeground(ACCENT);
        header.add(logo, BorderLayout.NORTH);

        JLabel sub = new JLabel("Image Converter");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(TEXT_MUTED);
        header.add(sub, BorderLayout.SOUTH);

        sidebar.add(header, BorderLayout.NORTH);

        JPanel controls = new JPanel();
        controls.setBackground(BG_PANEL);
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBorder(new EmptyBorder(0, 16, 16, 16));

        openBtn = buildPrimaryButton("Open Image", "⌘ O");
        openBtn.addActionListener(e -> openImage());
        controls.add(openBtn);
        controls.add(Box.createVerticalStrut(12));

        toggleBtn = buildSecondaryButton("View: ASCII Art", "⇄");
        toggleBtn.setEnabled(false);
        toggleBtn.addActionListener(e -> toggleView());
        controls.add(toggleBtn);
        controls.add(Box.createVerticalStrut(24));

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBackground(BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        controls.add(sep);
        controls.add(Box.createVerticalStrut(20));

        controls.add(buildSectionLabel("FILE INFO"));
        controls.add(Box.createVerticalStrut(8));
        fileNameLabel = buildInfoLabel("No file loaded");
        controls.add(fileNameLabel);

        controls.add(Box.createVerticalStrut(20));
        controls.add(buildSectionLabel("TIPS"));
        controls.add(Box.createVerticalStrut(8));
        controls.add(buildInfoLabel("Scroll to zoom image"));
        controls.add(Box.createVerticalStrut(4));
        controls.add(buildInfoLabel("Drag to pan image"));
        controls.add(Box.createVerticalStrut(4));
        controls.add(buildInfoLabel("Toggle for before/after"));

        sidebar.add(controls, BorderLayout.CENTER);

        JLabel version = new JLabel("v2.0");
        version.setFont(new Font("Monospaced", Font.PLAIN, 10));
        version.setForeground(TEXT_MUTED);
        version.setBorder(new EmptyBorder(0, 20, 16, 0));
        sidebar.add(version, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel buildMainArea() 
    {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG_DARK);
        main.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel title = new JLabel("Workspace");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        titleRow.add(title, BorderLayout.WEST);

        JLabel viewHint = new JLabel("ASCII ART VIEW");
        viewHint.setFont(new Font("Monospaced", Font.PLAIN, 10));
        viewHint.setForeground(TEXT_MUTED);
        titleRow.add(viewHint, BorderLayout.EAST);

        main.add(titleRow, BorderLayout.NORTH);

        viewCards = new CardLayout();
        viewContainer = new JPanel(viewCards);
        viewContainer.setBackground(BG_DARK);

        dropZone = buildDropZone();
        viewContainer.add(dropZone, "drop");

        JPanel contentCard = new JPanel(new CardLayout());
        contentCard.setBackground(BG_CARD);
        contentCard.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        asciiArea = new JTextArea();
        asciiArea.setFont(new Font("Monospaced", Font.PLAIN, 6));
        asciiArea.setEditable(false);
        asciiArea.setBackground(BG_CARD);
        asciiArea.setForeground(new Color(180, 220, 180));
        asciiArea.setCaretColor(ACCENT);
        JScrollPane asciiScroll = new JScrollPane(asciiArea);
        asciiScroll.setBorder(BorderFactory.createEmptyBorder());
        asciiScroll.setBackground(BG_CARD);
        asciiScroll.getViewport().setBackground(BG_CARD);

        imagePanel = new ZoomaX();

        CardLayout innerCards = new CardLayout();
        contentCard.setLayout(innerCards);
        contentCard.add(asciiScroll, "ascii");
        contentCard.add(imagePanel, "image");

        viewContainer.add(contentCard, "content");

        viewContainer.putClientProperty("innerCards", innerCards);
        viewContainer.putClientProperty("contentCard", contentCard);

        main.add(viewContainer, BorderLayout.CENTER);
        return main;
    }

    private JPanel buildDropZone() 
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createDashedBorder(BORDER, 8, 6, 4, false));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("[ + ]");
        icon.setFont(new Font("Monospaced", Font.BOLD, 36));
        icon.setForeground(ACCENT);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel line1 = new JLabel("Click to open an image");
        line1.setFont(new Font("SansSerif", Font.BOLD, 16));
        line1.setForeground(TEXT_PRIMARY);
        line1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel line2 = new JLabel();
        line2.setFont(new Font("Monospaced", Font.PLAIN, 12));
        line2.setForeground(TEXT_MUTED);
        line2.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(icon);
        inner.add(Box.createVerticalStrut(16));
        inner.add(line1);
        inner.add(Box.createVerticalStrut(6));
        inner.add(line2);

        panel.add(inner);

        panel.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e) 
            { openImage(); }
            
            public void mouseEntered(MouseEvent e) 
            {
                panel.setBackground(new Color(35, 33, 55));
                panel.repaint();
            }
            public void mouseExited(MouseEvent e) 
            {
                panel.setBackground(BG_CARD);
                panel.repaint();
            }
        });

        return panel;
    }

    private JPanel buildStatusBar() 
    {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
            new EmptyBorder(6, 20, 6, 20)
        ));

        statusLabel = new JLabel("Ready — open an image to begin");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_MUTED);
        bar.add(statusLabel, BorderLayout.WEST);

        JLabel shortcut = new JLabel();
        shortcut.setFont(new Font("Monospaced", Font.PLAIN, 10));
        shortcut.setForeground(TEXT_MUTED);
        bar.add(shortcut, BorderLayout.EAST);

        return bar;
    }

    private void openImage() 
    {
        File file = FileLoader.chooseImageFile(this);
        if (file == null) 
        {
            setStatus("No file selected.");
            return;
        }
        setStatus("Loading…");
        fileNameLabel.setText(file.getName());

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            BufferedImage img;
            protected String doInBackground() 
            {
                img = ImageConverter.loadImage(file);
                return ImageConverter.convertToASCII(img);
            }
            protected void done() 
            {
                try {
                    String ascii = get();
                    currentImage = img;
                    asciiText = ascii;
                    asciiArea.setText(ascii);
                    asciiArea.setCaretPosition(0);
                    imagePanel.setImage(img);
                    showingOriginal = false;
                    updateToggleLabel();
                    toggleBtn.setEnabled(true);
                    showContentView();
                    setStatus("Done ✔  —  " + file.getName());
                } 
                catch (Exception ex) 
                {
                    setStatus("Error loading image.");
                }
            }
        };
        worker.execute();
    }

    private void toggleView() 
    {
        if (currentImage == null) return;
        showingOriginal = !showingOriginal;
        updateToggleLabel();

        CardLayout innerCards = (CardLayout) ((JPanel) viewContainer.getComponent(1)).getLayout();
        JPanel contentCard = (JPanel) viewContainer.getComponent(1);
        innerCards.show(contentCard, showingOriginal ? "image" : "ascii");

        setStatus(showingOriginal ? "Viewing original image — scroll to zoom, drag to pan" : "Viewing ASCII art output");
    }

    private void updateToggleLabel() 
    {
        toggleBtn.setText(showingOriginal ? "View: ASCII Art" : "View: Original");
    }

    private void showContentView() 
    {
        viewCards.show(viewContainer, "content");
        CardLayout innerCards = (CardLayout) ((JPanel) viewContainer.getComponent(1)).getLayout();
        JPanel contentCard = (JPanel) viewContainer.getComponent(1);
        innerCards.show(contentCard, "ascii");
    }

    private void setStatus(String msg) 
    {
        statusLabel.setText(msg);
    }

    private JButton buildPrimaryButton(String label, String shortcut) 
    {
        JButton btn = new JButton(label) 
        {
            @Override 
            protected void paintComponent(Graphics g) 
            {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? ACCENT_HOVER : ACCENT;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(188, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private JButton buildSecondaryButton(String label, String icon) 
    {
        JButton btn = new JButton(label) 
        {
            @Override 
            protected void paintComponent(Graphics g) 
            {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() && isEnabled()) 
                {
                    g2.setColor(new Color(45, 44, 65));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setForeground(TEXT_MUTED);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER, 1, true),
            new EmptyBorder(6, 12, 6, 12)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setPreferredSize(new Dimension(188, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setDisabledIcon(null);
        return btn;
    }

    private JLabel buildSectionLabel(String text) 
    {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 9));
        lbl.setForeground(new Color(80, 78, 110));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JLabel buildInfoLabel(String text) 
    {
        JLabel lbl = new JLabel("· " + text);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}

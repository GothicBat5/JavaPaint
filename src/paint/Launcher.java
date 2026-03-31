package paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Launcher extends JFrame
{
    private static final int WINDOW_W = 780;
    private static final int WINDOW_H = 480;
    private static final int SPLASH_DURATION_MS = 3000;

    //Main palette
    private static final Color BG_SPLASH = new Color(10,  5,  20);
    private static final Color BG_MENU = new Color(22, 10, 40);
    private static final Color BG_HEADER = new Color(81, 51, 108);
    private static final Color MAGENTA = new Color(255,  0, 170);
    private static final Color PINK = new Color(255, 60, 172);
    private static final Color NEON_RED = new Color(255, 45,  85);
    private static final Color VIOLET = new Color(180,  0, 255);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(200, 150, 220); // soft lavender
    private static final Color TEXT_DIM = new Color(130,  90, 160);
    private static final Color BTN_HOVER = new Color(50,  10,  70);
    private static final Color BTN_BORDER = new Color(180,  0, 180);
    private static final Color PROGRESS_FG = MAGENTA;
    private static final Color PROGRESS_BG = new Color(40,  10,  60);

    private JPanel currentPanel;

    public Launcher()
    {
        setTitle("JanePaint v2");
        setSize(WINDOW_W, WINDOW_H);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        showSplash();
        setVisible(true);
    }

    //The splash screen

    private void showSplash()
    {
        JPanel splash = new JPanel(new BorderLayout())
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // Gradient:
                GradientPaint gp = new GradientPaint(
                        0, 0,          new Color(10,  5, 25),
                        0, WINDOW_H,   new Color(35,  0, 50)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        splash.setOpaque(false);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JPanel textBlock = new JPanel();
        textBlock.setOpaque(false);
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("\u2726");   //star
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        icon.setForeground(MAGENTA);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("JanePaint");
        appName.setFont(new Font("SansSerif", Font.BOLD, 46));
        appName.setForeground(TEXT_PRIMARY);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("v2");
        version.setFont(new Font("SansSerif", Font.BOLD, 20));
        version.setForeground(MAGENTA);
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("2026 Fortitude Labs * JanePaint");
        tagline.setFont(new Font("SansSerif", Font.ITALIC, 14));
        tagline.setForeground(TEXT_SECONDARY);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        textBlock.add(icon);
        textBlock.add(Box.createVerticalStrut(8));
        textBlock.add(appName);
        textBlock.add(Box.createVerticalStrut(2));
        textBlock.add(version);
        textBlock.add(Box.createVerticalStrut(10));
        textBlock.add(tagline);

        center.add(textBlock);
        splash.add(center, BorderLayout.CENTER);

        //loading bar ──
        JPanel bottomBar = new JPanel(new BorderLayout(0, 6));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(BorderFactory.createEmptyBorder(10, 30, 18, 30));

        JLabel loadingLabel = new JLabel("Initializing...");
        loadingLabel.setForeground(TEXT_DIM);
        loadingLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setPreferredSize(new Dimension(WINDOW_W, 5));
        bar.setBorderPainted(false);
        bar.setForeground(PROGRESS_FG);
        bar.setBackground(PROGRESS_BG);

        bottomBar.add(loadingLabel, BorderLayout.NORTH);
        bottomBar.add(bar,          BorderLayout.SOUTH);

        splash.add(bottomBar, BorderLayout.SOUTH);

        // Thin magenta top border accent
        splash.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, MAGENTA));

        switchTo(splash);

        Timer t = new Timer(SPLASH_DURATION_MS, e -> showStartupMenu());
        t.setRepeats(false);
        t.start();
    }

    //The start up
    private void showStartupMenu()
    {
        JPanel menu = new JPanel(new BorderLayout())
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // Slightly lighter purple gradient for the menu
                GradientPaint gp = new GradientPaint(
                        0, 0,        new Color(22, 10, 42),
                        0, WINDOW_H, new Color(45, 10, 65)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        menu.setOpaque(false);

        //Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_HEADER);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, MAGENTA),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        JLabel title = new JLabel("JanePaint  \u2726  v2");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("JanPaint 2026 Alpha phase");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(TEXT_SECONDARY);

        // Close button top right
        JButton closeBtn = new JButton("\u2715");
        closeBtn.setForeground(TEXT_DIM);
        closeBtn.setBackground(BG_HEADER);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));

        closeBtn.addMouseListener(new MouseAdapter()
        {
            @Override public void mouseEntered(MouseEvent e)
            {
                closeBtn.setForeground(NEON_RED);
            }
            @Override public void mouseExited(MouseEvent e)
            {
                closeBtn.setForeground(TEXT_DIM);
            }
        });

        JPanel titleStack = new JPanel();
        titleStack.setOpaque(false);
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.add(title);
        titleStack.add(Box.createVerticalStrut(3));
        titleStack.add(subtitle);

        header.add(titleStack,  BorderLayout.WEST);
        header.add(closeBtn,    BorderLayout.EAST);
        menu.add(header, BorderLayout.NORTH);

        //Center
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JPanel buttonCol = new JPanel();
        buttonCol.setOpaque(false);
        buttonCol.setLayout(new BoxLayout(buttonCol, BoxLayout.Y_AXIS));

        buttonCol.add(makeMenuButton(
                "\u25B6   New Project",
                "Set canvas size.",
                MAGENTA,
                this::showCanvasSizeDialog
        ));
        buttonCol.add(Box.createVerticalStrut(14));
        buttonCol.add(makeMenuButton(
                "\u25A1   Open Recent",
                "Open a previously saved image",
                PINK,
                this::openRecent
        ));
        buttonCol.add(Box.createVerticalStrut(14));
        buttonCol.add(makeMenuButton(
                "\u2716   Exit",
                "Close JanePaint",
                NEON_RED,
                () -> System.exit(0)
        ));

        center.add(buttonCol);
        menu.add(center, BorderLayout.CENTER);

        // ── Footer ──
        JLabel footer = new JLabel("JanePaint   \u2014  2025 - 2026 0.1.0");
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setForeground(TEXT_DIM);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        menu.add(footer, BorderLayout.SOUTH);

        // Same thin magenta top accent
        menu.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, MAGENTA));

        switchTo(menu);
    }

    private void showCanvasSizeDialog()
    {
        JPanel sizePanel = new JPanel(new BorderLayout())
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0,        new Color(22, 10, 42),
                        0, WINDOW_H, new Color(45, 10, 65)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sizePanel.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_HEADER);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, MAGENTA),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        JLabel title = new JLabel("New Project");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Choose your canvas size");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(TEXT_SECONDARY);

        JPanel titleStack = new JPanel();
        titleStack.setOpaque(false);
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.add(title);
        titleStack.add(Box.createVerticalStrut(3));
        titleStack.add(subtitle);
        header.add(titleStack, BorderLayout.WEST);
        sizePanel.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel presetLabel = makeStyledLabel("Quick Presets:");
        JLabel customLabel = makeStyledLabel("Custom Size:");

        SpinnerNumberModel wModel = new SpinnerNumberModel(800, 100, 4000, 10);
        SpinnerNumberModel hModel = new SpinnerNumberModel(600, 100, 4000, 10);
        JSpinner widthSpinner  = styledSpinner(new JSpinner(wModel));
        JSpinner heightSpinner = styledSpinner(new JSpinner(hModel));

        String[][] presets = {
                {"800 × 600",   "800",  "600"},
                {"1024 × 768",  "1024", "768"},
                {"1280 × 720",  "1280", "720"},
                {"1920 × 1080", "1920", "1080"}
        };

        JPanel presetRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        presetRow.setOpaque(false);
        presetRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String[] p : presets)
        {
            JButton pb = new JButton(p[0]);
            pb.setFocusPainted(false);
            pb.setFont(new Font("SansSerif", Font.PLAIN, 12));
            pb.setBackground(new Color(40, 10, 60));
            pb.setForeground(PINK);
            pb.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 1, true));
            pb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            pb.addActionListener(e -> {
                widthSpinner.setValue(Integer.parseInt(p[1]));
                heightSpinner.setValue(Integer.parseInt(p[2]));
            });
            presetRow.add(pb);
        }

        JPanel spinnerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        spinnerRow.setOpaque(false);
        spinnerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel wLbl = new JLabel("Width:");
        JLabel hLbl = new JLabel("Height:");
        wLbl.setForeground(TEXT_SECONDARY);
        hLbl.setForeground(TEXT_SECONDARY);
        spinnerRow.add(wLbl);
        spinnerRow.add(widthSpinner);
        spinnerRow.add(hLbl);
        spinnerRow.add(heightSpinner);

        content.add(presetLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(presetRow);
        content.add(Box.createVerticalStrut(16));
        content.add(customLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(spinnerRow);

        center.add(content);
        sizePanel.add(center, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        bottomBar.setBackground(new Color(18, 5, 32));
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BTN_BORDER));

        JButton backBtn = new JButton("\u2190  Back");
        backBtn.setFocusPainted(false);
        backBtn.setBackground(new Color(40, 10, 60));
        backBtn.setForeground(TEXT_SECONDARY);
        backBtn.setBorder(BorderFactory.createLineBorder(BTN_BORDER, 1, true));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> showStartupMenu());

        JButton createBtn = new JButton("Create Project  \u2192");
        createBtn.setFocusPainted(false);
        createBtn.setBackground(MAGENTA);
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        createBtn.setBorderPainted(false);
        createBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createBtn.addActionListener(e -> {
            int w = (int) widthSpinner.getValue();
            int h = (int) heightSpinner.getValue();
            launchPaintApp(w, h);
        });

        bottomBar.add(backBtn);
        bottomBar.add(createBtn);
        sizePanel.add(bottomBar, BorderLayout.SOUTH);

        sizePanel.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, MAGENTA));
        switchTo(sizePanel);
    }

    private void openRecent()
    {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open Image");
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            try
            {
                BufferedImage img = ImageIO.read(file);
                if (img != null)
                    launchPaintApp(img.getWidth(), img.getHeight());
                else
                    JOptionPane.showMessageDialog(this,
                            "Could not read image: " + file.getName(),
                            "Open Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this,
                        "Error opening file: " + ex.getMessage(),
                        "Open Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void launchPaintApp(int w, int h)
    {
        dispose();
        SwingUtilities.invokeLater(() -> new PaintF(w, h));
    }

    private void switchTo(JPanel panel)
    {
        if (currentPanel != null) remove(currentPanel);
        currentPanel = panel;
        add(currentPanel);
        revalidate();
        repaint();
    }

    private JPanel makeMenuButton(String label, String description, Color accent, Runnable action)
    {
        JPanel row = new JPanel(new BorderLayout(14, 0));
        row.setBackground(new Color(30, 8, 50));
        row.setOpaque(true);
        row.setMaximumSize(new Dimension(460, 68));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 0, 120), 1, true),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));

        JLabel accentBar = new JLabel(" ");
        accentBar.setOpaque(true);
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(4, 0));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        lbl.setForeground(TEXT_PRIMARY);

        JLabel desc = new JLabel(description);
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(TEXT_DIM);

        JPanel textStack = new JPanel();
        textStack.setOpaque(false);
        textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));
        textStack.add(lbl);
        textStack.add(Box.createVerticalStrut(3));
        textStack.add(desc);

        row.add(accentBar,  BorderLayout.WEST);
        row.add(textStack,  BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter()
        {
            @Override public void mouseEntered(MouseEvent e)
            {
                row.setBackground(BTN_HOVER);
                row.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accent, 1, true),
                        BorderFactory.createEmptyBorder(12, 18, 12, 18)
                ));
                row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override public void mouseExited(MouseEvent e)
            {
                row.setBackground(new Color(30, 8, 50));
                row.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(90, 0, 120), 1, true),
                        BorderFactory.createEmptyBorder(12, 18, 12, 18)
                ));
                row.setCursor(Cursor.getDefaultCursor());
            }
            @Override public void mouseClicked(MouseEvent e) { action.run(); }
        });

        return row;
    }

    private JLabel makeStyledLabel(String text)
    {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(PINK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JSpinner styledSpinner(JSpinner spinner)
    {
        spinner.setPreferredSize(new Dimension(90, 28));
        spinner.getEditor().getComponent(0).setBackground(new Color(40, 10, 60));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setForeground(TEXT_PRIMARY);
        return spinner;
    }
}

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class Theme
{
    public static final Color BG_DARK = new Color(0x22283B);
    public static final Color BG_PANEL = new Color(0x161B22);
    public static final Color BG_EDITOR = new Color(0x0D1117);
    public static final Color BG_TAB = new Color(0x1C2128);
    public static final Color BG_TAB_ACTIVE = new Color(0x0D1117);
    public static final Color BG_MENUBAR = new Color(0x010409);
    public static final Color BG_MENUITEM = new Color(0x1C2128);
    public static final Color ACCENT = new Color(0x58A6FF);   
    public static final Color ACCENT_DIM = new Color(0x1F4068);
    public static final Color GUTTER_BG  = new Color(0x161B22);
    public static final Color GUTTER_FG = new Color(0x3D444D);
    public static final Color GUTTER_ACTIVE = new Color(0x8B949E);
    public static final Color TEXT_MAIN = new Color(0xC009D9);
    public static final Color TEXT_DIM = new Color(0x8B949E);
    public static final Color TEXT_SUBTLE  = new Color(0x3D444D);
    public static final Color BORDER_COLOR = new Color(0x30363D);
    public static final Color SELECTION_BG = new Color(0x1F4068);
    public static final Color CARET_COLOR = new Color(0x58A6FF);
    public static final Color STATUS_BG = new Color(0x010409);
    public static final Color STATUS_FG = new Color(0x8B949E);

    //Fonts
    public static final Font  EDITOR_FONT = new Font("JetBrains Mono", Font.PLAIN, 15);
    public static final Font  EDITOR_FONT_FB = new Font("Consolas",  Font.PLAIN, 15);
    public static final Font  UI_FONT  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font  UI_FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    public static Font editorFont()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (String name : ge.getAvailableFontFamilyNames())
        {
            if (name.equalsIgnoreCase("JetBrains Mono")) return EDITOR_FONT;
        }
        return EDITOR_FONT_FB;
    }

    public static void apply(JFrame frame)
    {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ignored) {}

        // Global overrides
        UIManager.put("Panel.background", BG_PANEL);
        UIManager.put("OptionPane.background", BG_PANEL);
        UIManager.put("OptionPane.messageForeground", TEXT_MAIN);
        // Menu
        UIManager.put("MenuBar.background", BG_MENUBAR);
        UIManager.put("MenuBar.border", BorderFactory.createMatteBorder(0,0,1,0, BORDER_COLOR));
        UIManager.put("Menu.background", BG_MENUBAR);
        UIManager.put("Menu.foreground", TEXT_DIM);
        UIManager.put("Menu.selectionBackground", ACCENT_DIM);
        UIManager.put("Menu.selectionForeground", TEXT_MAIN);
        UIManager.put("MenuItem.background", BG_MENUITEM);
        UIManager.put("MenuItem.foreground", TEXT_MAIN);
        UIManager.put("MenuItem.selectionBackground", ACCENT_DIM);
        UIManager.put("MenuItem.selectionForeground", TEXT_MAIN);
        UIManager.put("PopupMenu.background", BG_MENUITEM);
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(BORDER_COLOR, 1));
        UIManager.put("Separator.background", BORDER_COLOR);
        UIManager.put("Separator.foreground", BORDER_COLOR);
        //Tabs
        UIManager.put("TabbedPane.background", BG_TAB);
        UIManager.put("TabbedPane.foreground", TEXT_DIM);
        UIManager.put("TabbedPane.selected", BG_TAB_ACTIVE);
        UIManager.put("TabbedPane.selectedForeground", TEXT_MAIN);
        UIManager.put("TabbedPane.contentAreaColor", BG_EDITOR);
        UIManager.put("TabbedPane.borderHightlightColor", BORDER_COLOR);
        UIManager.put("TabbedPane.darkShadow", BORDER_COLOR);
        UIManager.put("TabbedPane.shadow", BG_PANEL);
        UIManager.put("TabbedPane.focus", ACCENT_DIM);
        //Scroll
        UIManager.put("ScrollBar.background", BG_EDITOR);
        UIManager.put("ScrollBar.thumb", new Color(0x30363D));
        UIManager.put("ScrollBar.thumbHighlight",  new Color(0x3D444D));
        UIManager.put("ScrollBar.track", BG_EDITOR);
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("Button.background", BG_MENUITEM);
        UIManager.put("Button.foreground", TEXT_MAIN);
        //FileChooser
        UIManager.put("FileChooser.background", BG_PANEL);
        UIManager.put("Label.foreground", TEXT_MAIN);
        UIManager.put("TextField.background", BG_EDITOR);
        UIManager.put("TextField.foreground", TEXT_MAIN);
        UIManager.put("TextField.caretForeground", CARET_COLOR);
        UIManager.put("List.background", BG_PANEL);
        UIManager.put("List.foreground", TEXT_MAIN);
        UIManager.put("List.selectionBackground", ACCENT_DIM);
        UIManager.put("List.selectionForeground", TEXT_MAIN);
        UIManager.put("Table.background", BG_PANEL);
        UIManager.put("Table.foreground", TEXT_MAIN);
        UIManager.put("Table.selectionBackground", ACCENT_DIM);
        UIManager.put("Table.selectionForeground", TEXT_MAIN);
        UIManager.put("TableHeader.background", BG_MENUBAR);
        UIManager.put("TableHeader.foreground", TEXT_DIM);

        SwingUtilities.updateComponentTreeUI(frame);
        frame.getContentPane().setBackground(BG_DARK);
    }

    public static Border panelBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createMatteBorder(top, left, bottom, right, BORDER_COLOR);
    }

    public static void styleScrollBar(JScrollBar sb)
    {
        sb.setPreferredSize(new Dimension(8, 8));
        sb.setBackground(BG_EDITOR);
        sb.setUI(new BasicScrollBarUI()
        {

            @Override
            protected void configureScrollBarColors()
            {
                thumbColor = new Color(0x3D444D);
                trackColor = BG_EDITOR;
            }

            @Override
            protected JButton createDecreaseButton(int o)
            {
                return zeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int o)
            {
                return zeroButton();
            }

            private JButton zeroButton()
            {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2, 6, 6);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r)
            {
                g.setColor(trackColor);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
        });
    }
}

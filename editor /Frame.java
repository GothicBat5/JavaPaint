import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class Frame extends JFrame
{

    private final JTabbedPane tabbedPane;
    private final StatusBar statusBar;
    private int untitledCount = 1;

    public Frame()
    {
        setTitle("ChloeJane Editor");
        setSize(1200, 780);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        Theme.apply(this);

        tabbedPane = buildTabbedPane();
        statusBar = new StatusBar();

        getContentPane().setBackground(Theme.BG_DARK);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buildToolbar(), BorderLayout.NORTH);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        createMenuBar();
        registerShortcuts();
        addNewTab();

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                tryCloseAll();
            }
        });

        setVisible(true);
    }

    private JTabbedPane buildTabbedPane()
    {
        JTabbedPane tp = new JTabbedPane(JTabbedPane.TOP);
        tp.setBackground(Theme.BG_TAB);
        tp.setForeground(Theme.TEXT_DIM);
        tp.setFont(Theme.UI_FONT);
        tp.setBorder(BorderFactory.createEmptyBorder());

        tp.addChangeListener(e -> {
            Editor tab = activeEditor();
            statusBar.update(tab);
            if (tab != null)
            {
                tab.getTextArea().addCaretListener(ev -> statusBar.update(tab));
            }
        });
        return tp;
    }


    private JPanel buildToolbar()
    {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        bar.setBackground(Theme.BG_MENUBAR);
        bar.setBorder(Theme.panelBorder(0, 0, 1, 0));
        bar.add(toolButton("+ New",e -> addNewTab()));
        bar.add(toolButton("^ Open",e -> FileManager.openFile(this, tabbedPane)));
        bar.add(toolButton("# Save",e -> saveCurrentTab()));
        bar.add(toolSeparator());
        bar.add(toolButton("* Close",e -> closeCurrentTab()));

        return bar;
    }

    private JButton toolButton(String label, ActionListener al)
    {
        JButton btn = new JButton(label);
        btn.setFont(Theme.UI_FONT);
        btn.setBackground(new Color(0x1C2128));
        btn.setForeground(Theme.TEXT_DIM);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1), BorderFactory.createEmptyBorder(3, 10, 3, 10)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);

        btn.addMouseListener(new MouseAdapter()
        {

            public void mouseEntered(MouseEvent e)
            {
                btn.setBackground(Theme.ACCENT_DIM);
                btn.setForeground(Theme.TEXT_MAIN);
            }

            public void mouseExited(MouseEvent e)
            {
                btn.setBackground(new Color(0x1C2128));
                btn.setForeground(Theme.TEXT_DIM);
            }
        });
        return btn;
    }

    private JSeparator toolSeparator()
    {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 22));
        sep.setForeground(Theme.BORDER_COLOR);
        return sep;
    }


    private void createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Theme.BG_MENUBAR);
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        JMenu fileMenu = menu("File");
        fileMenu.add(menuItem("New","ctrl N",e -> addNewTab()));
        fileMenu.add(menuItem("Open...","ctrl O",e -> FileManager.openFile(this, tabbedPane)));
        fileMenu.addSeparator();
        fileMenu.add(menuItem("Save","ctrl S",e -> saveCurrentTab()));
        fileMenu.add(menuItem("Save As…","ctrl shift S",e -> saveCurrentTabAs()));
        fileMenu.addSeparator();
        fileMenu.add(menuItem("Close Tab","ctrl W", e -> closeCurrentTab()));
        fileMenu.add(menuItem("Exit",null,e -> tryCloseAll()));

        JMenu editMenu = menu("Edit");
        editMenu.add(menuItem("Undo","ctrl Z",e -> { Editor t = activeEditor();
            if (t != null) t.undo(); }));

        editMenu.add(menuItem("Redo","ctrl Y",e -> { Editor t = activeEditor();
            if (t != null) t.redo(); }));

        editMenu.addSeparator();
        editMenu.add(menuItem("Select All", "ctrl A", e -> { Editor t = activeEditor();
            if (t != null) t.getTextArea().selectAll(); }));

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
    }

    private JMenu menu(String text)
    {
        JMenu m = new JMenu(text);
        m.setFont(Theme.UI_FONT);
        m.setForeground(Theme.TEXT_DIM);
        return m;
    }

    private JMenuItem menuItem(String text, String shortcut, ActionListener al)
    {
        JMenuItem item = new JMenuItem(text);
        item.setFont(Theme.UI_FONT);
      
        if (shortcut != null) item.setAccelerator(KeyStroke.getKeyStroke(shortcut));
        item.addActionListener(al);
        return item;
    }

    private void registerShortcuts() {  }

    void addNewTab()
    {
        Editor tab = new Editor();
        String title = "Untitled" + (untitledCount > 1 ? " " + untitledCount : "");
        untitledCount++;

        int idx = tabbedPane.getTabCount();
        tabbedPane.addTab(title, tab);
        tabbedPane.setSelectedIndex(idx);
        FileManager.wireModifiedCallback(tabbedPane, tab, idx);
        tab.getTextArea().requestFocusInWindow();
    }

    private void closeCurrentTab()
    {
        int idx = tabbedPane.getSelectedIndex();
        if (idx < 0) return;

        Editor tab = (Editor) tabbedPane.getComponentAt(idx);

        if (confirmClose(tab))
        {
            tabbedPane.removeTabAt(idx);
            if (tabbedPane.getTabCount() == 0) addNewTab();
        }
    }

    private void tryCloseAll()
    {

        for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--)
        {
            tabbedPane.setSelectedIndex(i);
            Editor tab = (Editor) tabbedPane.getComponentAt(i);

            if (!confirmClose(tab)) return;
            tabbedPane.removeTabAt(i);
        }
        dispose();
        System.exit(0);
    }

    private boolean confirmClose(Editor tab)
    {
        if (!tab.isModified()) return true;

        String name = tab.getCurrentFile() != null ? tab.getCurrentFile().getName() : "Untitled";
        int choice = JOptionPane.showConfirmDialog(this, "\"" + name + "\" has unsaved changes. Save before closing?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) return FileManager.saveFile(this, tab, tabbedPane);
        else if (choice == JOptionPane.NO_OPTION) return true;
        return false; //CANCEL
    }


    private void saveCurrentTab()
    {
        Editor tab = activeEditor();
        if (tab != null) FileManager.saveFile(this, tab, tabbedPane);
    }

    private void saveCurrentTabAs()
    {
        Editor tab = activeEditor();
        if (tab != null) FileManager.saveFileAs(this, tab, tabbedPane);
    }


    private Editor activeEditor()
    {
        Component c = tabbedPane.getSelectedComponent();
        return (c instanceof Editor) ? (Editor) c : null;
    }
}

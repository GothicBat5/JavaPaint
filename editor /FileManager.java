import javax.swing.*;
import java.io.*;

public class FileManager
{

    public static void openFile(JFrame parent, JTabbedPane tabbedPane)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open File");

        if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();


        for (int i = 0; i < tabbedPane.getTabCount(); i++)
        {
            Editor tab = (Editor) tabbedPane.getComponentAt(i);

            if (file.equals(tab.getCurrentFile()))
            {
                tabbedPane.setSelectedIndex(i);
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            Editor tab = new Editor();
            StringBuilder sb = new StringBuilder();
            String line;
            boolean first = true;

            while ((line = reader.readLine()) != null)
            {
                if (!first) sb.append('\n');
                sb.append(line);
                first = false;
            }
            tab.getTextArea().setText(sb.toString());
            tab.getTextArea().setCaretPosition(0);
            tab.setCurrentFile(file);

            int idx = tabbedPane.getTabCount();
            tabbedPane.addTab(tab.getDisplayName(), tab);
            tabbedPane.setSelectedIndex(idx);
            wireModifiedCallback(tabbedPane, tab, idx);

        }
        catch (IOException e) {

            JOptionPane.showMessageDialog(parent, "Could not open file:\n" + e.getMessage(),"Open Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean saveFile(JFrame parent, Editor tab, JTabbedPane tabbedPane)
    {
        File file = tab.getCurrentFile();

        if (file == null)
        {
            return saveFileAs(parent, tab, tabbedPane);
        }

        return doWrite(parent, tab, file, tabbedPane);
    }

    public static boolean saveFileAs(JFrame parent, Editor tab, JTabbedPane tabbedPane)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save As");

        if (tab.getCurrentFile() != null) chooser.setSelectedFile(tab.getCurrentFile());

        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return false;

        File file = chooser.getSelectedFile();

        if (file.exists())
        {
            int confirm = JOptionPane.showConfirmDialog(parent, "\"" + file.getName() + "\" already exists. Overwrite?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return false;
        }

        return doWrite(parent, tab, file, tabbedPane);
    }

    private static boolean doWrite(JFrame parent, Editor tab, File file, JTabbedPane tabbedPane)
    {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
        {
            writer.write(tab.getTextArea().getText());
            tab.setCurrentFile(file);
            int idx = tabbedPane.indexOfComponent(tab);
            if (idx >= 0) tabbedPane.setTitleAt(idx, tab.getDisplayName());
            return true;
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Could not save file:\n" + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void wireModifiedCallback(JTabbedPane tabbedPane, Editor tab, int tabIndex)
    {
        tab.setOnModifiedChanged(() -> {
            int idx = tabbedPane.indexOfComponent(tab);
            if (idx >= 0) tabbedPane.setTitleAt(idx, tab.getDisplayName());
        });
    }
}

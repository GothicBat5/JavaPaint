import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.File;

public class Editor extends JPanel
{

    private final JTextArea   textArea;
    private final UndoManager undoManager = new UndoManager();
    private File currentFile;
    private boolean modified = false;

    //Callback so the tab title can be updated
    private Runnable onModifiedChanged;

    public Editor()
    {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_EDITOR);

        textArea = new JTextArea();
        textArea.setFont(Theme.editorFont());
        textArea.setBackground(Theme.BG_EDITOR);
        textArea.setForeground(Theme.TEXT_MAIN);
        textArea.setCaretColor(Theme.CARET_COLOR);
        textArea.setSelectionColor(Theme.SELECTION_BG);
        textArea.setSelectedTextColor(Theme.TEXT_MAIN);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(true);
        textArea.setTabSize(4);
        textArea.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setBlinkRate(530);
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        textArea.getDocument().addUndoableEditListener(undoManager);

        textArea.getDocument().addDocumentListener(new DocumentListener()
        {
            private void mark()
            {
                if (!modified)
                {
                    modified = true;
                    if (onModifiedChanged != null) onModifiedChanged.run();
                }
            }
            public void insertUpdate(DocumentEvent e)  { mark(); }
            public void removeUpdate(DocumentEvent e)  { mark(); }
            public void changedUpdate(DocumentEvent e) { mark(); }
        });

        LineNumberGutter gutter = new LineNumberGutter(textArea);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setRowHeaderView(gutter);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Theme.BG_EDITOR);
        scrollPane.getViewport().setBackground(Theme.BG_EDITOR);

        Theme.styleScrollBar(scrollPane.getVerticalScrollBar());
        Theme.styleScrollBar(scrollPane.getHorizontalScrollBar());

        add(scrollPane, BorderLayout.CENTER);
    }

    public void undo()
    {
        if (undoManager.canUndo()) undoManager.undo();
    }

    public void redo()
    {
        if (undoManager.canRedo()) undoManager.redo();
    }


    public JTextArea getTextArea()
    {
        return textArea;
    }

    public File getCurrentFile()
    {
        return currentFile;
    }

    public void setCurrentFile(File f)
    {
        currentFile = f;
        modified = false;
        if (onModifiedChanged != null) onModifiedChanged.run();
    }

    public boolean isModified()
    {
        return modified;
    }

    public void clearModified()
    {
        modified = false;
        if (onModifiedChanged != null) onModifiedChanged.run();
    }

    public void setOnModifiedChanged(Runnable r)
    {
        onModifiedChanged = r;
    }

    public String getDisplayName()
    {
        String name = (currentFile != null) ? currentFile.getName() : "Untitled";
        return modified ? "● " + name : name;
    }
}

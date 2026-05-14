import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StatusBar extends JPanel
{

    private final JLabel positionLabel;
    private final JLabel fileLabel;
    private final JLabel brandLabel;

    public StatusBar()
    {
        setLayout(new BorderLayout());
        setBackground(Theme.STATUS_BG);
        setPreferredSize(new Dimension(0, 26));
        setBorder(Theme.panelBorder(1, 0, 0, 0));

        Font font = Theme.UI_FONT_SMALL;

        positionLabel = new JLabel("  Ln 1, Col 1");
        positionLabel.setForeground(Theme.STATUS_FG);
        positionLabel.setFont(font);

        fileLabel = new JLabel("", SwingConstants.CENTER);
        fileLabel.setForeground(Theme.TEXT_SUBTLE);
        fileLabel.setFont(font);

        brandLabel = new JLabel("Ryujin Editor  ");
        brandLabel.setForeground(new Color(0x3D444D));
        brandLabel.setFont(font);

        add(positionLabel, BorderLayout.WEST);
        add(fileLabel, BorderLayout.CENTER);
        add(brandLabel, BorderLayout.EAST);
    }

    public void update(Editor editor)
    {
        if (editor == null)
        {
            positionLabel.setText("  Ln 1, Col 1");
            fileLabel.setText("");
            return;
        }

        javax.swing.text.JTextComponent ta = editor.getTextArea();
        try {
            int caret = ta.getCaretPosition();
            int line = editor.getTextArea().getLineOfOffset(caret);
            int col = caret - editor.getTextArea().getLineStartOffset(line);

            positionLabel.setText(String.format("  Ln %d, Col %d", line + 1, col + 1));
        }
        catch (Exception ignored) {
            positionLabel.setText("  Ln 1, Col 1");
        }

        String path = editor.getCurrentFile() != null ? editor.getCurrentFile().getAbsolutePath() : "Untitled";
        fileLabel.setText(path);
    }
}

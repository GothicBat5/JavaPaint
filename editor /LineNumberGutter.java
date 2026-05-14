import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import java.awt.*;

public class LineNumberGutter extends JPanel
{

    private final JTextArea textArea;
    private static final int PADDING_RIGHT = 12;
    private static final int PADDING_LEFT  = 16;

    public LineNumberGutter(JTextArea textArea)
    {
        this.textArea = textArea;
        setBackground(Theme.GUTTER_BG);
        setForeground(Theme.GUTTER_FG);
        setFont(Theme.editorFont());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_COLOR));

        textArea.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(DocumentEvent e)
            {
                repaint();
            }
            public void removeUpdate(DocumentEvent e)
            {

                repaint();
            }
            public void changedUpdate(DocumentEvent e)
            {
                repaint();
            }
        });

        textArea.addPropertyChangeListener("font", e -> { setFont(textArea.getFont()); repaint(); });
    }

    @Override
    public Dimension getPreferredSize()
    {
        int lines = textArea.getLineCount();
        String sample = String.valueOf(Math.max(lines, 99));
        FontMetrics fm = getFontMetrics(getFont());
        int width = fm.stringWidth(sample) + PADDING_LEFT + PADDING_RIGHT;
        return new Dimension(width, textArea.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g2.getFontMetrics(getFont());
        int lineHeight = textArea.getFontMetrics(textArea.getFont()).getHeight();
        int caretLine   = -1;

        try {
            int caretPos = textArea.getCaretPosition();
            caretLine = textArea.getLineOfOffset(caretPos);
        }
        catch (Exception ignored) {

        }

        Rectangle clip = g2.getClipBounds();
        int startLine = (clip.y / lineHeight);
        int endLine = Math.min(textArea.getLineCount() - 1, (clip.y + clip.height) / lineHeight + 1);

        Element root = textArea.getDocument().getDefaultRootElement();
        int componentWidth = getWidth();

        for (int i = startLine; i <= endLine; i++)
        {
            int y = (i + 1) * lineHeight - (lineHeight - fm.getAscent()) / 2 - 2;

            if (i == caretLine)
            {
                g2.setColor(new Color(0x1F4068));
                g2.fillRect(0, i * lineHeight, componentWidth, lineHeight);
                g2.setColor(Theme.GUTTER_ACTIVE);
            }

            else {
                g2.setColor(Theme.GUTTER_FG);
            }

            String lineNum = String.valueOf(i + 1);
            int textWidth = fm.stringWidth(lineNum);
            g2.drawString(lineNum, componentWidth - textWidth - PADDING_RIGHT, y);
        }
    }
}

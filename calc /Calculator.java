import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class Calculator extends JFrame implements ActionListener
{

    private String expression = "";
    private boolean justEvaluated = false;
    private JLabel expressionLabel;
    private JLabel resultLabel;

    private static final String[][] BUTTON_GRID = {
            { "C",  "/", "*", "-" },
            { "7",  "8", "9", "+" },
            { "4",  "5", "6", "=" },
            { "1",  "2", "3", "=" },
            { "0",  "0", ".", "=" }
    };

    public Calculator()
    {
        setTitle("CALC.EXE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);

        JPanel root = getJPanel();

        JPanel titleBar = buildTitleBar();
        root.add(titleBar, BorderLayout.NORTH);

        JPanel display = buildDisplay();
        root.add(display, BorderLayout.CENTER);

        JPanel buttons = buildButtons();
        root.add(buttons, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 24, 24));
    }

    private static JPanel getJPanel()
    {

        JPanel root = new JPanel(new BorderLayout(0, 0))
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 12));

                for (int y = 0; y < getHeight(); y += 3)
                {
                    g2.drawLine(0, y, getWidth(), y);
                }
            }
        };
        root.setBackground(Pallete.BG_DEEP);
        root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Pallete.BORDER_GLOW, 2),
        new EmptyBorder(18, 18, 18, 18)));
        return root;
    }

    private JPanel buildTitleBar()
    {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 0, 14, 0));

        JLabel sys = new JLabel("SYS::CALC");
        sys.setFont(new Font("Consolas", Font.PLAIN, 10));
        sys.setForeground(new Color(0x7C, 0x3C, 0xFC, 180));

        JLabel ver = new JLabel("v2.0 ●");
        ver.setFont(new Font("Consolas", Font.PLAIN, 10));
        ver.setForeground(Pallete.NEON_TEAL);

        final Point[] dragOrigin = {null};

        bar.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent e)
            {
                dragOrigin[0] = e.getPoint();
            }
        });

        bar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {

            public void mouseDragged(java.awt.event.MouseEvent e)
            {
                if (dragOrigin[0] != null)
                {
                    Point loc = getLocation();setLocation(loc.x + e.getX() - dragOrigin[0].x, loc.y + e.getY() - dragOrigin[0].y);
                }
            }
        });

        JButton close = new JButton("✕");
        close.setFont(new Font("Consolas", Font.PLAIN, 11));
        close.setForeground(Pallete.NEON_PINK);
        close.setOpaque(false);
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setFocusPainted(false);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> System.exit(0));

        bar.add(sys, BorderLayout.WEST);
        bar.add(ver, BorderLayout.EAST);

        JPanel rightBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBar.setOpaque(false);
        rightBar.add(ver);
        rightBar.add(close);
        bar.add(rightBar, BorderLayout.EAST);

        return bar;
    }


    private JPanel buildDisplay()
    {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(0, 0, 14, 0));

        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Pallete.BG_DISPLAY);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Pallete.NEON_TEAL, 1),
        new EmptyBorder(10, 16, 12, 16)));
        expressionLabel = new JLabel(" ");
        expressionLabel.setFont(new Font("Consolas", Font.PLAIN, 13));
        expressionLabel.setForeground(new Color(0x7C, 0x3C, 0xFC, 160));
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        resultLabel = new JLabel("0");
        resultLabel.setFont(new Font("Consolas", Font.BOLD, 38));
        resultLabel.setForeground(Pallete.NEON_TEAL);
        resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel badge = new JLabel("OUTPUT");
        badge.setFont(new Font("Consolas", Font.PLAIN, 8));
        badge.setForeground(new Color(0x1D, 0xE9, 0xB6, 80));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(badge, BorderLayout.WEST);
        topRow.add(expressionLabel, BorderLayout.EAST);

        card.add(topRow, BorderLayout.NORTH);
        card.add(resultLabel, BorderLayout.CENTER);

        outer.add(card, BorderLayout.CENTER);
        return outer;
    }


    private JPanel buildButtons()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(5, 5, 5, 5);
        g.weightx = 1;
        g.weighty = 1;

        addBtn(panel, g, "C",   0, 0, 1, 1, Pallete.BTN_CLEAR,    Pallete.NEON_PINK,    Pallete.NEON_PINK);

        addBtn(panel, g, "÷",   0, 1, 1, 1, Pallete.BTN_OPERATOR, Pallete.NEON_VIOLET,  Pallete.NEON_VIOLET);

        addBtn(panel, g, "×",0, 2, 1, 1, Pallete.BTN_OPERATOR, Pallete.NEON_VIOLET,  Pallete.NEON_VIOLET);

        addBtn(panel, g,"−",0, 3, 1, 1, Pallete.BTN_OPERATOR, Pallete.NEON_VIOLET,  Pallete.NEON_VIOLET);

        addBtn(panel, g,"7",1, 0, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,"8",1, 1, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,"9",1, 2, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,"+",1, 3, 1, 1, Pallete.BTN_OPERATOR, Pallete.NEON_VIOLET, Pallete.NEON_VIOLET);

        addBtn(panel, g,"4",2, 0, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,"5",2, 1, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,"6",2, 2, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,"=",2, 3, 2, 1, Pallete.BTN_EQUAL, Pallete.NEON_VIOLET, Pallete.NEON_VIOLET);

        addBtn(panel, g,"1",3, 0, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,"2",3, 1, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,"3",3, 2, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,"0",4, 0, 1, 2, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        addBtn(panel, g,".",4, 2, 1, 1, Pallete.BTN_DIGIT, null, Pallete.NEON_TEAL);

        return panel;
    }
    /**
     * @param row GridBag row
     * @param col GridBag col
     * @param rowSpan number of rows to span
     * @param colSpan number of cols to span
     */

    private void addBtn(JPanel panel, GridBagConstraints gbc, String label, int row, int col, int rowSpan, int colSpan, Color bg, Color fg, Color glow)
    {

        RoundedButton btn = new RoundedButton(label, bg, glow);
        if (fg != null)
        {
            btn.setForeground(fg);
        }

        if (label.equals("=") || label.equals("C"))
        {
            btn.setFont(new Font("Consolas", Font.BOLD, 20));
        }

        btn.setPreferredSize(new Dimension(74, rowSpan > 1 ? 74 * rowSpan + 5 * (rowSpan-1) : 62));
        btn.addActionListener(this);

        gbc.gridx = col;

        gbc.gridy = row;

        gbc.gridheight = rowSpan;

        gbc.gridwidth = colSpan;

        panel.add(btn, gbc);

        gbc.gridheight = 1;

        gbc.gridwidth  = 1;
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        cmd = cmd.replace("÷", "/").replace("×", "*").replace("−", "-");

        try
        {
            if (cmd.equals("C"))
            {
                expression = "";
                justEvaluated = false;
                expressionLabel.setText(" ");
                resultLabel.setText("0");
                resultLabel.setForeground(Pallete.NEON_TEAL);
                return;
            }

            if ("0123456789.".contains(cmd))
            {
                if (justEvaluated)
                {
                    justEvaluated = false;
                }

                expression += cmd;
                resultLabel.setText(expression);
                resultLabel.setForeground(Pallete.NEON_TEAL);
            }
            else if ("+-*/".contains(cmd))
            {
                justEvaluated = false;

                expression = expression.stripTrailing();
                if (!expression.isEmpty() && "+-*/".contains(String.valueOf(expression.charAt(expression.length() - 1))))
                {
                    expression = expression.substring(0, expression.length() - 2);
                }

                String display = cmd.replace("/","÷").replace("*","×").replace("-","−");
                expression += " " + cmd + " ";
                resultLabel.setText(expression.replace("/","÷").replace("*","×").replace("-","−"));
                resultLabel.setFont(new Font("Consolas", Font.BOLD, 28));
                resultLabel.setForeground(Pallete.NEON_VIOLET);
            }
            else if ("=".equals(cmd))
            {
                if (expression.isBlank()) return;

                expressionLabel.setText(expression.replace("/","÷").replace("*","×").replace("-","−") + " =");
                double result = evaluateExpression(expression.trim());

                String formatted = (result == Math.floor(result) && !Double.isInfinite(result))
                        ? String.valueOf((long) result)
                        : String.valueOf(result);

                resultLabel.setFont(new Font("Consolas", Font.BOLD, 38));
                resultLabel.setForeground(Pallete.NEON_TEAL);
                resultLabel.setText(formatted);

                expression = formatted;
                justEvaluated = true;
            }
        }
        catch (ArithmeticException ex)
        {
            resultLabel.setForeground(Pallete.NEON_PINK);
            resultLabel.setText("DIV/0 ERROR");
            expression    = "";
            justEvaluated = false;
        }
        catch (Exception ex)
        {
            resultLabel.setForeground(Pallete.NEON_PINK);
            resultLabel.setText("SYNTAX ERR");
            expression = "";
            justEvaluated = false;
        }
    }

    private double evaluateExpression(String expr) throws Exception
    {
        String[] raw = expr.split(" ");
        if (raw.length == 0 || raw.length % 2 == 0)
        {
            throw new Exception("Bad expression");
        }

        java.util.List<Double> nums = new java.util.ArrayList<>();
        java.util.List<String> ops  = new java.util.ArrayList<>();

        nums.add(Double.parseDouble(raw[0]));

        for (int i = 1; i < raw.length; i += 2)
        {
            ops.add(raw[i]);
            nums.add(Double.parseDouble(raw[i + 1]));
        }

        for (int i = 0; i < ops.size(); )
        {
            String op = ops.get(i);

            if (op.equals("*") || op.equals("/"))
            {
                double a = nums.get(i), b = nums.get(i + 1);

                if (op.equals("/") && b == 0)
                {
                    throw new ArithmeticException("Division by zero");
                }

                double r = op.equals("*") ? a * b : a / b;
                nums.set(i, r);
                nums.remove(i + 1);
                ops.remove(i);
            }
            else {
                i++;
            }
        }

        double result = nums.get(0);

        for (int i = 0; i < ops.size(); i++)
        {
            String op = ops.get(i);
            double b  = nums.get(i + 1);
            result = op.equals("+") ? result + b : result - b;
        }
        return result;
    }


    public static void main(String[] args)
    {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception ignored)
            {

            }

            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }

    private static String spaced(String text, int px)
    {
        return "<html><span style='letter-spacing:" + px + "px'>" + text + "</span></html>";
    }
}

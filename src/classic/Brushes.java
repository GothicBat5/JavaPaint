import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class Brushes 
{

    public enum Tool {
        PENCIL, BRUSH, ERASER, FILL, LINE, RECT, FILLED_RECT, ELLIPSE, FILLED_ELLIPSE
    }

    private Tool currentTool = Tool.PENCIL;
    private int brushSize = 1;
    private int startX, startY;
    public void setTool(Tool t) { currentTool = t; }
    public Tool getTool() { return currentTool; }
    public void setBrushSize(int s) { brushSize = s; }
    public int getBrushSize() { return brushSize; }
    public void setStart(int x, int y) { startX = x; startY = y; }
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }

    public void draw(Graphics2D g, int x1, int y1, int x2, int y2, Color color) 
    {
        g.setColor(color);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        switch (currentTool) 
        {
            case PENCIL -> drawLine(g, x1, y1, x2, y2, 1);
            case BRUSH -> drawLine(g, x1, y1, x2, y2, brushSize);
            case ERASER -> {
                g.setColor(Color.WHITE);
                drawLine(g, x1, y1, x2, y2, brushSize * 4);
            }
            default -> { }
        }
    }

    private void drawLine(Graphics2D g, int x1, int y1, int x2, int y2, int size) 
    {
        g.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x1, y1, x2, y2);
    }

    public void drawShape(Graphics2D g, int x1, int y1, int x2, int y2, Color color) 
    {
        g.setColor(color);
        g.setStroke(new BasicStroke(brushSize));
        int rx = Math.min(x1, x2), ry = Math.min(y1, y2);
        int rw = Math.abs(x2 - x1), rh = Math.abs(y2 - y1);

        switch (currentTool) 
        {
            case LINE -> g.drawLine(x1, y1, x2, y2);
            case RECT -> g.drawRect(rx, ry, rw, rh);
            case FILLED_RECT -> g.fillRect(rx, ry, rw, rh);
            case ELLIPSE -> g.drawOval(rx, ry, rw, rh);
            case FILLED_ELLIPSE-> g.fillOval(rx, ry, rw, rh);
            default -> {}
        }
    }

    // flood fill
    public void fill(BufferedImage img, int x, int y, Color fillColor) 
    {
        int target = img.getRGB(x, y);
        int fill = fillColor.getRGB();
        if (target == fill) return;

        Stack<Point> stack = new Stack<>();
        stack.push(new Point(x, y));
        int w = img.getWidth(), h = img.getHeight();

        while (!stack.isEmpty()) 
        {
            Point p = stack.pop();
            int px = p.x, py = p.y;
            if (px < 0 || px >= w || py < 0 || py >= h) continue;
            if (img.getRGB(px, py) != target) continue;
            img.setRGB(px, py, fill);
            stack.push(new Point(px + 1, py));
            stack.push(new Point(px - 1, py));
            stack.push(new Point(px, py + 1));
            stack.push(new Point(px, py - 1));
        }
    }

    public boolean isShapeTool() 
    {
        return currentTool == Tool.LINE || currentTool == Tool.RECT ||
               currentTool == Tool.FILLED_RECT || currentTool == Tool.ELLIPSE ||
               currentTool == Tool.FILLED_ELLIPSE;
    }
}

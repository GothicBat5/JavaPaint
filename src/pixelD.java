//Rainbow spinning donut :-p
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class pixelD 
{
    private double A = 0, B = 0;

    public pixelD() 
    {
        
        JFrame frame = new JFrame("Pixel Donut");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);//The windows suze obviosuly
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        DonutPanel panel = new DonutPanel();
        frame.add(panel);
        frame.setVisible(true);
        
        new Timer(16, e -> {
            panel.repaint();
            A += 0.06;
            B += 0.035;
        }).start();
    }

    //Converts HSV to packed RGB int
    private static int hsvToRgb(double h, double s, double v) 
    {
        
        int hi = (int)(h * 6) % 6;
        double f = h * 6 - Math.floor(h * 6);
        double p = v * (1 - s), q = v * (1 - f * s), t = v * (1 - (1 - f) * s);
        double r, g, b;
        
        switch (hi) 
        {
            case 0: r=v; g=t; b=p; break;
            case 1: r=q; g=v; b=p; break;
            case 2: r=p; g=v; b=t; break;
            case 3: r=p; g=q; b=v; break;
            case 4: r=t; g=p; b=v; break;
            default: r=v; g=p; b=q; break;
        }
        return ((int)(r*255) << 16) | ((int)(g*255) << 8) | (int)(b*255);
    }

    class DonutPanel extends JPanel 
    {
        
        private final BufferedImage img = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);

        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            int width = img.getWidth(), height = img.getHeight();
            double[] zbuffer = new double[width * height];

            //background
            int bg = 0x05061A;
            for (int i = 0; i < width * height; i++) 
            {
                img.setRGB(i % width, i / width, bg);
                zbuffer[i] = 0;
            }

            double cosA = Math.cos(A), sinA = Math.sin(A);
            double cosB = Math.cos(B), sinB = Math.sin(B);

            //more opaque surface
            for (double theta = 0; theta < 2 * Math.PI; theta += 0.04) 
            {
                
                double cosT = Math.cos(theta), sinT = Math.sin(theta);
                
                for (double phi = 0; phi < 2 * Math.PI; phi += 0.013) 
                {
                    double cosP = Math.cos(phi), sinP = Math.sin(phi);

                    double cx = 2.3 + cosT;
                    double cy = sinT;
                    double x = cx * (cosB * cosP + sinA * sinB * sinP) - cy * cosA * sinB;
                    double y = cx * (sinB * cosP - sinA * cosB * sinP) + cy * cosA * cosB;
                    double z = cosA * cx * sinP + cy * sinA + 6;

                    double ooz = 1 / z;
                    int xp = (int)(width  / 2 + 180 * ooz * x);
                    int yp = (int)(height / 2 - 180 * ooz * y);
                    int idx = xp + yp * width;

                    double L = cosP * cosT * sinB - cosA * cosT * sinP - sinA * sinT + cosB * (cosA * sinT - cosT * sinA * sinP);

                    if (idx >= 0 && idx < width * height && xp >= 0 && xp < width) 
                    {
                        
                        if (ooz > zbuffer[idx]) 
                        {
                            
                            zbuffer[idx] = ooz;

                            double brightness = Math.max(0.15, Math.min(1.0, L));

                            //color animation?
                            double hue = ((phi / (2 * Math.PI)) + A * 0.15) % 1.0;
                            
                            int color = hsvToRgb(hue, 0.75, brightness);
                            
                            img.setRGB(xp, yp, color);
                        }
                    }
                }
            }
            g.drawImage(img, 0, 0, null);
        }
    }

    public static void main(String[] args) 
    {
        //Runs the program here. 
        SwingUtilities.invokeLater(pixelD::new);
    }
}

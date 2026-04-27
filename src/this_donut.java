
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class pixelD 
{

    public static void main(String[] args) 
    {
        
        SwingUtilities.invokeLater(pixelD::new);
    }

    private double A = 0, B = 0;

    public pixelD () 
    {
      
        JFrame frame = new JFrame("Pixel Donut");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);

        DonutPanel panel = new DonutPanel();
        frame.add(panel);

        frame.setVisible(true);

        new Timer(16, e -> {
            panel.repaint();
            A += 0.05;
            B += 0.03;
        }).start();
    }

    class DonutPanel extends JPanel 
    {

        private BufferedImage img = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);

        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);

            int width = img.getWidth();
            int height = img.getHeight();

            double[] zbuffer = new double[width * height];

            for (int i = 0; i < width * height; i++) 
            {
                img.setRGB(i % width, i / width, 0x000000);
                zbuffer[i] = 0;
            }

            for (double theta = 0; theta < 2 * Math.PI; theta += 0.07) 
            {
                
                for (double phi = 0; phi < 2 * Math.PI; phi += 0.02) 
                {

                    double cosA = Math.cos(A), sinA = Math.sin(A);
                    double cosB = Math.cos(B), sinB = Math.sin(B);

                    double costheta = Math.cos(theta), sintheta = Math.sin(theta);
                    double cosphi = Math.cos(phi), sinphi = Math.sin(phi);

                    double circlex = 2 + costheta;
                    double circley = sintheta;

                    double x = circlex * (cosB * cosphi + sinA * sinB * sinphi) - circley * cosA * sinB;

                    double y = circlex * (sinB * cosphi - sinA * cosB * sinphi) + circley * cosA * cosB;

                    double z = cosA * circlex * sinphi + circley * sinA + 5;
                    
                    double ooz = 1 / z;

                    int xp = (int)(width / 2 + 150 * ooz * x);
                    int yp = (int)(height / 2 - 150 * ooz * y);

                    int idx = xp + yp * width;

                    double L = cosphi * costheta * sinB
                             - cosA * costheta * sinphi
                             - sinA * sintheta
                             + cosB * (cosA * sintheta - costheta * sinA * sinphi);

                    if (idx >= 0 && idx < width * height) 
                    {
                        
                        if (ooz > zbuffer[idx]) 
                        {
                            zbuffer[idx] = ooz;

                            // brightness
                            int brightness = (int) (L * 255);
                            brightness = Math.max(0, Math.min(255, brightness));

                            int color = (brightness << 16) | (brightness << 8) | brightness;

                            img.setRGB(xp, yp, color);
                        }
                    }
                }
            }

            g.drawImage(img, 0, 0, null);
        }
    }
}

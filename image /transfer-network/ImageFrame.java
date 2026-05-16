import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageFrame extends JFrame
{

    final private JLabel imageLabel;
    final private JLabel statusLabel;

    public ImageFrame()
    {

        setTitle("Image Receiver");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        imageLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel = new JLabel("Waiting for image...", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        add(imageLabel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void updateImage(BufferedImage img)
    {

        Image scaled = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
        statusLabel.setText("Image received!");
    }

    public void setStatus(String text)
    {
        statusLabel.setText(text);
    }
}

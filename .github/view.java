
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImageViewer {
  
    private JFrame frame;
    private JButton showButton, exitButton;
    private JLabel imageLabel;
  
    public ImageViewer() 
    {
        
        frame = new JFrame("Image Viewer");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        JPanel buttonPanel = new JPanel();
        showButton = new JButton("Show Image");
        exitButton = new JButton("Exit");
        
        buttonPanel.add(showButton);
        buttonPanel.add(exitButton);
        
        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        frame.add(imageLabel, BorderLayout.CENTER);
        
      
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showImage();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        frame.setVisible(true);
    }
    private void showImage() 
    {
        
        frame.getContentPane().removeAll();
        
        ImageIcon image = new ImageIcon("myimage.jpg"); 
        imageLabel.setIcon(image);
        frame.add(imageLabel, BorderLayout.CENTER);
       
        frame.revalidate();
        frame.repaint();
    }
  
    public static void main(String[] args) 
    {
        new ImageViewer();
    }
}

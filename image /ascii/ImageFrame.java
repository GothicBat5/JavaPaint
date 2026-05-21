import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageFrame extends JFrame {

    private JTextArea textArea;
    private JLabel statusLabel;

    public ImageFrame() {

        setTitle("ASCII Image Converter");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Button
        JButton openButton = new JButton("Open Image");

        // Text area (ASCII output)
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 6));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        // Status
        statusLabel = new JLabel("Ready");

        add(openButton, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Button action
        openButton.addActionListener(e -> {

            File file = FileLoader.chooseImageFile();

            if (file != null) {

                statusLabel.setText("Loading image...");

                BufferedImage img =
                        ImageConverter.loadImage(file);

                String ascii =
                        ImageConverter.convertToASCII(img);

                textArea.setText(ascii);

                statusLabel.setText("Done ✔");
            } else {
                statusLabel.setText("No file selected");
            }
        });

        setVisible(true);
    }
}
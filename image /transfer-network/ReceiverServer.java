import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiverServer
{

    private final ImageFrame frame;
    private static final int PORT = 5000;

    public ReceiverServer(ImageFrame frame)
    {
        this.frame = frame;
    }

    public void startServer()
    {

        new Thread(() -> {

            try (ServerSocket serverSocket = new ServerSocket(PORT))
            {

                frame.setStatus("Server running on port "+ PORT);

                while (true)
                {
                    Socket socket = serverSocket.accept();
                    frame.setStatus("Phone connected.");
                    InputStream inputStream = socket.getInputStream();
                    //Read image directly from stream
                    BufferedImage image = ImageIO.read(inputStream);

                    if (image != null)
                    {
                        frame.updateImage(image);
                    }
                    else {
                        frame.setStatus("Failed to decode image");
                    }
                    socket.close();
                }

            }
            catch (IOException e) {
                frame.setStatus("Server error: " + e.getMessage());
            }
        }).start();
    }
}

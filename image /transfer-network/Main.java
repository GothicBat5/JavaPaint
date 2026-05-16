
public class Main
{
    public static void main(String[] args)
    {

        ImageFrame frame = new ImageFrame();

        ReceiverServer server = new ReceiverServer(frame);

        server.startServer();
    }
}


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";    
    private static final String MSG_SORTIR = "sortir";

    public ServerSocket serverSocket;
    public Socket clientSocket;

    public void connecta() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
        clientSocket = serverSocket.accept();
        System.out.println("Client connectat: " + clientSocket.getInetAddress());
    }

    public void tanca() throws IOException {
        if (clientSocket != null) clientSocket.close();
        if (serverSocket != null) serverSocket.close();
        System.out.println("Servidor aturat.");
    }

}

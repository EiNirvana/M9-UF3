import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortir";
    private ServerSocket serverSocket;

    public void iniciarServidor() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat al port " + PORT);
    }

    public void pararServidor() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            System.out.println("Servidor aturat.");
        }
    }

    public String getNom(ObjectInputStream in) throws IOException, ClassNotFoundException {
        return (String) in.readObject();
    }

    public static void main(String[] args) {
        ServidorXat servidor = new ServidorXat();

        try {
            servidor.iniciarServidor();

            Socket clientSocket = servidor.serverSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());

            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());

            String nomClient = servidor.getNom(input);
            System.out.println("Nom del client: " + nomClient);

            FilServidorXat fil = new FilServidorXat(input, nomClient);
            fil.start();

            // Enviar missatges des de consola
            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
            String missatge;
            while ((missatge = consola.readLine()) != null) {
                output.writeObject(missatge);
                if (missatge.equalsIgnoreCase(MSG_SORTIR)) {
                    break;
                }
            }

            fil.join();  // Esperar que acabi el fil
            clientSocket.close();
            servidor.pararServidor();

        } catch (Exception e) {
            System.err.println("Error al servidor: " + e.getMessage());
        }
    }
}

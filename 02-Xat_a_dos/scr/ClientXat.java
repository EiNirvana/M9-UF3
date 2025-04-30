import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";   

    public Socket socket;
    public ObjectOutputStream output;
    public ObjectInputStream input;
    
    public void connecta() throws IOException {
        socket = new Socket(HOST, PORT);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        System.out.println("Client connectat al servidor en " + HOST + ":" + PORT);
    }

    public void tancarClient() throws IOException {
        if (output != null) output.close();
        if (input != null) input.close();
        if (socket != null) socket.close();
    }

    public void enviarMissatge(String missatge) throws IOException {
        if (output != null) {
            output.writeObject(missatge);
            output.flush();
            System.out.println("Enviat: " + missatge);
        }
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        Scanner scanner = new Scanner(System.in);

        try {
            client.connecta();

            System.out.print("Introdueix el teu nom: ");
            String nom = scanner.nextLine();
            client.enviarMissatge(nom); // Primer missatge: el nom del client

            String missatge;
            do {
                System.out.print("Tu: ");
                missatge = scanner.nextLine();
                client.enviarMissatge(missatge);
            } while (!missatge.equalsIgnoreCase("sortir"));

            client.tancarClient();
            System.out.println("Client tancat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";   

    public Socket socket;
    public PrintWriter streamOut;
    public BufferedReader streamIn;
    
    public void connecta() throws IOException {
        socket = new Socket(HOST, PORT);
        streamOut = new PrintWriter(socket.getOutputStream(), true);
        streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Client connectat al servidor en " + HOST + ":" + PORT);
    }

    public void tancarClient() throws IOException {
        if (streamOut != null) streamOut.close();
        if (socket != null) socket.close();
    }

    public void enviarMissatge(String missatge) {
        if (streamOut != null) {
            streamOut.println(missatge);
            System.out.println("Enviant missatge: " + missatge);
        }
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        try {
            client.connecta();

            client.enviarMissatge("Prova d'enviament 1");
            client.enviarMissatge("Prova d'enviament 2");
            client.enviarMissatge("Adeu!");

            System.out.println("Prem ENTER per tancar...");
            new Scanner(System.in).nextLine();

            client.tancarClient();
            System.out.println("Client tancat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

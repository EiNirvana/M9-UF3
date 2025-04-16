import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int PORT = 7777;
    private static final String HOST = "localhost";

    private Socket socket;
    private PrintWriter out;

    public void connecta() throws IOException {
        socket = new Socket(HOST, PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connectat al servidor.");
    }

    public void envia(String missatge) {
        if (out != null) {
            out.println(missatge);
            System.out.println("Enviat al servidor: " + missatge);
        }
    }

    public void tanca() throws IOException {
        if (out != null) out.close();
        if (socket != null) socket.close();
        System.out.println("Connexió tancada.");
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connecta();

            client.envia("Prova d'enviament 1");
            client.envia("Prova d'enviament 2");
            client.envia("Adeu!");

            System.out.println("Prem ENTER per tancar...");
            new Scanner(System.in).nextLine();

            client.tanca();
            System.out.println("Client tancat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

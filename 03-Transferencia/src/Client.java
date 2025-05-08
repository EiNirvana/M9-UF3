import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String DIR_ARRIBADA = "C:\\Temp";
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public void connectar(String host, int port) {
        try {
            socket = new Socket(host, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            System.out.println("Connexió establerta amb el servidor.");
        } catch (IOException e) {
            System.err.println("Error en connectar al servidor: " + e.getMessage());
        }
    }

    public void rebreFitxers() {
        try (Scanner scanner = new Scanner(System.in)) {

            // Llegim el nom del fitxer que volem rebre
            System.out.print("Introdueix el nom del fitxer a rebre (ruta al servidor): ");
            String nomFitxer = scanner.nextLine();
            dos.writeUTF(nomFitxer); // Enviem el nom al servidor

            // Llegim la mida del fitxer
            int mida = dis.readInt();
            if (mida == -1) {
                System.out.println("El fitxer no s'ha trobat al servidor.");
                return;
            }

            // Llegim els bytes del fitxer
            byte[] dades = new byte[mida];
            dis.readFully(dades);

            // Guardem el fitxer al directori d'arribada
            File desti = new File(DIR_ARRIBADA + File.separator + new File(nomFitxer).getName());
            try (FileOutputStream fos = new FileOutputStream(desti)) {
                fos.write(dades);
                System.out.println("Fitxer desat a: " + desti.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("Error en rebre el fitxer: " + e.getMessage());
        }
    }

    public void tancarConnexio() {
        try {
            if (dis != null) dis.close();
            if (dos != null) dos.close();
            if (socket != null) socket.close();
            System.out.println("Connexió tancada.");
        } catch (IOException e) {
            System.err.println("Error en tancar la connexió: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connectar("localhost", 9999);
        client.rebreFitxers();
        client.tancarConnexio();
    }
}

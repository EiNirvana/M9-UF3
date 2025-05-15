
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortir";
    private static boolean sortir = false;

    private ServerSocket serverSocket;
    private Hashtable<String, GestorClients> clients = new Hashtable<>();
    
    public static void main(String[] args) {
        ServidorXat servidor = new ServidorXat();
        servidor.servidorAEscoltar();
    }

    public void servidorAEscoltar() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor escoltant al port " + PORT);

            while (!sortir) {
                Socket clientSocket = serverSocket.accept();
                GestorClients client = new GestorClients(clientSocket, this);
                client.start();
            }

        } catch (IOException e) {
            System.out.println("Error en escoltar: " + e.getMessage());
        }
    }
    public void pararServidor() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error en parar el servidor: " + e.getMessage());
        }
    }

    public void finalitzarXat() {
        enviarMissatgeGrup(MSG_SORTIR);
        clients.clear();
        sortir = true;
        pararServidor();
        System.exit(0);
    }

    public synchronized void afegirClient(GestorClients client) {
        clients.put(client.getNom(), client);
        enviarMissatgeGrup("[XAT] " + client.getNom() + " ha entrat al xat.");
    }

    public synchronized void eliminarClient(String nom) {
        if (clients.containsKey(nom)) {
            clients.remove(nom);
            enviarMissatgeGrup("[XAT] " + nom + " ha sortit del xat.");
        }
    }

    public synchronized void enviarMissatgeGrup(String missatge) {
        for (GestorClients client : clients.values()) {
            client.enviarMissatge(missatge);
        }
    }

    public synchronized void enviarMissatgePersonal(String destinatari, String remitent, String missatge) {
        GestorClients client = clients.get(destinatari);
        if (client != null) {
            client.enviarMissatge("[Privat de " + remitent + "]: " + missatge);
        }
    }
}

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GestorClients extends Thread {

    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServidorXat servidorXat;
    private String nom;
    private boolean sortir = false;

    public GestorClients(Socket client, ServidorXat servidorXat) {
        this.client = client;
        this.servidorXat = servidorXat;

        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("Error creant fluxos: " + e.getMessage());
        }
    }

    public String getNom() {
        return nom;
    }

    public void enviarMissatge(String missatge) {
        try {
            out.writeObject(missatge);
            out.flush();
        } catch (IOException e) {
            System.out.println("Error enviant missatge a " + nom + ": " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (!sortir) {
                String missatge = (String) in.readObject();
                processaMissatge(missatge);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connexió amb client perduda: " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("Error tancant socket: " + e.getMessage());
            }
        }
    }

    private void processaMissatge(String missatge) {
        try {
            String[] parts = missatge.split(";", 4); // Format: CODI;remitent;destinatari;missatge

            String codi = parts[0];

            switch (codi) {
                case Missatge.CODI_CONECTAR:
                    this.nom = parts[1];
                    servidorXat.afegirClient(this);
                    break;

                case Missatge.CODI_SORTIR_CLIENT:
                    sortir = true;
                    servidorXat.eliminarClient(nom);
                    break;

                case Missatge.CODI_SORTIR_TOTS:
                    sortir = true;
                    servidorXat.finalitzarXat();
                    break;

                case Missatge.CODI_MSG_PERSONAL:
                    String remitent = parts[1];
                    String destinatari = parts[2];
                    String contingut = parts[3];
                    servidorXat.enviarMissatgePersonal(destinatari, remitent, contingut);
                    break;

                default:
                    enviarMissatge("[ERROR] Codi desconegut: " + codi);
            }
        } catch (Exception e) {
            enviarMissatge("[ERROR] Format de missatge incorrecte.");
        }
    }
}

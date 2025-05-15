import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean sortir = false;

    public void connecta(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("[INFO] Connectat al servidor.");
        } catch (IOException e) {
            System.out.println("[ERROR] No es pot connectar: " + e.getMessage());
        }
    }

    public void enviarMissatge(String missatge) {
        try {
            out.writeObject(missatge);
            out.flush();
        } catch (IOException e) {
            System.out.println("[ERROR] No s'ha pogut enviar el missatge.");
        }
    }

    public void tancarClient() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("[INFO] Connexió tancada.");
        } catch (IOException e) {
            System.out.println("[ERROR] No es pot tancar correctament: " + e.getMessage());
        }
    }

    public void runLectura() {
        new Thread(() -> {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                while (!sortir) {
                    String missatgeCru = (String) in.readObject();
                    processaMissatge(missatgeCru);
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Connexió perduda.");
            } finally {
                tancarClient();
            }
        }).start();
    }

    private void processaMissatge(String missatge) {
        try {
            String[] parts = missatge.split(";", 4);
            String codi = parts[0];

            switch (codi) {
                case Missatge.CODI_SORTIR_TOTS:
                    System.out.println("[SERVIDOR] Xat tancat per l'administrador.");
                    sortir = true;
                    break;

                case Missatge.CODI_MSG_PERSONAL:
                    System.out.println("[Privat de " + parts[1] + "]: " + parts[3]);
                    break;

                case Missatge.CODI_CONECTAR:
                case Missatge.CODI_MSG_GRUP:
                    System.out.println(parts[3]);
                    break;

                default:
                    System.out.println("[ERROR] Missatge desconegut: " + missatge);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Format incorrecte de missatge.");
        }
    }

    public void ajuda() {
        System.out.println("\nComandes disponibles:");
        System.out.println("1 - Connectar amb nom");
        System.out.println("2 - Enviar missatge personal");
        System.out.println("3 - Enviar missatge grupal");
        System.out.println("4 - Sortir del xat");
        System.out.println("5 - Tancar el servidor per a tothom");
        System.out.println("Enter buit per sortir.\n");
    }

    public String getLinea(Scanner scanner, String missatge, boolean obligatori) {
        String input;
        do {
            System.out.print(missatge + ": ");
            input = scanner.nextLine().trim();
        } while (obligatori && input.isEmpty());
        return input;
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        Scanner scanner = new Scanner(System.in);
        String host = "localhost";
        int port = 9999;

        client.connecta(host, port);
        client.runLectura();
        client.ajuda();

        while (!client.sortir) {
            System.out.print("> ");
            String opcio = scanner.nextLine().trim();

            if (opcio.isEmpty()) {
                client.sortir = true;
                break;
            }

            String msg = "";

            switch (opcio) {
                case "1":
                    String nom = client.getLinea(scanner, "Nom d'usuari", true);
                    msg = Missatge.CODI_CONECTAR + ";" + nom + ";;";
                    break;

                case "2":
                    String remitent = client.getLinea(scanner, "El teu nom", true);
                    String destinatari = client.getLinea(scanner, "Destinatari", true);
                    String textPrivat = client.getLinea(scanner, "Missatge", true);
                    msg = Missatge.CODI_MSG_PERSONAL + ";" + remitent + ";" + destinatari + ";" + textPrivat;
                    break;

                case "3":
                    String remitentG = client.getLinea(scanner, "El teu nom", true);
                    String textGrup = client.getLinea(scanner, "Missatge al grup", true);
                    msg = Missatge.CODI_MSG_GRUP + ";" + remitentG + ";;" + textGrup;
                    break;

                case "4":
                    String remitentSortida = client.getLinea(scanner, "El teu nom", true);
                    msg = Missatge.CODI_SORTIR_CLIENT + ";" + remitentSortida + ";;";
                    client.sortir = true;
                    break;

                case "5":
                    msg = Missatge.CODI_SORTIR_TOTS + ";Admin;;";
                    client.sortir = true;
                    break;

                default:
                    System.out.println("[ERROR] Opció desconeguda.");
            }

            if (!msg.isEmpty()) {
                client.enviarMissatge(msg);
            }
        }

        scanner.close();
        client.tancarClient();
    }
}

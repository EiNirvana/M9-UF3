
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class FilLectorCX {
    private ObjectOutputStream stream;
    private static final String MSG_SORTIR = "sortir";

    public FilLectorCX(ObjectOutputStream stream) {
        this.stream = stream;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            String missatge;
            do {
                System.out.print("Tu: ");
                missatge = scanner.nextLine();
                stream.writeObject(missatge);
                stream.flush();
            } while (!missatge.equalsIgnoreCase(MSG_SORTIR));
        } catch (Exception e) {
            System.err.println("Error al fil lector del client: " + e.getMessage());
        }
    }

}

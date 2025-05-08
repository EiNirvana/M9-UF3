import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Fitxer {

    private String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public byte[] getContingut() throws IOException {
        File file = new File(nom);

        if (!file.exists() || !file.isFile()) {
            throw new IOException("El fitxer no existeix o no és vàlid.");
        }

        contingut = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        int llegits = fis.read(contingut);
        fis.close();

        if (llegits != contingut.length) {
            throw new IOException("No s'han pogut llegir tots els bytes del fitxer.");
        }

        return contingut;
    }

    public String getNom() {
        return nom;
    }
}

import java.io.ObjectInputStream;

public class FilServidorXat extends Thread{
    public ObjectInputStream input;
    private String nom;
    private static final String MSG_SORTIR = "sortir";

    public FilServidorXat(ObjectInputStream input, String nom) {
        this.input = input;
        this.nom = nom;
    }
    
    public void run(){
        try {
            String missatge;
            while((missatge = (String) input.readObject()) != null){
                if (missatge.equalsIgnoreCase(MSG_SORTIR)){
                    System.out.println(nom + " ha sortit del xat.");
                    break;
                }
                System.out.println(nom + ": " + missatge);
            }
        } catch (Exception e) {
            System.err.println("Error al fil del servidor: " + e.getMessage());
        }
    }

}

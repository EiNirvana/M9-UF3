import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";

    public Socket clientSocket;
    public ServerSocket srvSocket;

    public void connectar() throws IOException {
        srvSocket = new ServerSocket(PORT);
        System.out.println("Servidor en marxa a " + HOST + ":" + PORT);
        System.out.println("Esperant connexions a " + HOST + ":" + PORT);
        clientSocket = srvSocket.accept();
        System.out.println("Client connectat: " + clientSocket.getInetAddress());
    }

    public void tancaConexio() throws IOException {
        if (clientSocket != null) clientSocket.close();
        if (srvSocket != null) srvSocket.close();
        System.out.println("Servidor tancat.");
    }

    public void enviarFitxers(Socket socket) throws IOException{
        try {
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

            String nom = dataIn.readUTF();
            File file = new File(nom);

            if (!file.exists() || !file.isFile()) {
                System.out.println("El fitxer no existeix o no és vàlid.");
                dataOut.writeInt(-1);
                return;
            }
            byte[] contingut = new byte[(int) file.length()];

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(contingut, 0, contingut.length);
            bis.close();
    
            dataOut.writeInt(contingut.length);
            dataOut.write(contingut);
            dataOut.flush();
    
            System.out.println("Fitxer '" + nom + "' enviat correctament.");

        } catch (IOException e) {
            System.out.println("Error llegint el fitxer del client: null");
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        try {
            servidor.connectar();
            //servidor.enviarFitxers();
            servidor.tancaConexio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

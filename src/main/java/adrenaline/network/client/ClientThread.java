package adrenaline.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket socket;
    private ObjectInputStream input;

    public ClientThread(Socket socket){
        this.socket = socket;
        try {
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        while(true){
            // Tipo nomeVar = (Tipo)input.readObject():
            // elabora messaggio ricevuto dal server che adesso è in nomeVar

        }
    }
}

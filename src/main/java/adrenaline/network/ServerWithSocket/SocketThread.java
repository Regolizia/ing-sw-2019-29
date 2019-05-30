package adrenaline.network.ServerWithSocket;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketThread implements Runnable {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public SocketThread(Socket socket){
        this.socket= socket;
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            input= new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while(true){
            // Tipo nomeVar = (Tipo)input.readObject():
            // invia messaggio al controller
            try {
                String mex = (String)input.readObject();
                System.out.println(mex);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}

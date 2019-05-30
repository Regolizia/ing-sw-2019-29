package adrenaline.network.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientWithSocket {
    private Socket s;
    private static ObjectOutputStream output;

    public void client(String ip, int port){
        try {
            s = new Socket(ip, port);
            System.out.println("Client online");
            output = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread t = new Thread();
        t.start();
    }

    public static void sendToServer(String message) throws IOException {
        output.writeObject(message);
        output.flush();

    }
}

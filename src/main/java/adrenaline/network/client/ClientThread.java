package adrenaline.network.client;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket socket;

    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    public ClientThread(Socket s) throws IOException {
        this.socket = s;
        this.output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.output.flush();
        this.input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        System.out.println("DONE");
        run();
    }


    @Override
    public void run() {
        while(true){
            try {
                sendToServer("CIAO");
                handleRequest();

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendToServer(String message) throws IOException {
        output.writeObject(message);
        output.flush();

    }
    public String getFromServer() throws IOException {
        String action = "";
        try {
            action = (String)input.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    return action;
    }

    public void handleRequest() {
        String action = "";
        try {
             action = getFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (action) {
            case "LOGIN":
                System.out.println("OK!");
                break;
            case "String":
                break;
            case "GameEvent":
                break;
        }
    }
}

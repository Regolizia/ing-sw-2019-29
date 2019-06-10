package adrenaline.network.client;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Client client;
    private Socket socket;

    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    public ClientThread(Socket s, Client client) throws IOException {
        this.socket = s;
        this.client = client;
        this.output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.output.flush();
        this.input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        run();
    }


    @Override
    public void run() {
        sendToServer("New Client");
        while(true){
            try {

                handleRequest();

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendToServer(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void sendIntToServer(int number){
        try {
            output.writeObject(number);
            output.flush();
        } catch (IOException e){
            e.printStackTrace();

        }
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

        switch (action) {
            case "LOGIN":
                sendToServer(client.login());
                break;
            case "COLOR":
                sendToServer(client.chooseColor(getFromServer()));
                break;
            case "BOARD":
                sendIntToServer(client.chooseBoard());
                break;
            case "START":
                break;
            case "YOURTURN":
                //sendToServer(client.chooseAction());
                break;
            case "END":
                break;
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package adrenaline.network.client;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

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

    public List<String> getListFromServer() throws IOException {
        List<String> action = new LinkedList<>();
        try {
            action = (List<String >)input.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    return action;
    }

    // GETS SHOWMAINMENU() AS PARAMETER
    public void turnAction(String s){
        if(s=="M"){
            //show stuff
            //ASK SERVER STUFF
        }
        if(s=="B"){
            //show stuff
        }
        if(s=="C"){
            //show stuff
        }
        else{
            sendToServer(s); // S OR G OR R
        }
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
            case "ACCEPTED":
                client.waitStart();
                break;
            case "START":
                break;
            case "YOURFIRSTTURN":
                sendIntToServer(client.firstTurn(getListFromServer()));
                break;
            case "YOURTURN":
                turnAction(client.showMainMenu());
                break;
            case "ENDGAME":
                break;
            case "SHOOT":
                break;
            case "GRAB":
                List<String> items = new LinkedList<>();
                items = getListFromServer();
                List<String> cells = new LinkedList<>();
                cells = getListFromServer();
                sendIntToServer(client.grab(items,cells));
                break;
            case "RUN":
                sendIntToServer(client.run(getListFromServer()));
                break;
            case "MESSAGE":
                client.printMessage(action.substring(7));
                break;
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

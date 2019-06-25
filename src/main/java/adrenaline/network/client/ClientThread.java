package adrenaline.network.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        client.getOutput(output);
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


    public void handleRequest() {
        String action = "";
        try {
             action = getFromServer();

        switch (action) {
            case "LOGIN":
                client.login();
                break;
            case "COLOR":
                client.chooseColor(getFromServer());
                break;
            case "BOARD":
                client.chooseBoard();
                break;
            case "ACCEPTED":
                client.waitStart();
                break;
            case "START":
                break;
            case "YOURFIRSTTURN":
                int n = Integer.parseInt(getFromServer());
                List<String> colors = getListFromServer();
                List<String> names = getListFromServer();
                List<String> blueredyellowWeapons = getListFromServer();
                client.boardSetup(n,colors,names,blueredyellowWeapons);    // MAINLY FOR GUI

                client.firstTurn(getListFromServer());
                break;
            case "YOURTURN":
                client.showMainMenu();
                break;
            case "ENDGAME":
                break;
            case "SHOOT":
                break;
            case "CHOOSETARGET":
                client.chooseTarget(getListFromServer());
                break;
            case "CHOOSEANOTHER":
                client.chooseAnother();
                break;
            case "MOVETARGET":
                client.moveTarget();
                break;
            case "TARGETINGSCOPE":
                client.targetingScope();
                break;
            case "CHOOSECELL":
                client.chooseCell(getListFromServer());
                break;
            case "CHOOSEROOM":
                client.chooseRoom(getListFromServer());
                break;
            case "GRAB":
                List<String> items = new LinkedList<>();
                items = getListFromServer();
                List<String> cells = new LinkedList<>();
                cells = getListFromServer();
                client.grab(items,cells);
                break;
            case "PAYMENT":
                client.payment();
                break;
            case "PAYWITHPOWERUP":
                client.payWithPowerup(getFromServer());
                break;
            case "GRABWEAPON":
                client.grabWeapon(getFromServer());
                break;
            case "DROPWEAPON":
                client.dropWeapon(getListFromServer());
                break;
            case "POWERUP":
                client.powerups(getListFromServer());
                break;
            case "CHOOSEWEAPON":
                client.chooseWeapon(getListFromServer());
                break;
            case "RUN":
                client.run(getListFromServer());
                break;
            case "MESSAGE":
                client.printMessage(getFromServer());
                break;
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

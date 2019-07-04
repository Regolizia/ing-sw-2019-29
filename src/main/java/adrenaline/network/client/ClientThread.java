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
        } catch (Exception e) {
            //e.printStackTrace();
            socket.close();
            //socket = new Socket();
        }
    return action;
    }

    public List<String> getListFromServer() throws IOException {
        List<String> action = new LinkedList<>();
        try {
            action = (List<String >)input.readObject();
        } catch (ClassNotFoundException e) {
            try{
                System.out.println(input.readObject());
            }catch (Exception ee) {
                ee.printStackTrace();
            }
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
            case "FRENZY":
                client.frenzy();
                break;
            case "ACCEPTED":
                client.waitStart();
                break;
            case "CUBE":
                client.chooseCube();
                break;
            case "START":
                break;
            case "YOURFIRSTTURN":
                int n = Integer.parseInt(getFromServer());
                List<String> colors = getListFromServer();
                List<String> names = getListFromServer();
                List<String> blueWeapons = getListFromServer();
                List<String> redWeapons = getListFromServer();
                List<String> yellowWeapons = getListFromServer();
                List<String> items3 = getListFromServer();
                List<String> cells3 = getListFromServer();
                client.boardSetup(n,colors,names,blueWeapons,redWeapons,yellowWeapons,cells3,items3);    // MAINLY FOR GUI

                client.firstTurn(getListFromServer());
                break;
            case "YOURTURN":
                client.showMainMenu();
                break;
            case "FRENZY1":
                client.frenzy1();
                break;
            case "FRENZY2":
                client.frenzy2();
                break;
            case "ENDGAME":
                client.endgame(getListFromServer());
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
            case "TAGBACKGRENADE":
                client.tagbackGrenade();
                break;
            case "TARGETINGSCOPE":
                client.targetingScope();
                break;
            case "RESPAWN":
                client.respawn(getListFromServer());
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
            case "MAP":
                int nn = Integer.parseInt(getFromServer());
                List<String> colorsn = getListFromServer();
                List<String> namesn = getListFromServer();
                List<String> blueWeaponsn = getListFromServer();
                List<String> redWeaponsn = getListFromServer();
                List<String> yellowWeaponsn = getListFromServer();
                List<String> items2 = getListFromServer();
                List<String> cells2 = getListFromServer();
                client.boardSetup(nn,colorsn,namesn,blueWeaponsn,redWeaponsn,yellowWeaponsn,cells2,items2);
                break;
            case "BOARDS":
                List<String> namesp = getListFromServer();
                List<String> drops = getListFromServer();
                List<String> marks = getListFromServer();
                List<String> weapons = getListFromServer();
                List<String> powerups = getListFromServer();
                List<String> ammo = getListFromServer();
                List<String> positions = getListFromServer();
                client.boards(namesp,drops,marks,weapons,powerups,ammo,positions);
                break;
            case "MESSAGE":
                client.printMessage(getFromServer());
                break;
            case "DISCONNECTED":
                client.disconnected();
                break;
            case "OP1":
                client.op1();
                break;
            case "OP2":
                client.op2();
                break;
            case "ALT":
                client.alt();
                break;
            case "RELOAD":
                client.reload(getFromServer());
                break;
            case "CHANGE":
                client.change();
                break;
            case "CHANGEORDER":
                client.changeOrder(getListFromServer());
                break;
            case "SCORE":
                client.score(getListFromServer());
                break;
        }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}

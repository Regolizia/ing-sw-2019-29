package adrenaline.network.client;

// SOCKET
import adrenaline.Figure;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientCLI extends Client{

    static ClientCLI cli;
    public static Socket socket;

    public static Scanner scanner = new Scanner(System.in);

    private static boolean quit = false;

    private ObjectOutputStream output;

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static String serverAddress;
    public static final int socketPort = 4321;
    public static final int rmiPort = 59002;

    public static void main(String[] args){


        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        serverAddress = args[0];

        //serverAddress = "192.168.43.171";//todo change every time

        cli = new ClientCLI(serverAddress, socketPort, rmiPort);

        try {
            new ClientThread(socket,cli).run();
        } catch (IOException e) {
            //e.printStackTrace();
            reconnect();
        }
    }

    public static void startClient(String serverAddress, int socketPort, int rmiPort){
        startSocketClient(serverAddress,socketPort);

    }

    public static void startSocketClient(String serverAddress, int socketPort){
        try {
            socket = new Socket(serverAddress,socketPort);
            System.out.println(ANSI_YELLOW +
                    "          _____  _____  ______ _   _          _      _____ _   _ ______ \n" +
                    "    /\\   |  __ \\|  __ \\|  ____| \\ | |   /\\   | |    |_   _| \\ | |  ____|\n" +
                    "   /  \\  | |  | | |__) | |__  |  \\| |  /  \\  | |      | | |  \\| | |__   \n" +
                    "  / /\\ \\ | |  | |  _  /|  __| | . ` | / /\\ \\ | |      | | | . ` |  __|  \n" +
                    " / ____ \\| |__| | | \\ \\| |____| |\\  |/ ____ \\| |____ _| |_| |\\  | |____ \n" +
                    "/_/    \\_\\_____/|_|  \\_\\______|_| \\_/_/    \\_\\______|_____|_| \\_|______|"+ ANSI_RESET);

            System.out.println("Socket Connection");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ClientCLI(String serverAddress, int socketPort, int rmiPort) {
        startClient(serverAddress,socketPort,rmiPort);

    }
    public static void reconnect(){
        try {
            socket.close();
            socket = new Socket(serverAddress,socketPort);
            new ClientThread(socket,cli).run();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void getOutput(ObjectOutputStream o){
        this.output=o;
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
    public void login(){
         System.out.println("Enter nickname: ");

          sendToServer(scanner.nextLine());

     }
    public String view() {
        return "CLI";
    }

    public void chooseColor(String possibleColors
     ){
         System.out.println("Choose a color: ");
         System.out.println(possibleColors);
        sendToServer(scanner.nextLine());

     }

    public void chooseBoard(){

         System.out.println("Choose board number: 1, 2, 3, 4");
         sendIntToServer(scanner.nextInt());
         scanner.nextLine();
     }


    public void printMessage(String message){
         System.out.println(message);
     }

     public void firstTurn(List<String> list){
         System.out.println("\nChoose one card, its color will decide your respawn coordinates. You'll keep the other one:");
         System.out.println(list.get(0) + " [1] [Default] or " + list.get(1) + " [2]\n");
         if(scanner.hasNextInt()){
             int x = scanner.nextInt();
             scanner.nextLine();
             if(x==2||x==1){
                 sendIntToServer(x);
             }else sendIntToServer(1);
         }else sendIntToServer(1);
     }
     public void respawn(List<String> list){
         System.out.println("\nChoose one card, its color will indicate your respawn but the card will be discarded:");
         for(int i=0; i<list.size();i++) {
             System.out.println("[" + (i + 1) + "]  " +list.get(i));
         }
         if(scanner.hasNextInt()){
             int x = scanner.nextInt();
             scanner.nextLine();
             if(x>=1 && x<=list.size()){
                 sendIntToServer(x);
             }else sendIntToServer(1);
         }else sendIntToServer(1);
     }

    public void showMainMenu() {
            System.out.println("\nMain Menu:\n" +
                    //"Z: Exit\n" +
                    "M: Show Map Info\n" +
                    "B: Show player boards\n" +
                    "C: Show scores\n"+
                    "P: Use one of your powerups\n"+
                            "S: Shoot\n" +
                            "G: Grab\n" +
                            "R: Run [Default]\n"
                    );
            String s = scanner.nextLine();
            s = s.trim().toUpperCase();
                switch (s) {
                    case "M":
                    case "B":
                    case "C":
                    case "P":
                    case "S":
                    case "G":
                    case "R":
                        sendToServer(s);
                        break;
                    default:
                        sendToServer("R");
                        break;
                }

    }

    public static void back() {
        // GO BACK TO MAIN MENU
    }

    public void run(List<String> list) {
        System.out.println("\nChoose a cell (first is default):");
        for(int i=0; i<list.size();i++) {
            System.out.println("[" + (i + 1) + "]  " +list.get(i).toString());
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<list.size()+1){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }
  public void powerups(List<String> list) {
        System.out.println("\nChoose a powerup (first is default):");
        for(int i=0; i<list.size();i++) {
            System.out.println("[" + (i + 1) + "]  " +list.get(i).toString());
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<list.size()+1){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }

    public void grab(List<String> items,List<String> cells) {
        System.out.println("\nChoose an item cell (first is default):");
        for(int i=0; i<items.size();i++) {
            System.out.println("[" + (i + 1) + "]  " +items.get(i).toString()+" in "+cells.get(i).toString());
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<items.size()+1){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }
    public void payment(){
        System.out.println("\nDo you want to pay with just ammo [1] or also with powerups [2]? ([1] is default): ");
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x==1||x==2){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }

    public void payWithPowerup(String powerup){
        System.out.println("\nDo you want to pay with this powerup, [Y] or [N]? ([Y] is default): "+powerup);
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("Y");
        }else sendToServer("Y");
    }

    public void grabWeapon(String weapons){
        String[] list = weapons.split(" ");
        System.out.println("\nChoose a weapon to grab (first is default):");
        for(int i =0;i<list.length;i++) {
            System.out.println(list[i] + " ["+(i+1)+"] ");
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<4){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }

    public void dropWeapon(List<String> weapons){
        System.out.println("\nChoose one of your weapons to drop (first is default):");
        for(int i =0;i<weapons.size();i++) {
            System.out.println(weapons.get(i) + " ["+(i+1)+"] ");
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<4){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }
    public void chooseWeapon(List<String> weapons){
        System.out.println("\nChoose one of your weapons (first is default):");
        for(int i =0;i<weapons.size();i++) {
            System.out.println(weapons.get(i) + " ["+(i+1)+"] ");
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<4){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }

    public void chooseTarget(List<String> targets){
        System.out.println("\nChoose one target (first is default):");
        for(int i =0;i<targets.size();i++) {
            System.out.println(targets.get(i) + " ["+(i+1)+"] ");
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<=targets.size()){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }
    public void changeOrder(List<String> effect){
        System.out.println("\nChoose one effect (first is default):");
        for(int i =0;i<effect.size();i++) {
            System.out.println(effect.get(i) + " ["+(i+1)+"] ");
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<=effect.size()){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }

    public void chooseAnother(){
        System.out.println("\nDo you want to choose another one, [Y] or [N]? ([Y] is default): ");
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("Y");
        }else sendToServer("Y");
    }

    public void op1(){
        System.out.println("\nDo you want to use Optional Effect 1, [Y] or [N]? ([Y] is default): ");
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("Y");
        }else sendToServer("Y");
    }

    public void op2(){
        System.out.println("\nDo you want to use Optional Effect 2, [Y] or [N]? ([Y] is default): ");
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("Y");
        }else sendToServer("Y");
    }
    public void alt(){
        System.out.println("\nDo you want to use Alternate Effect, [Y] or [N]? ([Y] is default): ");
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("Y");
        }else sendToServer("Y");
    }
    public void change(){
        System.out.println("\nDo you want to change the order of the effects, [Y] or [N]? ([Y] is default): ");
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("Y");
        }else sendToServer("Y");
    }

    public void reload(String s){
        System.out.println("\nDo you want to reload " +s +", [Y] or [N]? ([N] is default): ");
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("N");
        }else sendToServer("N");
    }

    public void moveTarget(){
        System.out.println("\nDo you want to move the target, [Y] or [N]? ([Y] is default): ");
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("Y");
        }else sendToServer("Y");
    }
    public void tagbackGrenade(){
        System.out.println("\nYou've been shot, do you want to use your TagbackGrenade, [Y] or [N]? ([N] is default): ");
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("N");
        }else sendToServer("N");
    }

    public void targetingScope(){
        System.out.println("\nDo you want to use you TargetingScope, [Y] or [N]? ([N] is default): ");
        if(scanner.hasNextLine()){
            String x = (String)scanner.nextLine();
            if(x.toUpperCase().equals("Y") ||x.toUpperCase().equals("N")){
                sendToServer(x);
            }else sendToServer("N");
        }else sendToServer("N");
    }

    public void chooseCell(List<String> cells){
        System.out.println("\nChoose a cell (first is default):");
        for(int i =0;i<cells.size();i++) {
            System.out.println("[" + (i + 1) + "]  " +cells.get(i));
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<=cells.size()){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }
    public void chooseRoom(List<String> rooms){
        System.out.println("\nChoose a room (first is default):");
        for(int i =0;i<rooms.size();i++) {
            System.out.println(rooms.get(i) + " ["+i+1+"] ");
        }
        if(scanner.hasNextInt()){
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x>0 && x<=rooms.size()){
                sendIntToServer(x);
            }else sendIntToServer(1);
        }else sendIntToServer(1);
    }


    public static void shoot() {
    }

    public static void showOtherPlayers() {
        // SHOW INFO ABOUT OTHER PLAYERS
    }

    public static void showPlayerBoard() {
        // SHOW INFO ABOUT PLAYER, CARDS ETC...
    }

    public static void showMapInfo() {
        // TODO PRINT BOARD OR SOMETHING
    }

    public void waitStart(){
        System.out.println("Waiting for other players...");
    }

    public static void printPlayerDetails(String playerName, int score, Figure.PlayerColor color) {
        System.out.println("Player: "+"\n"+"Name: " + playerName+"\n"+"Color: " + color+"\n"+"Score: " + score);
    }
    public void disconnected(){
        System.out.println("You've been disconnected!");
        reconnect();
    }

    public void boards(List<String> names, List<String> drops,List<String> weapons,List<String> ammo){
        int i=0;
        for(int y=0;y<names.size();y++){
            System.out.println(names.get(y)+": "+drops.get(i)+" "+drops.get(i+1)+" "+drops.get(i+2)+" "+drops.get(i+3)+" "+
                    drops.get(i+4)+" "+drops.get(i+5)+" "+drops.get(i+6)+" "+
                    drops.get(i+7)+" "+drops.get(i+8)+" "+drops.get(i+9)+" "+drops.get(i+10)+" "+drops.get(i+11));
            i=i+12;
        }
        printWeapons(names,weapons);
        printAmmo(names,ammo);
    }
    public void printWeapons(List<String> names,List<String> weapons){
        int i=0;
        for(int y=0;y<names.size();y++){
            System.out.println(names.get(y)+": "+weapons.get(i)+" "+weapons.get(i+1)+" "+weapons.get(i+2)+" "
                    +weapons.get(i+3)+" "+weapons.get(i+4)+" "+weapons.get(i+5));
            i=i+6;
        }
    }

    public void printAmmo(List<String> names,List<String> ammo){
        int y=0;
        for(int i=0;i<names.size();i++){
            System.out.println(names.get(i)+": BLUE "+ammo.get(y)+" - RED "+ammo.get(y+1)+" - YELLOW "+ammo.get(y+2));
            y=y+3;
        }
    }

    public void score(List<String> scores){
        System.out.println("Scores: ");
        for(int i=0;i<scores.size();i=i+2){
            System.out.println(scores.get(i)+":  "+scores.get(i+1));
        }
    }

    public void endgame(List<String> scores){// name score name score...
        System.out.println("Endgame: ");
        int x =1;
        for(int i=0;i<scores.size();i=i+2){
            System.out.println(x+"°: "+scores.get(i)+" points "+scores.get(i+1));
            x++;
        }
    }

    public void boardSetup(int n, List<String> colors, List<String> names, List<String> blue, List<String> red, List<String> yellow,List<String> cells,List<String> items){
        mapInfo(n);
        System.out.println("\nPlayers:");
        for(int i =0; i<names.size();i++){
            System.out.println(names.get(i) + " ("+colors.get(i)+")");
        }
        System.out.println("\nWeapons of Spawnpoint BLUE:");
        for(int i =0; i<blue.size();i++){
            System.out.println(blue.get(i));
        }
        System.out.println("\nWeapons of Spawnpoint RED:");
        for(int i =0; i<red.size();i++){
            System.out.println(red.get(i));
        }
        System.out.println("\nWeapons of Spawnpoint YELLOW:");
        for(int i =0; i<yellow.size();i++){
            System.out.println(yellow.get(i));
        }
        printItems(cells,items);
    }

    public void printItems(List<String> cells,List<String> items){
        System.out.println("\nItems on map:");
        for(int i =0;i<cells.size();i++){
            System.out.println(cells.get(i) + " "+items.get(i));
        }
    }

    public void mapInfo(int n){
        switch (n){
            case 1:
                System.out.println("MapOne is the first little map in the rules.");
                System.out.println("Room 0 is BLUE -- Room 1 is RED -- Room 2 is WHITE -- Room 3 is YELLOW\n");
                break;
            case 2:
                System.out.println("MapTwo is the big map in the rules near the little maps.");
                System.out.println("Room 0 is BLUE -- Room 1 is RED -- Room 2 is PURPLE -- Room 3 is WHITE -- Room 4 is YELLOW\n");
                break;
            case 3:
                System.out.println("MapThree is the second little map in the rules.");
                System.out.println("Room 0 is BLUE -- Room 1 is RED -- Room 2 is WHITE -- Room 3 is YELLOW -- Room 4 is GREEN\n");
                break;
            case 4:
                System.out.println("MapFour is the third little map in the rules.");
                System.out.println("Room 0 is BLUE -- Room 1 is RED -- Room 2 is WHITE -- Room 3 is YELLOW -- Room 4 is PURPLE -- Room 5 is GREEN\n");
                break;
        }
        System.out.println("To read a cell position:  EXAMPLE 1, 2 Room 0");
        System.out.println("The first number is the x coordinate (from left to right)");
        System.out.println("The second number is the y coordinate (from up to down)");
        System.out.println("The third number is the Room number (see above). It depends on the map chosen.");
    }
}
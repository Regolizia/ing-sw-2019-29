package adrenaline.network.client;

// SOCKET
import adrenaline.Figure;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class ClientCLI extends Client{

    static ClientCLI cli;
    public static Socket socket;

    public static Scanner scanner = new Scanner(System.in);

    private static boolean quit = false;

    public static void main(String[] args) throws Exception {


        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        String serverAddress = args[0];

        //String serverAddress = "192.168.43.171";//todo change every time
        int socketPort = 4321, rmiPort = 59002;

        cli = new ClientCLI(serverAddress, socketPort, rmiPort);

        new ClientThread(socket,cli).run();
    }

    public static void startClient(String serverAddress, int socketPort, int rmiPort){
        startSocketClient(serverAddress,socketPort);

    }

    public static void startSocketClient(String serverAddress, int socketPort){
        try {
            socket = new Socket(serverAddress,socketPort);
            System.out.println("Socket Connection");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ClientCLI(String serverAddress, int socketPort, int rmiPort) {
        startClient(serverAddress,socketPort,rmiPort);

    }
    public String login(){
         System.out.println("Enter nickname: ");

          return scanner.nextLine();

     }

    public String chooseColor(String possibleColors
     ){
         System.out.println("Choose a color: ");
         System.out.println(possibleColors);
         return scanner.nextLine();

     }

    public int chooseBoard(){

         System.out.println("Choose board number: 1, 2, 3, 4");
         return scanner.nextInt();
     }

    public static void chooseSpawnpoint(// TODO SEND Two cards or names
     ){

         System.out.println("Choose a card: ");
         //System.out.println(Cards to print
         String input = scanner.nextLine();
         // TODO SEND input to check, (message CHOOSE SPAWNPOINT)
     }

    public static void printMessage(String message){
         System.out.println(message);
         // TODO change in server (message MESSAGE)
     }

    public String showMainMenu() {
            System.out.println("Main Menu:\n" +
                    //"Z: Exit\n" +
                    "M: Show Map Info\n" +
                    "B: Show your player board\n" +
                    "C: Show other players' information\n" +
                    "A: Perform an action [Default]\n"
                    );
            String s = scanner.nextLine();
            s = s.trim().toUpperCase();
                switch (s) {
                    case "M":
                    case "B":
                    case "C":
                    case "A":return s;
                    default: return "A";
                }

    }

    public String showActionMenu() {
        System.out.println("Action Menu:\n" +
                        "S: Shoot [Default]\n" +
                        "G: Grab\n" +
                        "R: Run\n"
                       // +"B: Go back to Main Menu\n"
        );
        String s = scanner.nextLine();
        s = s.trim().toUpperCase();
        switch (s) {
            case "S":
            case "G":
            case "R": return s;
            default: return "S";
        }
    }

 /*   switch (s) {
        case "S": shoot(); break;
        case "G": grab(); break;
        case "R": run(); break;
        //case "B": back(); break;
        default: break;*/

    public static void back() {
        // GO BACK TO MAIN MENU
    }

    public static void run() {
    }

    public static void grab() {
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

}
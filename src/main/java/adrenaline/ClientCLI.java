package adrenaline;

// SOCKET
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

// RMI


public class ClientCLI {

    public static Scanner scanner = new Scanner(System.in);

    private static ArrayList mainMenu;
    private static boolean quit = false;


    public static void main(String[] args) throws Exception {
        String serverAddress = args[0];
        int socketPort = 59001, rmiPort = 59002;

        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        new ClientCLI(serverAddress, socketPort, rmiPort);
    }


    public ClientCLI(String serverAddress, int socketPort, int rmiPort) {

        System.out.print("[R]MI or [S]ocket? (Default: [S]): ");
        String inText = scanner.nextLine().toUpperCase();

        if (!(inText.equals("S") || inText.equals("R"))){
            inText = "S"; // SOCKET DEFAULT
        }

        boolean connected = false;
        // TODO CONNECTION TO CLIENT
        // START CLIENT
        // client.startClient(inText...
        // inText is the choice if Socket or Rmi

        // IF SUCCESS
        connected=true;

        if(connected){
            ClientCLI.login();
        }

     }

    private static void login(){
         System.out.println("Enter nickname: ");

          String input = scanner.nextLine();
         // TODO SEND input to check, (message ENTER)

     }

     private static void chooseColor(// TODO SEND List of colors
     ){
         System.out.println("Choose a color: ");
         //System.out.println(list of colors); TODO
         String input = scanner.nextLine();
         // TODO SEND input to check, (message CHOOSE COLOR)

         // TODO GET ANSWER IF PLAYER IS FIRST AND DO THIS IF IT IS:
         //chooseBoard();
     }

     private static void chooseBoard(){

         System.out.println("Choose board number: 1, 2, 3, 4");
         String input = scanner.nextLine();
         // TODO SEND input to check, (message CHOOSE BOARD)
     }

     private static void chooseSpawnpoint(// TODO SEND Two cards or names
     ){

         System.out.println("Choose a card: ");
         //System.out.println(Cards to print
         String input = scanner.nextLine();
         // TODO SEND input to check, (message CHOOSE SPAWNPOINT)
     }

     private static void printMessage(String message){
         System.out.println(message);
         // TODO change in server (message MESSAGE)
     }

    public static void showMainMenu() {
        do {
            System.out.println("Main Menu:\n" +
                    "Z: Exit\n" +
                    "S: Show Board Info\n" +
                    "B: Show your player board\n" +
                    "C: Show other players' information\n" +
                    "A: Perform an action\n"
                    //"E: Pass the turn\n" +
                    //"F: Suggest game's interruption\n" +
                    //"I: Show Card Information\n"
                    );
            String s = scanner.nextLine();
            s = s.trim().toUpperCase();
            if (isPresentCommand(mainMenu, s)) {
                switch (s) {
                    case "S": showBoardInfo(); break;
                    case "B": showPlayerBoard(); break;
                    case "C": showOtherPlayers(); break;
                    case "A": showActionMenu(); break;
                    //case "E": passTurn(); break;
                    //case "F": suggestGameInterruption(); break;
                    case "Z": quit = true;
                    System.exit(0);
                        break;
                    //case "I": showCardInformation(); break;
                    default: break;
                }
            }
            else
                System.out.println("Sorry, your choice \""+ s +"\" is not valid\n");
        }while (!quit);
    }

    private static void showActionMenu() {
        System.out.println("Action Menu:\n" +
                        "S: Shoot\n" +
                        "G: Grab\n" +
                        "R: Run\n" +
                        "B: Go back to Main Menu\n"
        );
        String s = scanner.nextLine();
        s = s.trim().toUpperCase();
        switch (s) {
            case "S": shoot(); break;
            case "G": grab(); break;
            case "R": run(); break;
            case "B": back(); break;
            default: break;
        }
    }

    private static void back() {
        // GO BACK TO MAIN MENU
    }

    private static void run() {
    }

    private static void grab() {
    }

    private static void shoot() {
    }

    private static void showOtherPlayers() {
        // SHOW INFO ABOUT OTHER PLAYERS
    }

    private static void showPlayerBoard() {
        // SHOW INFO ABOUT PLAYER, CARDS ETC...
    }

    private static void showBoardInfo() {
        // TODO PRINT BOARD OR SOMETHING
    }


    private static boolean isPresentCommand(ArrayList menu, String s) {
        Optional<String> command = menu.stream().filter(str -> str.equals(s)).findFirst();
        return command.isPresent();
    }

    public static void printPlayerDetails(String playerName, int score, Figure.PlayerColor color) {
        System.out.println("Player: "+"\n"+"Name: " + playerName+"\n"+"Color: " + color+"\n"+"Score: " + score);
    }

}
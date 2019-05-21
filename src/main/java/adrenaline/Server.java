package adrenaline;

// SOCKET
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;
import  java.lang.*;
import java.util.stream.*;

// RMI

public class Server {

    private static GameModel model;

    private static int TIME = 0;
    private static int connectionsCount = 0;
    private static String firstPlayer;
    private static int boardChosen = 0;
    private static boolean gameIsOn = false;

    // STRING LIST OF THE COLORS A PLAYER CAN CHOOSE AND LIST OF THOSE ALREADY CHOSEN
    private static List<String> possibleColors = Stream.of(Figure.PlayerColor.values())
            .map(Figure.PlayerColor::name)
            .collect(Collectors.toList());
    private static ArrayList<String> colorsChosen = new ArrayList<>();
    //String csv = String.join(",", possibleColors); WILL BE USED TO SEND THE LIST AS A STRING TO THE CLIENT

    // All client names, so we can check for duplicates upon registration.
    private static ArrayList<String> names = new ArrayList<>();

    // The set of all the print writers for all the clients, used for broadcast.
    private static List<PrintWriter> writers = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        TIME =Integer.parseInt(args[0]);
        possibleColors.remove("NONE");

        System.out.println("The server is running...");
        var pool = Executors.newFixedThreadPool(500);
        try (var listener = new ServerSocket(59001)) {
           
            while (connectionsCount>=0) {
                pool.execute(new Handler(listener.accept()));
               
            }
        }
    }

    private static class Countdown{
        public Countdown(){
            final Timer timer = new Timer();
            try {
                timer.scheduleAtFixedRate(new TimerTask() {
                    int i = TIME;

                    public void run() {
                        System.out.println(i--);
                        if (i< 0 || connectionsCount<3 || connectionsCount==5 && colorsChosen.size()==5) {
                            if(i<0 || connectionsCount==5 && colorsChosen.size()==5) {
                                System.out.println("Game is starting...");
                                Server.startGame();
                            // DO SOMETHING TO START THE GAME
                            }
                            else if(connectionsCount<3){
                                System.out.println("TIMER STOPPED: LESS THAN 3 CONNECTIONS");
                            }
                            timer.cancel();
                        }
                    }
                }, 0, 1000);

            } catch (Exception e) {
                //
            }

        }
    }


    /**
     * The client handler task.
     */
    private static class Handler implements Runnable {
        private String name;
        private String color;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;


        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                Server.connectionsCount++;
                if(connectionsCount==3){
                    Countdown c = new Countdown();
                }

                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                // Keep requesting a name until we get a unique one.
                while (true) {
                    out.println("ENTER NICKNAME");
                    name = in.nextLine();
                    if (!name.equals("null")) {
                        synchronized (names) {
                            if (!isBlank(name) && !names.contains(name)) {
                                names.add(name);
                                break;
                            } else if (names.contains(name)) {
                                System.out.println("DUPLICATE NAME ");    // WHAT I SEE IN SERVER
                                out.println("DUPLICATE NAME ");   // WHAT I SEND TO CLIENT
                            }
                        }
                    }
                }

                System.out.println("NAME ACCEPTED " + name);    // WHAT I SEE IN SERVER
                out.println("NAME ACCEPTED " + name);   // WHAT I SEND TO CLIENT
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE" + name + " has joined");
                }
                writers.add(out);

                while (true) {
                    String csv = String.join(", ", possibleColors);  // SEND POSSIBLE COLORS
                    out.println("CHOOSE COLOR ");
                    out.println(csv);
                    color = in.nextLine();
                    if (color == null) {
                        System.out.println("color null");
                        return;
                    }
                    synchronized (possibleColors) {
                        if (!possibleColors.isEmpty() && (possibleColors.contains(color.toUpperCase()) || possibleColors.contains(color) )
                                && (colorsChosen.isEmpty() || (!colorsChosen.contains(color) && !colorsChosen.contains(color.toUpperCase())))) {
                            colorsChosen.add(color.toUpperCase());
                            possibleColors.remove(color.toUpperCase());
                            break;
                        }
                        else if((!possibleColors.contains(color.toUpperCase()) || !possibleColors.contains(color) )
                            && !colorsChosen.contains(color.toUpperCase()) && !colorsChosen.contains(color)) {
                            System.out.println("WORD NOT ACCEPTED ");    // WHAT I SEE IN SERVER
                            out.println("WORD NOT ACCEPTED ");   // WHAT I SEND TO CLIENT

                        }
                        else if(colorsChosen.contains(color.toUpperCase()) || colorsChosen.contains(color)){
                            System.out.println("DUPLICATE COLOR ");    // WHAT I SEE IN SERVER
                            out.println("DUPLICATE COLOR ");   // WHAT I SEND TO CLIENT
                        }
                    }
                }
                System.out.println("COLOR ACCEPTED " + color);    // WHAT I SEE IN SERVER
                out.println("COLOR ACCEPTED " + color);   // WHAT I SEND TO CLIENT
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE" + name + " has chosen " + color);
                }

                if(name.equals(names.get(0))){
                    System.out.println("BOARD SELECTION ");
                    System.out.println(names.get(0));
                    while(true){
                        out.println("CHOOSE BOARD ");
                        int result = isInteger(in.nextLine());
                        System.out.println(result);
                            if (result == 1 || result == 2 || result == 3 || result == 4) {
                                Server.setBoardChosen(result);
                                System.out.println("BOARD CHOSEN " + result);
                                Server.firstPlayer = name;
                                break;
                            } else {
                                System.out.println("NOT ACCEPTED, TRY AGAIN");
                                out.println("NOT ACCEPTED, TRY AGAIN");
                            }

                    }
                }

                out.println("MESSAGE" + "Waiting for other players...");



                // TODO DO SOMETHING IF NAME DISCONNECTED, SOMEONE ELSES CHOOSES

                // Accept messages from this client and broadcast them.
                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        System.out.println(writer);
                        writer.println("MESSAGE" + name + ": " + input);
                    }
                }
            } catch (Exception e) {
                // PLAYER DISCONNECTED
                System.out.println(e);
                System.out.println(e.getStackTrace()[0].getLineNumber());
            } finally {
                connectionsCount--;
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println(name + " is leaving");
                    names.remove(name);
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE" + name + " has left");
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
    public static boolean isBlank(String str) {
                int strLen;
                    if (str == null || (strLen = str.length()) == 0) {
                        return true;
                    }
                    for (int i = 0; i < strLen; i++) {
                        if ((Character.isWhitespace(str.charAt(i)) == false)) {
                            return false;
                       }
                    }
                    return true;
    }

    public static int isInteger(String input){
        try{
            int i = Integer.valueOf(input);
            return i;
        }catch(NumberFormatException e){
            return 0;
        }
    }
    public static void setBoardChosen(int i){
        boardChosen = i;
    }

    public static void startGame(){
        gameIsOn = true;

        String pbc = String.join(", ", colorsChosen);  // SEND COLORS
        String pn = String.join(", ", names);  // SEND COLORS

        for (PrintWriter writer : writers) {
            writer.println("MESSAGE" + "The board chosen is number " + boardChosen);
            writer.println("MESSAGE" + "The game is starting...");

            writer.println("PLAYER NAMES ");
            writer.println(pn);
            writer.println("PLAYER BOARDS ");
            writer.println(pbc);
        }

        model = new GameModel(GameModel.Mode.DEATHMATCH, GameModel.Bot.NOBOT,boardChosen);
    }

    public void Turn(){
        //Draw 2 Powerups, choose one
            if (model.getPlayers().get(model.currentPlayer).isFirstTurn()){
                ArrayList<PowerUpCard> twoCards= new ArrayList<>();
                ArrayList<String> twoCardsNames= new ArrayList<>();
                twoCards.add(model.powerUpDeck.deck.removeFirst());
                twoCards.add(model.powerUpDeck.deck.removeFirst());
                twoCardsNames.add(twoCards.get(0).toString());
                twoCardsNames.add(twoCards.get(1).toString());
                String cards = String.join(", ", twoCardsNames);
                writers.get(model.currentPlayer).println("CHOOSE SPAWNPOINT ");
                writers.get(model.currentPlayer).println(cards);

                // GET RESPONSE



            }
            // TODO DO THE STUFF TO MAKE THE ACTIONS

    }

    /**
     * Updates index of next Player.
     * If everybody has played, it resets.
     */
    public void nextPlayer(){
        model.currentPlayer++;
        if(model.currentPlayer==model.getPlayers().size())
            model.currentPlayer=0;
    }
}

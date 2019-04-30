package adrenaline;

// SOCKET
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;
import  java.lang.*;
import java.util.stream.*;

// RMI

public class Server {

    GameModel model;

    private static int TIME = 0;
    private static int connectionsCount = 0;

    // STRING LIST OF THE COLORS A PLAYER CAN CHOOSE AND LIST OF THOSE ALREADY CHOSEN
    private static List<String> possibleColors = Stream.of(Figure.PlayerColor.values())
            .map(Figure.PlayerColor::name)
            .collect(Collectors.toList());
    private static Set<String> colorsChosen = new HashSet<>();
    //String csv = String.join(",", possibleColors); WILL BE USED TO SEND THE LIST AS A STRING TO THE CLIENT

    // All client names, so we can check for duplicates upon registration.
    private static Set<String> names = new HashSet<>();

    // The set of all the print writers for all the clients, used for broadcast.
    private static Set<PrintWriter> writers = new HashSet<>();


    public static void main(String[] args) throws Exception {
        TIME =Integer.parseInt(args[0]);
        possibleColors.remove("NONE");

        System.out.println("The server is running...");
        var pool = Executors.newFixedThreadPool(500);
        try (var listener = new ServerSocket(59001)) {
           //questo è un loop infinito ele
            while (true) {
                pool.execute(new Handler(listener.accept()));
                break;//just to remove bug
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
                        if (i< 0 || connectionsCount<3) {
                            if(i<0) {
                                System.out.println("Game is starting...");
                            // DO SOMETHING TO START THE GAME
                            }
                            else {
                                System.out.println("TIMER STOPPED: LESS THAN 3 CONNECTIONS");
                            }
                            timer.cancel();
                        }
                    }
                }, 0, 1000);

            } catch (Exception e) {
                e.printStackTrace();
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

        /**
         * Constructs a handler thread, squirreling away the socket. All the interesting
         * work is done in the run method. Remember the constructor is called from the
         * server's main method, so this has to be as short as possible.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Services this thread's client by repeatedly requesting a screen name until a
         * unique one has been submitted, then acknowledges the name and registers the
         * output stream for the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
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
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!isBlank(name) && !names.contains(name)) {
                            names.add(name);
                            break;
                        }
                        else if(names.contains(name)){
                            System.out.println("DUPLICATE NAME ");    // WHAT I SEE IN SERVER
                            out.println("DUPLICATE NAME ");   // WHAT I SEND TO CLIENT
                        }
                    }
                }

                // Now that a successful name has been chosen, add the socket's print writer
                // to the set of all writers so this client can receive broadcast messages.
                // But BEFORE THAT, let everyone else know that the new person has joined!
                System.out.println("NAME ACCEPTED " + name);    // WHAT I SEE IN SERVER
                out.println("NAME ACCEPTED " + name);   // WHAT I SEND TO CLIENT
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE" + name + " has joined");
                }
                writers.add(out);

                while (true) {
                    String csv = String.join(",", possibleColors);  // SEND POSSIBLE COLORS
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
                //System.out.println(e);
                //System.out.println(e.getStackTrace()[0].getLineNumber());
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
}
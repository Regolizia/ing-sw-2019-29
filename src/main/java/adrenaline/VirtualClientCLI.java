package adrenaline;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class VirtualClientCLI {

    String serverAddress;
    Scanner in;
    PrintWriter out;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        var client = new VirtualClientCLI(args[0]);
        client.run();
    }

    /**
     * Constructs the client by laying out the GUI and registering a listener with the
     * textfield so that pressing Return in the listener sends the textfield contents
     * to the server. Note however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED message from
     * the server.
     */
    public VirtualClientCLI(String serverAddress) {
        this.serverAddress = serverAddress;
     }

    private void run() throws IOException {

        var socket = new Socket(serverAddress, 59001);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean hasNickname = false;
        String line;
         do {
/* TO HAVE A CHAT, IT SEND THE WORDS YOU WRITE
             if(hasNickname && scanner.hasNextLine()) {
                 input = scanner.nextLine();
                 out.println(input);
                // System.out.println(1);
             }*/
             line = in.nextLine();
            //System.out.println("line: "+line);

             if (line.startsWith("MESSAGE")) {
                 System.out.println(line.substring(7));
              //   System.out.println(4);
             } else

             if (line.startsWith("ENTER")) {
                 //// ALTRIMENTI
                 System.out.println("Enter nickname: ");
                 //Scanner scanner = new Scanner(System.in);
                 input = scanner.nextLine();
                 out.println(input);
              //   System.out.println(2);
             }
             if (line.startsWith("NAME ACCEPTED ")) {
                 System.out.println("Nickname accepted!");
                 hasNickname = true;
               //  System.out.println(3);
             }
             if (line.startsWith("CHOOSE COLOR ")) {
                 System.out.println("Choose a color: ");
                System.out.println(in.nextLine());
                 input = scanner.nextLine();
                 out.println(input);
             }
             if (line.startsWith("COLOR ACCEPTED ")) {
                 System.out.println("Color accepted!");
             }
             if (line.startsWith("DUPLICATE COLOR ")) {
                 System.out.println("You have chosen a color already selected by another player, choose again");
             }
             if (line.startsWith("DUPLICATE NAME ")) {
                 System.out.println("You have entered a nickname already chosen by another player!");
             }
             if (line.startsWith("WORD NOT ACCEPTED ")) {
                 System.out.println("Word not accepted, insert again");
             }
             if (line.startsWith("CHOOSE BOARD ")) {
                 System.out.println("Choose board number: 1, 2, 3, 4");
                 input = scanner.nextLine();
                 out.println(input);
             }
             if (line.startsWith("NOT ACCEPTED, TRY AGAIN")) {
                 System.out.println("Not accepted, insert again");
             }
             if (line.startsWith("CHOOSE SPAWNPOINT ")) {

             }
         }
         while (in.hasNextLine());
        }

    public void printPlayerDetails(String playerName, int score, String color) {
        System.out.println("Player: "+"\n"+"Name: " + playerName+"\n"+"Color: " + color+"\n"+"Score: " + score);
    }

    // UPDATES DIRECTLY FROM THE MODEL WHAT'S GOING ON IN THE GAME
    // PLAYERS, SCORE, LIFE...
   /* public void update() {
        // TODO implement here
    */
}
package adrenaline;

// SOCKET
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// RMI
import java.rmi.Naming;


public class VirtualClientCLI {

    String serverAddress;
    Scanner in;
    PrintWriter out;
    Socket socket;
    boolean isRMI;   // FALSE FOR SOCKET, TRUE FOR RMI

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        var client = new VirtualClientCLI(args[0]);
        client.run();
    }


    public VirtualClientCLI(String serverAddress) {
        this.serverAddress = serverAddress;
     }

    private void run() throws IOException, Exception {
        Scanner scanner = new Scanner(System.in);

        /*while (true) {
            System.out.println("Do you want to use Socket or RMI? s/r");
            String type= scanner.nextLine();
            if(type.equals("s")){
            */    isRMI=false;
                socket = new Socket(serverAddress, 59001);
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
         /*       break;
            } else if (type.equals("r")){

                InterfaceRMI serv = (InterfaceRMI)Naming.lookup("//localhost/RmiServer");
                isRMI=true;
                toSend="true";*//*
                break;
            }
        }*/

            String input;
            String line;
        try{
            do {
                line = in.nextLine();
                //System.out.println("line: "+line);

                if (line.startsWith("MESSAGE")) {
                    System.out.println(line.substring(7));
                    //   System.out.println(4);
                } else if (line.startsWith("ENTER")) {
                 //// ALTRIMENTI
                 System.out.println("Enter nickname: ");
                 //Scanner scanner = new Scanner(System.in);
                 input = scanner.nextLine();
                 out.println(input);
              //   System.out.println(2);
             }
             if (line.startsWith("NAME ACCEPTED ")) {
                 System.out.println("Nickname accepted!");
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
                 System.out.println("Choose a card: ");
                 System.out.println(in.nextLine());
                 input = scanner.nextLine();
                 out.println(input);
             }
         }
         while (in.hasNextLine());
       } finally {
           // if(!isRMI)
                socket.close();
        }
    }
    public void printPlayerDetails(String playerName, int score, String color) {
        System.out.println("Player: "+"\n"+"Name: " + playerName+"\n"+"Color: " + color+"\n"+"Score: " + score);
    }


}
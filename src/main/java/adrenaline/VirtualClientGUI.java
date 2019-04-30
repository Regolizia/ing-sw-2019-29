package adrenaline;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * A simple Swing-based client for the chat server. Graphically it is a frame with a text
 * field for entering messages and a textarea to see the whole dialog.
 *
 * The client follows the following Chat Protocol. When the server sends "SUBMITNAME" the
 * client replies with the desired screen name. The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are already in use. When the
 * server sends a line beginning with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all chatters connected to the
 * server. When the server sends a line beginning with "MESSAGE" then all characters
 * following this string should be displayed in its message area.
 */
//added try finally to close socket

public class VirtualClientGUI {
    private JPanel panel1;
    private JLabel picLabel;

    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("ClientGUI");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 50);


    /**
     * Constructs the client by laying out the GUI and registering a listener with the
     * textfield so that pressing Return in the listener sends the textfield contents
     * to the server. Note however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED message from
     * the server.
     */
    public VirtualClientGUI(String serverAddress) {
        this.serverAddress = serverAddress;

        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.pack();

        // Send on enter then clear to prepare for next message
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    private String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose nickname:",
                "Nickname selection",
                JOptionPane.PLAIN_MESSAGE
        );
    }
    private String getColor() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose color: " + in.nextLine(),
                "Color selection",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void run() throws IOException {
        try {
            var socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            try{
            while (in.hasNextLine()) {
                var line = in.nextLine();
                if (line.startsWith("ENTER")) {
                    out.println(getName());
                } else if (line.startsWith("NAME ACCEPTED")) {
                    this.frame.setTitle("Player: " + line.substring(13)); // FRAME TITLE
                    textField.setEditable(true);
                } else if (line.startsWith("CHOOSE COLOR ")) {
                    out.println(getColor());
                } else if (line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(7) + "\n");
                }
            }}finally {socket.close();}
        } catch (IOException e){
            System.out.println("Couldn't connect to server");
        } finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        var client = new VirtualClientGUI(args[0]);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();


/*
/////////////////////////////// QUESTA PARTE DOVREBBE ANDARE, COMMENTATA PERCHé DEVO RICEVERE LA MAPPA SCELTA
        File partA;
        File partB;

        // CASE MAP SELECTED
        // IT DEPENDS ON THE MAP CHOSEN BY THE FIRST
        int mapChosen; // MAP CHOSEN

       switch(mapChosen) {//number of map chosen by first player)
           case(1):
               partA= new File("resources/images/Part1.jpg");
               partB= new File("resources/images/Part4.jpg");
               break;
           case(2):
               partA= new File("resources/images/Part3.jpg");
               partB= new File("resources/images/Part4.jpg");
               break;
           case(3):
               partA= new File("resources/images/Part1.jpg");
               partB= new File("resources/images/Part2.jpg");
               break;
           case(4):
               partA= new File("resources/images/Part3.jpg");
               partB= new File("resources/images/Part2.jpg");
               break;
       */

///////////////////////// QUESTA PARTE NON VA PERCHé NON LEGGE DALLA CARTELLA RESOURCES
/*

          VirtualClientGUI clientGUI = new VirtualClientGUI();
            File file = clientGUI.getFileFromResources("images/Part1.jpg");

        System.out.println(file.getAbsolutePath());
        System.out.println(file.canRead());
///////////////////////////////////////////////////////////////// non è qui?
        File partA=new File("src/main/resources/images/Part1.jpg"); //just to try
        File partB=new File("src/main/resources/images/Part4.jpg"); //just to try
        BufferedImage left = ImageIO.read(partA);
        BufferedImage right = ImageIO.read(partB);
        JLabel picLabelL = new JLabel(new ImageIcon(left));
        JLabel picLabelR = new JLabel(new ImageIcon(right));
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(600, 400);
        frame.add(picLabelL);
        frame.add(picLabelR);
*/

    }
/*
        // get file from classpath, resources folder
        private File getFileFromResources(String fileName) {

            ClassLoader classLoader = getClass().getClassLoader();

            URL resource = classLoader.getResource(fileName);
            if (resource == null) {
                throw new IllegalArgumentException("file is not found!");
            } else {
                return new File(resource.getFile());
            }

        }*/
/////////////////////////////////////// QUESTA PARTE SERVIRà QUANDO LEGGE IL FILE, SERVE PER COSTRUIRE LA VIEW


  /*  BufferedImage left = ImageIO.read(partA);
    BufferedImage right = ImageIO.read(partB);
    JLabel picLabelL = new JLabel(new ImageIcon(left));
    JLabel picLabelR = new JLabel(new ImageIcon(right));
    JFrame frame = new JFrame();
    // frame.setLayout(new FlowLayout());
    // frame.setSize(600, 400);
    // frame.add(picLabelL);
    // frame.add(picLabelR);*/

    }




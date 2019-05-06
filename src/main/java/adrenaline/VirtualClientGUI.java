package adrenaline;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import javax.swing.ImageIcon;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import java.awt.BorderLayout;
import javax.swing.*;

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

    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("ClientGUI");
    JTextArea messageArea = new JTextArea();
    JLabel labelA;
    JLabel labelB;
    JLabel labelC;
    JLabel labelD;
    ImageIcon imageA;
    ImageIcon imageB;
    Font font;

    public VirtualClientGUI(String serverAddress) {
        this.serverAddress = serverAddress;
        setGameBoardImages(0);
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

   /* // TODO SCELTA MAPPA CON IMMAGINI INVECE CHE NUMERI
    private String getBoard(){
        Image image=GenerateImage.toImage(true);  //this generates an image file
        ImageIcon icon = new ImageIcon(image);
        JLabel thumb = new JLabel();
        thumb.setIcon(icon);
    }*/
   private int getBoard() {
       String[] options = new String[]{"1", "2", "3", "4"};
       return JOptionPane.showOptionDialog(null, "Choose game board: ", "Game board selection",
               JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
               null, options, options[0]) +1 ;  // +1 BECAUSE IT STARTS FROM 0
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
                  //  textField.setEditable(true);
                } else if (line.startsWith("CHOOSE COLOR ")) {
                    out.println(getColor());
                } else if (line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(7) + "\n");
                } else if (line.startsWith("CHOOSE BOARD ")) {
                    out.println(getBoard());
                } else if (line.startsWith("PLAYER BOARDS ")) {
                    var original = in.nextLine();
                    addPlayerBoards(original);
                } else if (line.startsWith("PLAYER NAMES ")) {
                    var original = in.nextLine();
                    addPlayerNames(original);
                }
                if (line.startsWith("MESSAGE" + "The chosen board is number ")) {
                    // SET IMAGES AS BACKGROUND
                    setGameBoardImages(Integer.valueOf(line.substring(34)));
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

    }

    public void addPlayerBoards(String o){
       System.out.println(o);
       int numberOfCommas = o.replaceAll("[^,]","").length();
        System.out.println("commas "+ numberOfCommas);

        String[] singleColors = o.split(",");
        JLabel[] playerBoards = new JLabel[numberOfCommas +1];
        Insets insets = frame.getContentPane().getInsets();
        Dimension size;
        for(int index=0;index<singleColors.length;index++) {
            ImageIcon image = new ImageIcon();
            System.out.println("color->"+singleColors[index]);
            switch (singleColors[index]) {
                case ("GREEN"):
                    image = new ImageIcon("src\\main\\resources\\images\\GREEN.jpg");
                    break;
                case ("BLUE"):
                    image = new ImageIcon("src\\main\\resources\\images\\BLUE.jpg");
                    break;
                case ("PURPLE"):
                    image = new ImageIcon("src\\main\\resources\\images\\PURPLE.jpg");
                    break;
                case ("YELLOW"):
                    image = new ImageIcon("src\\main\\resources\\images\\YELLOW.jpg");
                    break;
                case ("GRAY"):
                    image = new ImageIcon("src\\main\\resources\\images\\GRAY.jpg");
                    break;
            }
            playerBoards[index] = new JLabel(image, SwingConstants.CENTER);

            playerBoards[index].setSize(446, 110);

            size = playerBoards[index].getPreferredSize();
            playerBoards[index].setBounds(1054 + insets.left, insets.top + (index)*image.getIconHeight() + 250,
                    size.width, size.height);
            frame.getContentPane().add(playerBoards[index]);
            playerBoards[index].repaint();
            System.out.println(playerBoards[index].getLocation());
        }
        labelC = new JLabel(new ImageIcon("src\\main\\resources\\images\\area.jpg"), SwingConstants.CENTER);
        labelC.setSize(446,250);
        size = labelC.getPreferredSize();
        labelC.setBounds(1054 + insets.left, insets.bottom,
                size.width, size.height);
        frame.getContentPane().add(labelC);
        labelC.repaint();
        System.out.println("LabelC--> "+labelC.getLocation());

        if(numberOfCommas!=4){
            labelD = new JLabel(new ImageIcon("src\\main\\resources\\images\\area.jpg"), SwingConstants.CENTER);
            labelD.setSize(446,250);
            size = labelC.getPreferredSize();
            labelD.setBounds(1054 + insets.left, 550 + insets.top,
                    size.width, size.height);
            frame.getContentPane().add(labelD);
            labelD.repaint();
        }

        frame.setSize(1500 + frame.getInsets().right + frame.getInsets().left,800+ frame.getInsets().top+ frame.getInsets().bottom);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public void addPlayerNames(String o){
       System.out.println(o);
       int numberOfCommas = o.replaceAll("[^,]","").length();
        System.out.println("commas "+ numberOfCommas);

        String[] singleNames = o.split(",");
        JLabel[] playerNames = new JLabel[numberOfCommas +1];
        Insets insets = frame.getContentPane().getInsets();
        Dimension size;
        for(int index=0;index<singleNames.length;index++) {
            System.out.println("names->"+singleNames[index]);

            playerNames[index] = new JLabel("", SwingConstants.CENTER);
            playerNames[index].setText(singleNames[index]);
            playerNames[index].setOpaque(false);
            playerNames[index].setForeground(Color.black);

            size = playerNames[index].getPreferredSize();
            playerNames[index].setBounds(1054 +40+ insets.left, insets.top + (index)*110 + 250 +10,
                    size.width, size.height);
            frame.getContentPane().add(playerNames[index]);
            playerNames[index].repaint();
            System.out.println(playerNames[index].getLocation());
        }

        frame.setSize(1500 + frame.getInsets().right + frame.getInsets().left,800+ frame.getInsets().top+ frame.getInsets().bottom);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public void setGameBoardImages(int n){
       ImageIcon img = new ImageIcon("src\\main\\resources\\images\\icon.jpg");
        frame.setIconImage(img.getImage());
        frame.getContentPane().setLayout(null);
        frame.setSize(1500 + frame.getInsets().right + frame.getInsets().left,800+ frame.getInsets().top+ frame.getInsets().bottom);
        frame.getContentPane().setBackground(Color.black);
        frame.setResizable(false);
        Insets insets = frame.getContentPane().getInsets();
        Dimension size;
        System.out.println(n);
        if(n!=0) {
            switch(n) {
                case (1):
                    imageA = new ImageIcon("src\\main\\resources\\images\\Part1.jpg");
                    imageB = new ImageIcon("src\\main\\resources\\images\\Part4.jpg");
                    break;
                case (2):
                    imageA = new ImageIcon("src\\main\\resources\\images\\Part3.jpg");
                    imageB = new ImageIcon("src\\main\\resources\\images\\Part4.jpg");
                    break;
                case (3):
                    imageA = new ImageIcon("src\\main\\resources\\images\\Part1.jpg");
                    imageB = new ImageIcon("src\\main\\resources\\images\\Part2.jpg");
                    break;
                case (4):
                default:
                    imageA = new ImageIcon("src\\main\\resources\\images\\Part3.jpg");
                    imageB = new ImageIcon("src\\main\\resources\\images\\Part2.jpg");
                    break;
            }
            labelA = new JLabel(imageA, SwingConstants.CENTER);
            labelB = new JLabel(imageB, SwingConstants.CENTER);

            labelA.setSize(560, 800);
            labelB.setSize(560, 800);

            size = labelA.getPreferredSize();
            labelA.setBounds(insets.left, insets.top,
                    size.width, size.height);
            size = labelB.getPreferredSize();
            labelB.setBounds(496 + insets.left, insets.top,
                    size.width, size.height);
            frame.getContentPane().add(labelA);
            frame.getContentPane().add(labelB);


            labelA.repaint();
            labelB.repaint();
        }

        size = messageArea.getPreferredSize();
        messageArea.setBounds(1054 + insets.left, insets.bottom,
                size.width, size.height);

        messageArea.setOpaque(false);
        messageArea.setForeground(Color.white);

        messageArea.setSize(446,250);
        messageArea.setEditable(false);
        frame.getContentPane().add(messageArea);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if(n==0) {
            frame.validate();
        } else
            frame.revalidate();
        frame.repaint();

        frame.setVisible(true);

   /*     try {
            File fontFile = new File(this.getClass().getResource("\\fonts\\ethnocentric.ttf").toURI());
            fontFile.getAbsolutePath();
            fontFile.canRead();
            //InputStream is = VirtualClientGUI.class.getResourceAsStream("fonts/ethnocentric.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch(FontFormatException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
        frame.setFont(font);
*/
    }


    }




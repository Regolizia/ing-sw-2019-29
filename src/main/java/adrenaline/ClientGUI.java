package adrenaline;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import java.awt.BorderLayout;

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

public class ClientGUI {

    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("ClientGUI");
    JTextArea messageArea = new JTextArea();
    JLabel leftBoard;
    JLabel rightBoard;
    JLabel underMessageArea;
    JLabel emptyRightCorner;
    JLabel adrenalin;
    ImageIcon imageA;
    ImageIcon imageB;
    //MAPS
    JButton buttonA;
    JButton buttonB;
    JButton buttonC;
    JButton buttonD;
    Font font;
    boolean isFirst = false;

    JRadioButton[] radioButtons;
    JButton okButton;
    JTextField questionField;

            JTextField textField= new JTextField(30);
    JTextField messageTextField= new JTextField(42);

    public ClientGUI(String serverAddress) {
        this.serverAddress = serverAddress;
        setGameBoardImages(0);
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
        textField.setEditable(false);
    }

    ///////////////////////

    public void selectOne(String question, String options){

        if(!question.equals("")&&!options.equals("")) {
            messageArea.setVisible(false);

            questionField = new JTextField(question);
            Insets insets = frame.getContentPane().getInsets();
            questionField.setSize(10,20);
            Dimension size = questionField.getPreferredSize();
            questionField.setBackground(Color.DARK_GRAY);
            questionField.setForeground(Color.WHITE);
            questionField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            questionField.setBounds(922 + insets.left +10, insets.top + 10,
                    size.width, size.height);
            frame.getContentPane().add(questionField, BorderLayout.SOUTH);

            okButton = new JButton("OK");
            okButton.setSize(10,20);
            okButton.setForeground(Color.WHITE);
            okButton.setBackground(Color.DARK_GRAY);
            okButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            okButton.setBounds(922 + insets.left + 300, insets.top + 200,
                    size.width, size.height);
            frame.getContentPane().add(okButton, BorderLayout.SOUTH);

            int numberOfCommas = options.replaceAll("[^,] ", "").length();
            System.out.println("commas options " + numberOfCommas);

            String[] singleOptions = options.split(", ");
            radioButtons = new JRadioButton[numberOfCommas + 1];
            // DO

            for(int index=0;index<singleOptions.length;index++) {
                System.out.println("options->"+singleOptions[index]);

                radioButtons[index] = new JRadioButton(singleOptions[index]);
                radioButtons[index].setText(singleOptions[index]);
                radioButtons[index].setOpaque(true);
                radioButtons[index].setForeground(Color.WHITE);
                radioButtons[index].setBackground(Color.DARK_GRAY);
                radioButtons[index].setBorder(javax.swing.BorderFactory.createEmptyBorder());

                size = radioButtons[index].getPreferredSize();
                radioButtons[index].setBounds(922 +10+ insets.left, insets.top + (index)*25 +40,
                        size.width, size.height);
                frame.getContentPane().add(radioButtons[index]);
                radioButtons[index].repaint();
                System.out.println(radioButtons[index].getLocation());
            }
            okButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    for(JRadioButton b : radioButtons) {
                        if (b!=null && b.isSelected()) {

                            System.out.println(b.getText());
                            out.println(b.getText());

                            // TODO MOVE MAYBE
                            restoreMessageArea();

                        }
                    }
                }
            });

        }
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public void restoreMessageArea(){
        for(JRadioButton b : radioButtons){
        if(b!=null) {
            b.setVisible(false);
        }
        }
        okButton.setVisible(false);
        questionField.setVisible(false);
        messageArea.setVisible(true);
    }

    public void setTextField() {
        Insets insets = frame.getContentPane().getInsets();
        textField.setSize(10,20);
        Dimension size = textField.getPreferredSize();
        textField.setBackground(Color.DARK_GRAY);
        textField.setCaretColor(Color.WHITE);
        textField.setForeground(Color.WHITE);
        textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        textField.setBounds(790 + insets.left, insets.top + 505,
                size.width, size.height);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        textField.setEditable(true);
        frame.setVisible(true);
    }

    public void closeTextField(){
    textField.setSize(0,0);
    textField.setEditable(false);
    }

    public void setMessageTextField(String s){
        messageTextField.setText(s);
        messageTextField.setSize(10,20);
        Dimension size = messageTextField.getPreferredSize();
        messageTextField.setForeground(Color.WHITE);
        messageTextField.setBackground(Color.BLACK);
        Insets insets = frame.getContentPane().getInsets();
        messageTextField.setBounds(790 + insets.left, insets.top + 485,
                size.width, size.height);
        frame.getContentPane().add(messageTextField, BorderLayout.SOUTH);
        messageTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        messageTextField.setEditable(false);
        frame.setVisible(true);
    }
    public void setStartImage(){
        adrenalin = new JLabel(new ImageIcon("src\\main\\resources\\images\\adrenalin.jpg"), SwingConstants.CENTER);
        adrenalin.setSize(879,260);
        Dimension size = adrenalin.getPreferredSize();
        Insets insets = frame.getContentPane().getInsets();
        adrenalin.setBounds(216 + insets.left, insets.top + 220,
                size.width, size.height);
        frame.getContentPane().add(adrenalin);
        adrenalin.repaint();
        frame.setVisible(true);
    }
    public void closeMessageTextField(){
        messageTextField.setSize(0,0);
    }
    public void closeStartImage(){
        adrenalin.setSize(0,0);
    }

    public void setMapChoice(){
        isFirst = true;
        buttonA = new JButton(new ImageIcon("src\\main\\resources\\images\\Map1.jpg"));
        buttonA.setActionCommand("1");
        buttonB = new JButton(new ImageIcon("src\\main\\resources\\images\\Map2.jpg"));
        buttonA.setActionCommand("2");
        buttonC = new JButton(new ImageIcon("src\\main\\resources\\images\\Map3.jpg"));
        buttonA.setActionCommand("3");
        buttonD = new JButton(new ImageIcon("src\\main\\resources\\images\\Map4.jpg"));
        buttonA.setActionCommand("4");
        buttonA.setSize(180,137);
        buttonB.setSize(180,137);
        buttonC.setSize(180,137);
        buttonD.setSize(180,137);
        buttonA.setBorder(BorderFactory.createEmptyBorder());
        buttonB.setBorder(BorderFactory.createEmptyBorder());
        buttonC.setBorder(BorderFactory.createEmptyBorder());
        buttonD.setBorder(BorderFactory.createEmptyBorder());
        Dimension size = buttonA.getPreferredSize();
        Insets insets = frame.getContentPane().getInsets();
        buttonA.setBounds(216 + insets.left, insets.top + 500,
                size.width, size.height);
        size = buttonB.getPreferredSize();
        buttonB.setBounds(216 + insets.left + 53 + 180, insets.top + 500,
                size.width, size.height);
        size = buttonC.getPreferredSize();
        buttonC.setBounds(216 + insets.left + 106 + 360, insets.top + 500,
                size.width, size.height);
        size = buttonD.getPreferredSize();
        buttonD.setBounds(216 + insets.left + 159 + 540, insets.top + 500,
                size.width, size.height);
        frame.getContentPane().add(buttonA);
        frame.getContentPane().add(buttonB);
        frame.getContentPane().add(buttonC);
        frame.getContentPane().add(buttonD);
        buttonA.repaint();
        buttonB.repaint();
        buttonC.repaint();
        buttonD.repaint();
        frame.setVisible(true);
        buttonA.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                out.println("1");
            }
        });
        buttonB.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                out.println("2");
            }
        });
        buttonC.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                out.println("3");
            }
        });
        buttonD.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                out.println("4");
            }
        });

    }
    public void closeMapChoice(){
        buttonA.setSize(0,0);
        buttonB.setSize(0,0);
        buttonC.setSize(0,0);
        buttonD.setSize(0,0);
    }
    //////////////////////7

    private void run() throws IOException {
        try {
            var socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            try{
            while (in.hasNextLine()) {
                var line = in.nextLine();
                if (line.startsWith("ENTER")) {

                    // TODO REMOVE
                    selectOne("Choose a target", "Leo, Beba, Fiocco, jbsaeh, jaesbdh, asebhjh, jab");


                    setMessageTextField("Insert nickname: ");
                    setTextField();
                    setStartImage();
                } else if (line.startsWith("NAME ACCEPTED")) {
                    this.frame.setTitle("Player: " + line.substring(13)); // FRAME TITLE
                } else if (line.startsWith("CHOOSE COLOR ")) {
                    setMessageTextField("Choose color: "+ in.nextLine());
                    //setTextField();
                } else if (line.startsWith("DUPLICATE NAME ")) {
                    closeMessageTextField();
                    setMessageTextField("This nickname is already taken, insert again:");
                /*} else if (line.startsWith("WORD NOT ACCEPTED ")) {
                    System.out.println("rjbhkeb");
                    //closeMessageTextField();
                    messageTextField.setText("Word not accepted, insert again:");
                    messageTextField.repaint();
                    //setMessageTextField("Word not accepted, insert again:");
                } else if (line.startsWith("NOT ACCEPTED, TRY AGAIN")) {
                    closeMessageTextField();
                    setMessageTextField("Not accepted, insert again:");*/
                } else if (line.startsWith("COLOR ACCEPTED ")) {
                    closeTextField();
                    closeMessageTextField();
                } else if (line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(7) + "\n");
                    messageArea.setCaretPosition(messageArea.getText().length() - 1);
                } else if (line.startsWith("CHOOSE BOARD ")) {
                    setMapChoice();
                } else if (line.startsWith("PLAYER BOARDS ")) {
                    var original = in.nextLine();
                    addPlayerBoards(original);
                } else if (line.startsWith("PLAYER NAMES ")) {
                    var original = in.nextLine();
                    addPlayerNames(original);
                }
                if (line.startsWith("MESSAGE" + "The board chosen is number ")) {
                    // SET IMAGES AS BACKGROUND
                    closeStartImage();
                    selectOne("","");
                    setGameBoardImages(Integer.valueOf(line.substring(34)));
                }
                if (line.startsWith("MESSAGE" + "Waiting for other players...")) {
                    if(isFirst)
                    closeMapChoice();
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
        var client = new ClientGUI(args[0]);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();

    }

    public void addPlayerBoards(String o){
       System.out.println(o);
       int numberOfCommas = o.replaceAll("[^,] ","").length();
        System.out.println("commas "+ numberOfCommas);

        String[] singleColors = o.split(", ");
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

            playerBoards[index].setSize(390, 96);

            size = playerBoards[index].getPreferredSize();
            playerBoards[index].setBounds(922 + insets.left, insets.top + (index)*image.getIconHeight() + 220,
                    size.width, size.height);
            frame.getContentPane().add(playerBoards[index]);
            playerBoards[index].repaint();
            System.out.println(playerBoards[index].getLocation());
        }
        underMessageArea = new JLabel(new ImageIcon("src\\main\\resources\\images\\area.jpg"), SwingConstants.CENTER);
        underMessageArea.setSize(390,219);
        size = underMessageArea.getPreferredSize();
        underMessageArea.setBounds(922 + insets.left, insets.bottom,
                size.width, size.height);
        frame.getContentPane().add(underMessageArea);
        underMessageArea.repaint();
        System.out.println("LabelC--> "+ underMessageArea.getLocation());

        if(numberOfCommas!=4){
            emptyRightCorner = new JLabel(new ImageIcon("src\\main\\resources\\images\\area.jpg"), SwingConstants.CENTER);
            emptyRightCorner.setSize(390,219);
            size = underMessageArea.getPreferredSize();
            emptyRightCorner.setBounds(922 + insets.left, 481 + insets.top,
                    size.width, size.height);
            frame.getContentPane().add(emptyRightCorner);
            emptyRightCorner.repaint();
        }

        frame.setSize(1312 + frame.getInsets().right + frame.getInsets().left,700+ frame.getInsets().top+ frame.getInsets().bottom);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public void addPlayerNames(String o){
       System.out.println(o);
       int numberOfCommas = o.replaceAll("[^,] ","").length();
        System.out.println("commas "+ numberOfCommas);

        String[] singleNames = o.split(",");
        JTextField[] playerNames = new JTextField[numberOfCommas +1];
        Insets insets = frame.getContentPane().getInsets();
        Dimension size;
        for(int index=0;index<singleNames.length;index++) {
            System.out.println("names->"+singleNames[index]);

            playerNames[index] = new JTextField("", SwingConstants.CENTER);
            playerNames[index].setText(singleNames[index]);
            playerNames[index].setOpaque(true);
            playerNames[index].setEditable(false);
            playerNames[index].setForeground(Color.WHITE);
            playerNames[index].setBackground(Color.GRAY);
            playerNames[index].setBorder(javax.swing.BorderFactory.createEmptyBorder());

            size = playerNames[index].getPreferredSize();
            playerNames[index].setBounds(922 +35+ insets.left, insets.top + (index)*96 + 220 +10,
                    size.width, size.height);
            frame.getContentPane().add(playerNames[index]);
            playerNames[index].repaint();
            System.out.println(playerNames[index].getLocation());
        }

        frame.setSize(1312 + frame.getInsets().right + frame.getInsets().left,700+ frame.getInsets().top+ frame.getInsets().bottom);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public void setGameBoardImages(int n){
       ImageIcon img = new ImageIcon("src\\main\\resources\\images\\icon.jpg");
        frame.setIconImage(img.getImage());
        frame.getContentPane().setLayout(null);
        frame.setSize(1312 + frame.getInsets().right + frame.getInsets().left,700+ frame.getInsets().top+ frame.getInsets().bottom);
        System.out.println("top "+frame.getInsets().top);
        System.out.println("bottom "+frame.getInsets().bottom);
        System.out.println("left "+frame.getInsets().left);
        System.out.println("right "+frame.getInsets().right);
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
            leftBoard = new JLabel(imageA, SwingConstants.CENTER);
            rightBoard = new JLabel(imageB, SwingConstants.CENTER);

            leftBoard.setSize(434, 700);
            rightBoard.setSize(488, 700);

            size = leftBoard.getPreferredSize();
            leftBoard.setBounds(insets.left, insets.top,
                    size.width, size.height);
            size = rightBoard.getPreferredSize();
            rightBoard.setBounds(434 + insets.left, insets.top,
                    size.width, size.height);
            frame.getContentPane().add(leftBoard);
            frame.getContentPane().add(rightBoard);


            leftBoard.repaint();
            rightBoard.repaint();
        }
        size = messageArea.getPreferredSize();
        messageArea.setBounds(922 + insets.left, insets.bottom,
                size.width, size.height);

        messageArea.setOpaque(false);
        messageArea.setForeground(Color.white);

        messageArea.setSize(390,220);
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
            //InputStream is = ClientGUI.class.getResourceAsStream("fonts/ethnocentric.ttf");
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




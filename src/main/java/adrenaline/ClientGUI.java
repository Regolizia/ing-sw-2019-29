package adrenaline;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import java.awt.BorderLayout;

public class ClientGUI {

    String serverAddress;
    Scanner in;
    PrintWriter out;

    private JFrame frame = new JFrame("ClientGUI");
    private JTextArea messageArea = new JTextArea();
    private JLabel adrenalin;
    //MAPS
    private JButton buttonA;
    private JButton buttonB;
    private JButton buttonC;
    private JButton buttonD;
    Font font;
    private boolean isFirst = false;

    private JRadioButton[] radioButtons;
    private JButton okButton;
    private JTextField questionField;

    private JTextField textField= new JTextField(30);
    private JTextField messageTextField= new JTextField(42);

    JLabel[] redArray = new JLabel[3];
    JLabel[] blueArray = new JLabel[3];
    JLabel[] yellowArray = new JLabel[3];
    JLabel[] playerCards = new JLabel[3];

    private ClientGUI(String serverAddress) {
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

    private void selectOne(String question, String options){

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

    private void restoreMessageArea(){
        for(JRadioButton b : radioButtons){
        if(b!=null) {
            b.setVisible(false);
        }
        }
        okButton.setVisible(false);
        questionField.setVisible(false);
        messageArea.setVisible(true);
    }

    private void setTextField() {
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

    private void closeTextField(){
    textField.setSize(0,0);
    textField.setEditable(false);
    }

    private void setMessageTextField(String s){
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
    private void setStartImage(){
        adrenalin = new JLabel(new ImageIcon("src\\main\\resources\\images\\adrenalin.jpg"), SwingConstants.CENTER);
        adrenalin.setSize(879,260);
        Dimension size = adrenalin.getPreferredSize();
        Insets insets = frame.getContentPane().getInsets();
        adrenalin.setBounds(216 + insets.left, insets.top + 220,
                size.width, size.height);
        frame.getContentPane().add(adrenalin);
        adrenalin.repaint();
        frame.setSize(1312 + frame.getInsets().right + frame.getInsets().left,700+ frame.getInsets().top+ frame.getInsets().bottom);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
    private void closeMessageTextField(){
        messageTextField.setSize(0,0);
    }
    private void closeStartImage(){
        adrenalin.setSize(0,0);
    }

    private void setMapChoice(){
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
    private void closeMapChoice(){
        buttonA.setSize(0,0);
        buttonB.setSize(0,0);
        buttonC.setSize(0,0);
        buttonD.setSize(0,0);
    }
    //////////////////////7

    private void run(){
        try {
            var socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            try{
            while (in.hasNextLine()) {
                var line = in.nextLine();
                if (line.startsWith("ENTER")) {

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

                    // TODO REMOVE
                    selectOne("Choose a target", "Leo, Beba, Fiocco, jbsaeh, jaesbdh, asebhjh, jab");
                    spawnpointsSetup("GrenadeLauncher, Zx_2, PowerGlove","Railgun, Thor, Furnace","Hellion, Whisper, LockRifle");
                    setupPlayerCards();
                    addPlayerCard("Shotgun");
                    addPlayerCard("Shockwave");
                    addPlayerCard("Flamethrower");
                    /////
                    /// TODO INSERT EMPTY SETUP

                    closeStartImage();
                    selectOne("","");
                    setGameBoardImages(Integer.valueOf(line.substring(34)));



                }
                if (line.startsWith("MESSAGE" + "Waiting for other players...")) {
                    if(isFirst)
                    {closeMapChoice();}
                }
            }}finally {socket.close();}
        } catch (IOException e){
            System.out.println("Couldn't connect to server");
        } finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    public static void main(String[] args){
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        var client = new ClientGUI(args[0]);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();

    }

    private void addPlayerBoards(String o){
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
        JLabel underMessageArea = new JLabel(new ImageIcon("src\\main\\resources\\images\\area.jpg"), SwingConstants.CENTER);
        underMessageArea.setSize(390,219);
        size = underMessageArea.getPreferredSize();
        underMessageArea.setBounds(922 + insets.left, insets.bottom,
                size.width, size.height);
        frame.getContentPane().add(underMessageArea);
        underMessageArea.repaint();
        System.out.println("LabelC--> "+ underMessageArea.getLocation());

        if(numberOfCommas!=4){
            JLabel emptyRightCorner = new JLabel(new ImageIcon("src\\main\\resources\\images\\area.jpg"), SwingConstants.CENTER);
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

    private void addPlayerNames(String o){
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

    private void setGameBoardImages(int n){
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
            ImageIcon imageA;
            ImageIcon imageB;
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
            JLabel leftBoard = new JLabel(imageA, SwingConstants.CENTER);
            JLabel rightBoard = new JLabel(imageB, SwingConstants.CENTER);

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

    // 90 OR -90
    public ImageIcon rotateImag(ImageIcon img, int degrees) {
        BufferedImage bi = new BufferedImage(
                img.getIconWidth(),
                img.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        img.paintIcon(null, g, 0,0);
        g.dispose();

        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.round(bi.getWidth() * cos + bi.getHeight() * sin);
        int newHeight = (int) Math.round(bi.getWidth() * sin + bi.getHeight() * cos);

        BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotate.createGraphics();
        int x = (newWidth - bi.getWidth()) / 2;
        int y = (newHeight - bi.getHeight()) / 2;

        AffineTransform at = new AffineTransform();
        at.setToRotation(radians, x + (bi.getWidth() / 2), y + (bi.getHeight() / 2));
        at.translate(x, y);
        g2d.setTransform(at);

        g2d.drawImage(bi, 0, 0, null);
        g2d.dispose();

        return(new ImageIcon(rotate));
    }

    public void spawnpointsSetup(String red, String blue, String yellow){
        // TODO REMOVE
        redArray = new JLabel[3];
        blueArray = new JLabel[3];
        yellowArray = new JLabel[3];
        ImageIcon img = new ImageIcon();

        int numberOfCommas = blue.replaceAll("[^,] ", "").length();
        System.out.println("commas blue " + numberOfCommas);

        String[] singleWeapons = blue.split(", ");
        for(int i=0; i<3;i++){
            switch (singleWeapons[i]){

                case "Cyberblade":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_cyb.png");
                    break;

                case "Electroscythe":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_ele.png");
                    break;

                case "Flamethrower":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_fla.png");
                    break;

                case "Furnace":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_fur.png");
                    break;

                case "GrenadeLauncher":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_gre.png");
                    break;

                case "Heatseeker":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_hea.png");
                    break;

                case "Hellion":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_hel.png");
                    break;

                case "LockRifle":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_loc.png");
                    break;

                case "MachineGun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_mac.png");
                    break;

                case "PlasmaGun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_pla.png");
                    break;

                case "PowerGlove":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_pow.png");
                    break;

                case "Railgun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_rai.png");
                    break;

                case "RocketLauncher":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_roc.png");
                    break;

                case "Shockwave":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_sho.png");
                    break;

                case "Shotgun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_shot.png");
                    break;

                case "Sledgehammer":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_sle.png");
                    break;

                case "Thor":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_tho.png");
                    break;

                case "TractorBeam":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_tra.png");
                    break;

                case "VortexCannon":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_vor.png");
                    break;

                case "Whisper":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_whi.png");
                    break;

                case "Zx_2":
                    default:
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_zx2.png");
                    break;
            }
            blueArray[i] = new JLabel(img, SwingConstants.CENTER);

            Insets insets = frame.getContentPane().getInsets();
            Dimension size;

            size = blueArray[i].getPreferredSize();
            blueArray[i].setBounds(490 + insets.left + (i)*(img.getIconWidth() + 10), insets.top,
                    size.width, size.height);
            frame.getContentPane().add(blueArray[i]);
            blueArray[i].repaint();
            System.out.println(blueArray[i].getLocation());

        }

        numberOfCommas = red.replaceAll("[^,] ", "").length();
        System.out.println("commas red " + numberOfCommas);

        singleWeapons = red.split(", ");
        for(int i=0; i<3;i++) {
            switch (singleWeapons[i]) {

                case "Cyberblade":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_cyb.png");
                    break;

                case "Electroscythe":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_ele.png");
                    break;

                case "Flamethrower":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_fla.png");
                    break;

                case "Furnace":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_fur.png");
                    break;

                case "GrenadeLauncher":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_gre.png");
                    break;

                case "Heatseeker":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_hea.png");
                    break;

                case "Hellion":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_hel.png");
                    break;

                case "LockRifle":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_loc.png");
                    break;

                case "MachineGun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_mac.png");
                    break;

                case "PlasmaGun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_pla.png");
                    break;

                case "PowerGlove":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_pow.png");
                    break;

                case "Railgun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_rai.png");
                    break;

                case "RocketLauncher":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_roc.png");
                    break;

                case "Shockwave":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_sho.png");
                    break;

                case "Shotgun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_shot.png");
                    break;

                case "Sledgehammer":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_sle.png");
                    break;

                case "Thor":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_tho.png");
                    break;

                case "TractorBeam":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_tra.png");
                    break;

                case "VortexCannon":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_vor.png");
                    break;

                case "Whisper":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_whi.png");
                    break;

                case "Zx_2":
                default:
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_zx2.png");
                    break;
            }

            redArray[i] = new JLabel(rotateImag(img, 270), SwingConstants.CENTER);

            Insets insets = frame.getContentPane().getInsets();
            Dimension size;

            size = redArray[i].getPreferredSize();
            redArray[i].setBounds(insets.left , insets.top + 260 + (i) * (img.getIconWidth() + 10),
                    size.width, size.height);
            frame.getContentPane().add(redArray[i]);
            redArray[i].repaint();
            System.out.println(redArray[i].getLocation());
        }

        numberOfCommas = yellow.replaceAll("[^,] ", "").length();
        System.out.println("commas red " + numberOfCommas);

        singleWeapons = yellow.split(", ");
        for(int i=0; i<3;i++) {
            switch (singleWeapons[i]) {

                case "Cyberblade":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_cyb.png");
                    break;

                case "Electroscythe":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_ele.png");
                    break;

                case "Flamethrower":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_fla.png");
                    break;

                case "Furnace":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_fur.png");
                    break;

                case "GrenadeLauncher":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_gre.png");
                    break;

                case "Heatseeker":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_hea.png");
                    break;

                case "Hellion":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_hel.png");
                    break;

                case "LockRifle":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_loc.png");
                    break;

                case "MachineGun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_mac.png");
                    break;

                case "PlasmaGun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_pla.png");
                    break;

                case "PowerGlove":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_pow.png");
                    break;

                case "Railgun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_rai.png");
                    break;

                case "RocketLauncher":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_roc.png");
                    break;

                case "Shockwave":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_sho.png");
                    break;

                case "Shotgun":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_shot.png");
                    break;

                case "Sledgehammer":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_sle.png");
                    break;

                case "Thor":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_tho.png");
                    break;

                case "TractorBeam":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_tra.png");
                    break;

                case "VortexCannon":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_vor.png");
                    break;

                case "Whisper":
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_whi.png");
                    break;

                case "Zx_2":
                default:
                    img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_zx2.png");
                    break;
            }

            yellowArray[i] = new JLabel(rotateImag(img, 90), SwingConstants.CENTER);

            Insets insets = frame.getContentPane().getInsets();
            Dimension size;

            size = yellowArray[i].getPreferredSize();
            yellowArray[i].setBounds(776 + insets.left , insets.top + 400 + (i) * (img.getIconWidth() + 10),
                    size.width, size.height);
            frame.getContentPane().add(yellowArray[i]);
            yellowArray[i].repaint();
            System.out.println(yellowArray[i].getLocation());
        }
    }

    public void addPlayerCard(String cardName){
        int i;
        for(i=0;i<3;i++) {
            if ((playerCards[i].getIcon()==null)) {
                break;
            }
        }
        ImageIcon img;
        switch (cardName) {

            case "Cyberblade":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_cyb.png");
                break;

            case "Electroscythe":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_ele.png");
                break;

            case "Flamethrower":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_fla.png");
                break;

            case "Furnace":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_fur.png");
                break;

            case "GrenadeLauncher":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_gre.png");
                break;

            case "Heatseeker":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_hea.png");
                break;

            case "Hellion":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_hel.png");
                break;

            case "LockRifle":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_loc.png");
                break;

            case "MachineGun":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_mac.png");
                break;

            case "PlasmaGun":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_pla.png");
                break;

            case "PowerGlove":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_pow.png");
                break;

            case "Railgun":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_rai.png");
                break;

            case "RocketLauncher":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_roc.png");
                break;

            case "Shockwave":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_sho.png");
                break;

            case "Shotgun":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_shot.png");
                break;

            case "Sledgehammer":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_sle.png");
                break;

            case "Thor":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_tho.png");
                break;

            case "TractorBeam":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_tra.png");
                break;

            case "VortexCannon":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_vor.png");
                break;

            case "Whisper":
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_whi.png");
                break;

            case "Zx_2":
            default:
                img = new ImageIcon("src\\main\\resources\\images\\weapons\\w_IT_zx2.png");
                break;
        }

        playerCards[i] = new JLabel(img, SwingConstants.CENTER);

        Insets insets = frame.getContentPane().getInsets();
        Dimension size;

        size = playerCards[i].getPreferredSize();
        playerCards[i].setBounds(insets.left + 6 + (i) * (img.getIconWidth()+ 8), insets.top + 553 ,
                size.width, size.height);
        frame.getContentPane().add(playerCards[i]);
        playerCards[i].repaint();
        System.out.println(playerCards[i].getLocation());
        playerCards[i].setVisible(true);
        // TODO SET VISIBLE(FALSE) WHEN WE REMOVE CARD FROM PLAYER

    }

    public void setupPlayerCards(){
        playerCards[0]= new JLabel();
        playerCards[1]= new JLabel();
        playerCards[2]= new JLabel();
    }
    }




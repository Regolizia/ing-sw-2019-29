package adrenaline;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import java.net.URL;

public class VirtualClientGUI {
    private JPanel panel1;
    private JLabel picLabel;

    public static void main(String avg[]) throws Exception {
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

        VirtualClientGUI clientGUI = new VirtualClientGUI();
        File file = clientGUI.getFileFromResources("images/Part1.jpg");

   /*  TODO SOSTITUIRE CON "." + File . separatorChar + "src" + File . separatorChar + "main"
                + File . separatorChar + "resources" + File . separatorChar + "images"+ File . separatorChar + "Part1.jpg"
           */

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

    }

    // get file from classpath, resources folder
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }
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




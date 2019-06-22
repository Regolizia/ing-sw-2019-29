
package adrenaline.network.client;

import javax.swing.*;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client {

        public static void startClient(String serverAddress, int socketPort, int rmiPort){

        }

        public static void startSocketClient(String serverAddress, int socketPort){
        }

        public void login(){}
        public void chooseColor(String s){}
        public void chooseBoard(){}
        public int run(List<String> s){return 42;}
        public int grab(List<String> items,List<String> cells){return 42;}
        public void firstTurn(List<String> list){}
        public void printMessage(String s){}
        public String showMainMenu(){return "";}
        public void waitStart(){}
        public void boardSetup(int n, List<String> colors, List<String> names){}

        public String view(){return "Client";}
        public void getOutput(ObjectOutputStream o){}
    }


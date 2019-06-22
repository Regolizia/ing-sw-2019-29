
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

        public String login(){return "";}
        public String chooseColor(String s){ return "";}
        public String chooseAction(){return "";}
        public int chooseBoard(){return 42;}
        public int run(List<String> s){return 42;}
        public int grab(List<String> items,List<String> cells){return 42;}
        public int firstTurn(List<String> list){return 42;}
        public void printMessage(String s){}
        public String showMainMenu(){return "";}
        public void waitStart(){}

        public String view(){return "Client";}
        public void getOutput(ObjectOutputStream o){
        }
        public void setLogin(){}
        public void setChooseColor(String s){}
        public void setMapChoice(){}
    }



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
        public void run(List<String> s){}
        public void grab(List<String> items,List<String> cells){}
        public void payment(){}
        public void payWithPowerup(String weapons){}
        public void grabWeapon(String weapons){}
        public void dropWeapon(List<String> weapons){}
        public void chooseWeapon(List<String> weapons){}
        public void chooseTarget(List<String> targets){}
        public void chooseAnother(){}
        public void moveTarget(){}
        public void targetingScope(){}
        public void powerups(List<String> cells){}
        public void chooseCell(List<String> cells){}
        public void chooseRoom(List<String> rooms){}
        public void firstTurn(List<String> list){}
        public void printMessage(String s){}
        public void showMainMenu(){}
        public void waitStart(){}
        public void boardSetup(int n, List<String> colors, List<String> names, List<String> blueredyellow){}

        public String view(){return "Client";}
        public void getOutput(ObjectOutputStream o){}
    }


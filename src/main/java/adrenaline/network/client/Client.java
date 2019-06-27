
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
        public void tagbackGrenade(){}
        public void targetingScope(){}
        public void powerups(List<String> cells){}
        public void chooseCell(List<String> cells){}
        public void respawn(List<String> pows){}
        public void chooseRoom(List<String> rooms){}
        public void firstTurn(List<String> list){}
        public void changeOrder(List<String> list){}
        public void printMessage(String s){}
        public void reload(String s){}
        public void mapInfo(String s){}
        public void showMainMenu(){}
        public void op1(){}
        public void op2(){}
        public void alt(){}
        public void change(){}
        public void disconnected(){}
        public void boards(List<String> names, List<String> drops){}
        public void waitStart(){}
        public void boardSetup(int n, List<String> colors, List<String> names, List<String> blue, List<String> red, List<String> yellow){}

        public String view(){return "Client";}
        public void getOutput(ObjectOutputStream o){}
    }


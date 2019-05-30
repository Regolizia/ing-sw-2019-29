package adrenaline.network.client;

import java.io.IOException;

public class Start {
    public static  void main(String[] args){
        ClientWithSocket client1 = new ClientWithSocket();
        client1.client("localhost",4321);
        try {
            ClientWithSocket.sendToServer("CIAO");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){}
    }
}

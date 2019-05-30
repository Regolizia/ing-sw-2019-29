package adrenaline.network.ServerWithSocket;

import java.io.IOException;

public class Start {
    public static void main(String[] args){
        ServerWithSocket server= ServerWithSocket.getInstance();

        server.server();

    }
}

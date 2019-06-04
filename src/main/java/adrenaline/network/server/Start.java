package adrenaline.network.server;

public class Start {
    public static void main(String[] args){
        ServerWithSocket server= ServerWithSocket.getInstance();

        server.server();

    }
}

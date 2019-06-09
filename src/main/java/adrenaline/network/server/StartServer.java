package adrenaline.network.server;

public class StartServer {
    public static void main(String[] args){
        Server server= Server.getInstance();

        server.server();
        try {
            server.setup(args);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

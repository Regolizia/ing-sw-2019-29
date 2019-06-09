package adrenaline.network.server;

public class StartServer {
    public static void main(String[] args){
        Server server = new Server();
        try {
            server.setup(args);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

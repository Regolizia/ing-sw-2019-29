package adrenaline.network.ServerWithSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerWithSocket {
    private ServerSocket server;
    private ExecutorService threadPool;

    private ServerWithSocket (){
        threadPool= Executors.newCachedThreadPool();
    }

    private static class ServerWithSocketInstance{
        private static final ServerWithSocket SERVER_WITH_SOCKET= new ServerWithSocket();

    }

    public static ServerWithSocket getInstance(){
        return ServerWithSocketInstance.SERVER_WITH_SOCKET;
    }


    public void server() {
        try {
            server = new ServerSocket(4321);
        } catch (IOException e) {
            e.printStackTrace();
            // Server non si avvia
        }
        System.out.println("SERVER ONLINE");
        while(true){
            Socket socket = null;
            try {
                socket = server.accept();

                socket.setKeepAlive(true);
                threadPool.submit(new SocketThread(socket));
            } catch (IOException e) {
                e.printStackTrace();
                // Client disconnesso
            }
        }

    }
}

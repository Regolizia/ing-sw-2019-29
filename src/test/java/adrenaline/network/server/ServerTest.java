package adrenaline.network.server;

import adrenaline.*;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Handler;

import static adrenaline.GameModel.Mode.DEATHMATCH;
import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private Server server;

    @Test
    public void constr(){
        GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1);
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));
        Player player = new Player(c1, Figure.PlayerColor.GREEN);
        Player vic = new Player(c1, Figure.PlayerColor.GREEN);
        LinkedList<Object> p=new LinkedList<>();
        p.add(vic);
        server=new Server();
        Socket socket=new Socket();
        String string[]=new String[47];
       try{ server.setup(string);}catch (Exception e){}
      server.printPlayerAmmo(player);
        try{Server.RequestHandler requestHandler=new Server.RequestHandler(socket);

        requestHandler.sendToClient("");

        }catch (Exception e){}



    }
}
import adrenaline.CoordinatesWithRoom;
import adrenaline.GameBoard;
import adrenaline.Map;
import adrenaline.MapOne;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Scanner;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class OneTileDistanceTest {



    @Test
    public void testConstructor() {
        GameBoard g;
        Map map = new MapOne(DEATHMATCH);
        g=map.getGameBoard();
        CoordinatesWithRoom c = new CoordinatesWithRoom();
        c.setRoom(map.getGameBoard().getRoom(2));
        c.setX(1);
        c.setY(1);
        LinkedList<CoordinatesWithRoom> listOne = c.oneTileDistant(g);

        for(int i=0;i<listOne.size();i++) {
            System.out.printf(listOne.get(i).getX()+","+
                    listOne.get(i).getY()+" Room:"+listOne.get(i).getRoom().getToken()+"\n");

        }



    }
}

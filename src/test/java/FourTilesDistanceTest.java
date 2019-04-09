import adrenaline.CoordinatesWithRoom;
import adrenaline.GameBoard;
import adrenaline.Map;
import adrenaline.MapOne;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class FourTilesDistanceTest {


        @Test
        public void testConstructor() {
            GameBoard g;
            Map map = new MapOne(DEATHMATCH);
            g=map.getGameBoard();
            CoordinatesWithRoom c = new CoordinatesWithRoom();
            c.setRoom(map.getGameBoard().getRoom(1));
            c.setX(2);
            c.setY(1);
            LinkedList<CoordinatesWithRoom> listFour = c.fourTilesDistant(g);

            for(int i=0;i<listFour.size();i++) {
                System.out.printf(listFour.get(i).getX()+","+
                        listFour.get(i).getY()+" Room:"+listFour.get(i).getRoom().getToken()+"\n");

            }



        }
    }



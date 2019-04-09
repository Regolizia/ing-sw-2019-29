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
            LinkedList<CoordinatesWithRoom> listOne = c.XTilesDistant(g,4);

            for (int j = 0; j < listOne.size(); j++) {
                System.out.printf(listOne.get(j).getX() + "," +
                        listOne.get(j).getY() + " Room:" + listOne.get(j).getRoom().getToken() + "\n");

            }



        }
    }



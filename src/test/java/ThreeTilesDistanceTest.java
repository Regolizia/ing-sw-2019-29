import adrenaline.CoordinatesWithRoom;
import adrenaline.GameBoard;
import adrenaline.Map;
import adrenaline.MapOne;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static adrenaline.GameModel.Mode.DEATHMATCH;

 public class ThreeTilesDistanceTest {


        @Test
        public void testConstructor() {
            GameBoard g;
            Map map = new MapOne(DEATHMATCH);
            g=map.getGameBoard();
            CoordinatesWithRoom c = new CoordinatesWithRoom();
            c.setRoom(map.getGameBoard().getRoom(3));
            c.setCoordinates(1,2);
            LinkedList<CoordinatesWithRoom> listOne = c.XTilesDistant(g,3);

            for (int j = 0; j < listOne.size(); j++) {
                System.out.printf(listOne.get(j).getX() + "," +
                        listOne.get(j).getY() + " Room:" + listOne.get(j).getRoom().getToken() + "\n");

            }



        }
    }


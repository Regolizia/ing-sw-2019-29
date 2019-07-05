package adrenaline;

import adrenaline.gameboard.GameBoard;
import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapOne;
import org.junit.jupiter.api.Test;

import java.util.List;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class FourTilesDistanceTest {
    /*map creation, getGameBoard, creation of CoordinatesWithRoom
    *
    * check all coordinatesWithRoom that have 4 tile distance from initial CoordinatesWithRoom
    * */


        @Test
        public void testConstructor() {
            GameBoard g;
            Map map = new MapOne(DEATHMATCH);
            g=map.getGameBoard();
            CoordinatesWithRoom c = new CoordinatesWithRoom();
            c.setRoom(map.getGameBoard().getRoom(1));
            c.setX(2);
            c.setY(1);
            List<CoordinatesWithRoom> listOne = c.xTilesDistant(g,4);

            for (int j = 0; j < listOne.size(); j++) {
                System.out.printf(listOne.get(j).getX() + "," +
                        listOne.get(j).getY() + " Room:" + listOne.get(j).getRoom().getToken() + "\n");

            }



        }
    }



package adrenaline;

import adrenaline.gameboard.GameBoard;
import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapOne;
import org.junit.jupiter.api.Test;

import java.util.List;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class OneTileDistanceTest {


    @Test
    public void testConstructor() {
        GameBoard g;
        Map map = new MapOne(DEATHMATCH);
        g = map.getGameBoard();
        CoordinatesWithRoom c = new CoordinatesWithRoom();
        c.setRoom(map.getGameBoard().getRoom(1));
        c.setX(3);
        c.setY(1);
        List<CoordinatesWithRoom> listTwo = c.xTilesDistant(g, 1);

        for (int j = 0; j < listTwo.size(); j++) {
            System.out.printf(listTwo.get(j).getX() + "," +
                    listTwo.get(j).getY() + " Room:" + listTwo.get(j).getRoom().getToken() + "\n");

        }


    }
}

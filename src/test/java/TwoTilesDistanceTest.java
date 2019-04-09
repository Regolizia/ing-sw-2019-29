import adrenaline.CoordinatesWithRoom;
import adrenaline.GameBoard;
import adrenaline.Map;
import adrenaline.MapOne;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class TwoTilesDistanceTest {


        @Test
        public void testConstructor() {
            GameBoard g;
            Map map = new MapOne(DEATHMATCH);
            g=map.getGameBoard();
            CoordinatesWithRoom c = new CoordinatesWithRoom();
            c.setRoom(map.getGameBoard().getRoom(1));
            c.setX(2);
            c.setY(1);
            LinkedList<CoordinatesWithRoom> listTwo = c.twoTilesDistant(g);

            for(int i=0;i<listTwo.size();i++) {
                System.out.printf(listTwo.get(i).getX()+","+
                        listTwo.get(i).getY()+" Room:"+listTwo.get(i).getRoom().getToken()+"\n");

            }



        }
    }

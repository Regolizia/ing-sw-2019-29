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
            c.setX(1);
            c.setY(2);
            LinkedList<CoordinatesWithRoom> listThree = c.threeTilesDistant(g);

            for(int i=0;i<listThree.size();i++) {
                System.out.printf(listThree.get(i).getX()+","+
                        listThree.get(i).getY()+" Room:"+listThree.get(i).getRoom().getToken()+"\n");

            }



        }
    }


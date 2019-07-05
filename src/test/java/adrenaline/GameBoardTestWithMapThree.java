package adrenaline;

import adrenaline.gameboard.GameBoard;
import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapThree;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class GameBoardTestWithMapThree {
    /*creationg of map 3 with deathmach gameboard*/
        @Test
        public void testConstructor() {

            Map map = new MapThree(DEATHMATCH);
            RoomDom r = new RoomDom(1,1);
            RoomDeath t = new RoomDeath(1,1);
            GameBoard g = new GameBoard();
            g.addRoom(t);
            g.addRoom(r);
            g.getRoom(1);
        }

    }

package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapFour;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;

    public class GameBoardTestWithMapFour {
        /*testing map 4 DEATHMATCH Gameboard*/
        @Test
        public void testConstructor() {

            Map map = new MapFour(DEATHMATCH);


        }

    }

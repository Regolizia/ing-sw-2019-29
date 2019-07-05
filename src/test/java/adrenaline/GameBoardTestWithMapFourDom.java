package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapFour;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DOMINATION;

    public class GameBoardTestWithMapFourDom {
        /*testing map 4 Domination Gameboard*/
        @Test
        public void testConstructor() {

            Map map = new MapFour(DOMINATION);
        }

    }


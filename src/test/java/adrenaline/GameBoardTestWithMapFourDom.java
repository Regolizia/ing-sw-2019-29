package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapFour;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DOMINATION;

    public class GameBoardTestWithMapFourDom {

        @Test
        public void testConstructor() {

            Map map = new MapFour(DOMINATION);
        }

    }


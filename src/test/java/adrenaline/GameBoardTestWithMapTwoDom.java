package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapTwo;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DOMINATION;

public class GameBoardTestWithMapTwoDom {
/*creation of map 2 with domination Gameboard*/
        @Test
        public void testConstructor() {

            Map map = new MapTwo(DOMINATION);
        }

    }
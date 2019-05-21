package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapTwo;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DOMINATION;

public class GameBoardTestWithMapTwoDom {

        @Test
        public void testConstructor() {

            Map map = new MapTwo(DOMINATION);
        }

    }
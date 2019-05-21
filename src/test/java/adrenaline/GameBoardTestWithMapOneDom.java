package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapOne;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DOMINATION;

public class GameBoardTestWithMapOneDom {

    @Test
    public void testConstructor() {

        Map map = new MapOne(DOMINATION);
    }

}

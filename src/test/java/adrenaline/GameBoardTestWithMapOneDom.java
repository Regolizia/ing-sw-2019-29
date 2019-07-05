package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapOne;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DOMINATION;

public class GameBoardTestWithMapOneDom {
/*creation of map 1 with Domination Gameboard*/
    @Test
    public void testConstructor() {

        Map map = new MapOne(DOMINATION);
    }

}

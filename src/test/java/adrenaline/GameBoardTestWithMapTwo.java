package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapTwo;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;


public class GameBoardTestWithMapTwo {
/*creation of map2 with deatchmath Gameboard*/
    @Test
    public void testConstructor() {

        Map map = new MapTwo(DEATHMATCH);
        Map map2 = new Map(DEATHMATCH);
    }

}

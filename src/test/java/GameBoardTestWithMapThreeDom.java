import adrenaline.*;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class GameBoardTestWithMapThreeDom {
    @Test
    public void testConstructor() {

        Map map = new MapThree(DEATHMATCH);
    }

}

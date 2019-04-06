import adrenaline.*;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DOMINATION;

public class GameBoardTestWithMapOneDom {

    @Test
    public void testConstructor() {

        Map map = new MapOne(DOMINATION);
    }

}

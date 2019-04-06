import adrenaline.*;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameBoardTestWithMapOne {

    @Test
    public void testConstructor() {

        Map map = new MapOne(DEATHMATCH);
    }

}

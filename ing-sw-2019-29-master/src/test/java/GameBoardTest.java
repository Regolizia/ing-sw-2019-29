import adrenaline.DeathmatchBoard;
import adrenaline.DominationBoard;
import adrenaline.GameBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GameBoardTest {

    @Test
    public void testConstructor() {

        GameBoard g1;

        g1 = new DominationBoard();
        assertTrue(c1 instanceof DominationBoard);

        g1 = new DeathmatchBoard();
        assertTrue(c1 instanceof DeathmatchBoard);

    }

}

package adrenaline;

import adrenaline.gameboard.GameBoard;
import org.junit.jupiter.api.Test;

public class GameBoardRoomsTest {
/*creation of Gameboard and its Rooms*/
    @Test
    public void testConstructor() {
        GameBoard g = new DeathmatchBoard();
        g.addRoom(new Room());


    }
}

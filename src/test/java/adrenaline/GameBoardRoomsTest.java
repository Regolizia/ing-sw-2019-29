package adrenaline;

import adrenaline.gameboard.GameBoard;
import org.junit.jupiter.api.Test;

public class GameBoardRoomsTest {

    @Test
    public void testConstructor() {
        GameBoard g = new DeathmatchBoard();
        g.addRoom(new Room());


    }
}

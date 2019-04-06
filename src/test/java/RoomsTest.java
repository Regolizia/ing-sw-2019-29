import adrenaline.DeathmatchBoard;
import adrenaline.GameBoard;
import adrenaline.Room;
import adrenaline.RoomDeath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;




public class RoomsTest {

    @Test
    public void testConstructor() {

        GameBoard board = new DeathmatchBoard();
        ((DeathmatchBoard) board).addRoom(new RoomDeath(2,2));
        ((DeathmatchBoard) board).addRoom(new RoomDeath(3,2));
        ((DeathmatchBoard) board).addRoom(new RoomDeath(2,4));
        board.getRoom(0);
        board.getRoom(1);
        board.getRoom(2);

    }
}

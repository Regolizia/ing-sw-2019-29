import adrenaline.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;




public class DoorsTest {

    @Test
    public void testConstructor() {

        GameBoard board = new DeathmatchBoard();
        ((DeathmatchBoard) board).addRoom(new RoomDeath(2,2));
        ((DeathmatchBoard) board).addRoom(new RoomDeath(3,2));
        ((DeathmatchBoard) board).addRoom(new RoomDeath(2,4));
        board.addDoor(new Door(board.getRoom(0),1,1,board.getRoom(2),1,1, Door.Direction.SN));
        board.addDoor(new Door(board.getRoom(0),2,1,board.getRoom(1),1,1, Door.Direction.NS));

    }
}


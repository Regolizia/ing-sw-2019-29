import adrenaline.*;
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
        Coordinates c1 = new Coordinates(1,1);
        board.getRoom(0).addAmmoTile(new AmmoTile(c1, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.BLUE));
        board.getRoom(1);
        board.getRoom(2);

    }
}

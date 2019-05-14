import adrenaline.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;




public class RoomsTest {

    @Test
    public void testConstructor() {
        Coordinates c1 = new Coordinates(1,1);
        GameBoard board = new DeathmatchBoard();
        ((DeathmatchBoard) board).addRoom(new RoomDeath(2,2));
        AmmoTile am = new AmmoTile(AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE);
        board.getRoom(0).addAmmoTile(am);
        LinkedList<AmmoTile> l = board.getRoom(0).getTiles();

        ((DeathmatchBoard) board).addRoom(new RoomDeath(3,2));
        ((DeathmatchBoard) board).addRoom(new RoomDeath(2,4));

        board.getRoom(0).addAmmoTile(new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.BLUE));
        board.getRoom(1);
        board.getRoom(2);
        Room a = new Room();
        Spawnpoint s = new Spawnpoint();
        a.addSpawnpoint(s);
        LinkedList<Spawnpoint> list = a.getSpawnpoints();

    }
}

package adrenaline;

import adrenaline.gameboard.GameBoard;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;




public class RoomsTest {
/*creation of the room*/
    @Test
    public void testConstructor() {
        Coordinates c1 = new Coordinates(1,1);
        GameBoard board = new DeathmatchBoard();
        ((DeathmatchBoard) board).addRoom(new RoomDeath(2,2));
        AmmoTile am = new AmmoTile(AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE);
       // board.getRoom(0).addAmmoTile(am);
        LinkedList<AmmoTile> l = board.getRoom(0).getTiles();

        ((DeathmatchBoard) board).addRoom(new RoomDeath(3,2));
        ((DeathmatchBoard) board).addRoom(new RoomDeath(2,4));

      //  board.getRoom(0).addAmmoTile(new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.BLUE));
        board.getRoom(1);
        board.getRoom(2);
        Room a = new Room();
        List<Spawnpoint> list = a.getSpawnpoints();
        Spawnpoint s=new Spawnpoint(1,2);
        a.addSpawnpoint(s);
        a.addSpawnpointDom(s);
        a.getSpawnpointDom();


    }
}

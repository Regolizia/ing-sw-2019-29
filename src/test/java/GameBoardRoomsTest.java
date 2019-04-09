import adrenaline.DeathmatchBoard;
import adrenaline.GameBoard;
import adrenaline.Room;
import org.junit.jupiter.api.Test;

public class GameBoardRoomsTest {

    @Test
    public void testConstructor() {
        GameBoard g = new DeathmatchBoard();
        g.addRoom(new Room());


    }
}

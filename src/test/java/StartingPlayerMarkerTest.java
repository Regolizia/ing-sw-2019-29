import adrenaline.Player;
import adrenaline.StartingPlayerMarker;
import org.junit.jupiter.api.Test;

public class StartingPlayerMarkerTest {

    @Test
    public void testConstructor() {
        Player p = new Player();
        StartingPlayerMarker s = new StartingPlayerMarker();
        StartingPlayerMarker t = new StartingPlayerMarker(p);
        Player v = t.getOwner();
        s.setOwner(p);
    }




}

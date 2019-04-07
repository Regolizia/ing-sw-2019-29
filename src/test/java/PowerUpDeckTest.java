import adrenaline.PowerUpCard;
import adrenaline.PowerUpDeck;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PowerUpDeckTest {


    @Test
    public void testConstructor() {

        PowerUpDeck deck = new PowerUpDeck();

        assertTrue(deck.getPowerUpDeck().size() == 24);



    }
}

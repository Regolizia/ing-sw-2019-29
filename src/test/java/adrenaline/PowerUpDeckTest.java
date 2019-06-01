package adrenaline;

import adrenaline.AmmoCube;
import adrenaline.PowerUpCard;
import adrenaline.PowerUpDeck;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PowerUpDeckTest {


    @Test
    public void testConstructor() {

        PowerUpDeck deck = new PowerUpDeck();

        assertTrue(deck.getPowerUpDeck().size() == 24);

        deck.getPowerUpDeck().add(new PowerUpCard(AmmoCube.CubeColor.RED));
        PowerUpCard c = new PowerUpCard(AmmoCube.CubeColor.RED);
        AmmoCube.CubeColor a = c.getPowerUpColor();


    }
}

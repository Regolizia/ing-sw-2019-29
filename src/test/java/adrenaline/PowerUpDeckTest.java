package adrenaline;

import adrenaline.AmmoCube;
import adrenaline.PowerUpCard;
import adrenaline.PowerUpDeck;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PowerUpDeckTest {

private PowerUpDeck deck;
    @Test
    public void testConstructor() {

        deck= new PowerUpDeck();

       // assertTrue(deck.getPowerUpDeck().size() == 24);
        System.out.println(deck.getPowerUpDeck().size());
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.pickPowerUp();
        deck.getUsedPowerUp();
        deck.shuffleCards();
        System.out.println(deck.getPowerUpDeck().size());
        deck.pickPowerUp();
        System.out.print(deck.getPowerUpDeck().size());
        PowerUpCard p=deck.pickPowerUp();


    }
}

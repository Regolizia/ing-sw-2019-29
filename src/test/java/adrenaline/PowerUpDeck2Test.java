package adrenaline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpDeck2Test {
    private PowerUpDeck powerUpDeck;

    @BeforeEach
    void setUp() {
        powerUpDeck = new PowerUpDeck();
    }

    @Test
    void pickPowerUp() {
        assertTrue(powerUpDeck.getPowerUpDeck().size()>0);
        PowerUpCard powerUpCard;
        powerUpCard = powerUpDeck.pickPowerUp();
        assertEquals(powerUpCard, powerUpDeck.getPowerUpDeck().getFirst());

    }

    @Test
    void setUsedPowerUp() {
        PowerUpCard powerUpCard = new PowerUpCard();
        powerUpDeck.setUsedPowerUp(powerUpCard);
        assertTrue(powerUpDeck.getUsedPowerUp().contains(powerUpCard));
        powerUpDeck.shuffleUsedPowerUp();
    }

}
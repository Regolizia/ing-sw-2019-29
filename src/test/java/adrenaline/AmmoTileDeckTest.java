package adrenaline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoTileDeckTest {
    private AmmoTileDeck ammoTileDeck;

    @BeforeEach
    void setUp() {
        ammoTileDeck = new AmmoTileDeck();
    }

    @Test
    void setUsedAmmoTile() {
        AmmoTile ammoTile = new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.YELLOW);
        ammoTileDeck.setUsedAmmoTile(ammoTile);

        assertTrue(ammoTileDeck.getUsedAmmoTile().contains(ammoTile));
    }

    @Test
    void pickUpAmmoTile() {
        assertTrue(ammoTileDeck.getDeck().size() > 0);
        AmmoTile ammoTile;
        ammoTile = ammoTileDeck.pickUpAmmoTile();

        assertEquals(ammoTile, ammoTileDeck.getDeck().getFirst());
    }
}
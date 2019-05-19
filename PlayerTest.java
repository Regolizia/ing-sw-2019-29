package adrenaline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        CoordinatesWithRoom coordinatesWithRoom = new CoordinatesWithRoom();
        player = new Player(coordinatesWithRoom, Figure.PlayerColor.BLUE);

    }

    @Test
    void numberOfDeaths() {
        assertEquals(player.numberOfDeaths(),0);
    }
}
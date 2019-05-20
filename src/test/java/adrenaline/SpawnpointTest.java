package adrenaline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpawnpointTest {
    private Spawnpoint spawnpoint;

    @BeforeEach
    void setUp() {
        spawnpoint = new Spawnpoint();
    }

    @Test
    void addWeaponCard() {
        WeaponCard weaponCard = new WeaponCard();
        spawnpoint.addWeaponCard(weaponCard);

    }

    @Test
    void getWeaponCards() {
        WeaponCard weaponCard = new WeaponCard();
        assertFalse(spawnpoint.getWeaponCards().contains(weaponCard));
    }
}
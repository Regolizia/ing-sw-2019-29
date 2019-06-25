package adrenaline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponDeckTest {
    private WeaponDeck weaponDeck;


    @BeforeEach
    void setUp() {
        weaponDeck = new WeaponDeck();
    }


    @Test
    void pickUpWeapon() {
        int i = weaponDeck.getList().size();
        assertTrue(weaponDeck.getList().size() > 0);
        WeaponCard weaponCard;
        weaponCard = weaponDeck.pickUpWeapon();
        int y = weaponDeck.getList().size();
        assertTrue(i==y+1);
    }
}
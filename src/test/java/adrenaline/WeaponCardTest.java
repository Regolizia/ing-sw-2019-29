package adrenaline;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponCardTest {
    /*Weapon card creation and reload's methods*/
    private WeaponCard weaponCard;
    @BeforeEach
    void setUp() {
        weaponCard = new WeaponCard();
    }

    @Test
    void setReload() {
        weaponCard.setReload();

        assertTrue(weaponCard.getReload());
    }


}
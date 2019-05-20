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
    void setUsedWeaponCard(){
       WeaponCard weaponCard = new WeaponCard();
        weaponDeck.setUsedWeaponCard(weaponCard);

        assertTrue(weaponDeck.getUsedWeaponCard().contains(weaponCard));

    }
    @Test
    void pickUpWeapon() {
        assertTrue(weaponDeck.getList().size() > 0);
        WeaponCard weaponCard;
        weaponCard = weaponDeck.pickUpWeapon();

        assertEquals(weaponCard, weaponDeck.getList().getFirst());
    }
}
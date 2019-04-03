package adrenaline;

import java.util.*;

/**
 * 
 */
public class WeaponsDeck extends Deck {

    private LinkedList<WeaponCard> deck;





    /**
     * Default constructor
     */
    public WeaponsDeck() {
        deck =  new LinkedList<WeaponCard>();

        Cyberblade();
        Electroscythe();
        Flamethrower();
        Furnace();


    }

}
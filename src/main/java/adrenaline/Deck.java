package adrenaline;

import java.util.*;

/**
 * Is the class that represents a Deck.
 *
 * @author Eleonora Toscano
 * @version 1.0
 */
public class Deck {

    /**
     * Default constructor
     */
    public Deck() {
    }

    /**
     * Shuffle the given list.
     *
     * @param list the list to shuffle
     */
    public void shuffleCards(List list) {
       Collections.shuffle(list);
    }



}
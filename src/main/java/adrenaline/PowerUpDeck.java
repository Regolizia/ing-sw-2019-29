package adrenaline;

import java.util.*;

/**
 * 
 */
public class PowerUpDeck extends Deck {



    private LinkedList<PowerUpCard> deck;




    public PowerUpDeck() {
        deck = new LinkedList<PowerUpCard>();

        for (int i = 0; i <= 1; i++) {
            deck.add(new Newton(PowerUpCard.CubeColor.RED));
            deck.add(new TagbackGrenade(PowerUpCard.CubeColor.RED));
            deck.add(new TargetingScope(PowerUpCard.CubeColor.RED));
            deck.add(new Teleporter(PowerUpCard.CubeColor.RED));
            deck.add(new Newton(PowerUpCard.CubeColor.YELLOW));
            deck.add(new TagbackGrenade(PowerUpCard.CubeColor.YELLOW));
            deck.add(new TargetingScope(PowerUpCard.CubeColor.YELLOW));
            deck.add(new Teleporter(PowerUpCard.CubeColor.YELLOW));
            deck.add(new Newton(PowerUpCard.CubeColor.BLUE));
            deck.add(new TagbackGrenade(PowerUpCard.CubeColor.BLUE));
            deck.add(new TargetingScope(PowerUpCard.CubeColor.BLUE));
            deck.add(new Teleporter(PowerUpCard.CubeColor.BLUE));
        }
    }


    /**
     * 
     */
    public void pickUpPower() {
        // TODO implement here
    }

}
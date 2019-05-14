package adrenaline;

import adrenaline.powerups.Newton;
import adrenaline.powerups.TagbackGrenade;
import adrenaline.powerups.TargetingScope;
import adrenaline.powerups.Teleporter;

import java.util.*;

/**
 * 
 */
public class PowerUpDeck{



    LinkedList<PowerUpCard> deck;


    public PowerUpDeck() {
        deck = new LinkedList<>();

        for (int i = 0; i <= 1; i++) {
            deck.add(new Newton(AmmoCube.CubeColor.RED));
            deck.add(new TagbackGrenade(AmmoCube.CubeColor.RED));
            deck.add(new TargetingScope(AmmoCube.CubeColor.RED));
            deck.add(new Teleporter(AmmoCube.CubeColor.RED));
            deck.add(new Newton(AmmoCube.CubeColor.YELLOW));
            deck.add(new TagbackGrenade(AmmoCube.CubeColor.YELLOW));
            deck.add(new TargetingScope(AmmoCube.CubeColor.YELLOW));
            deck.add(new Teleporter(AmmoCube.CubeColor.YELLOW));
            deck.add(new Newton(AmmoCube.CubeColor.BLUE));
            deck.add(new TagbackGrenade(AmmoCube.CubeColor.BLUE));
            deck.add(new TargetingScope(AmmoCube.CubeColor.BLUE));
            deck.add(new Teleporter(AmmoCube.CubeColor.BLUE));
        }
    }

    public LinkedList<PowerUpCard> getPowerUpDeck(){
        return deck;
    }

    /**
     * Shuffles the deck.
     */
    void shuffleCards() {
        Collections.shuffle(deck);
    }


}
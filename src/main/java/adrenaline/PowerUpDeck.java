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



    public LinkedList<PowerUpCard> deck;
    public LinkedList<PowerUpCard> usedPowerUp;

    public PowerUpDeck() {
        deck = new LinkedList<>();
        usedPowerUp=new LinkedList<>();
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
    /**
     *getPowerUpDeck
     * @return powerUpDeck
     **/
    public LinkedList<PowerUpCard> getPowerUpDeck(){
        return deck;
    }

    /**
     * Shuffles the deck.
     */
    void shuffleCards() {
        Collections.shuffle(deck);
    }




    /**
     *pickPowerUp
     * @return first PowerUpCard of the deck
     **/
    public PowerUpCard pickPowerUp(){
        if(!deck.isEmpty()){
            setUsedPowerUp(this.deck.getFirst());
            return this.deck.removeFirst();
        }

        else {
            shuffleUsedPowerUp();
            deck.addAll(usedPowerUp);
            usedPowerUp.clear();
            return deck.removeFirst();}
    }
    /**
     *setUsedPowerUp
     * @param powerUp  added to usedPowerUp's deck
     **/
    public void setUsedPowerUp(PowerUpCard powerUp){
        usedPowerUp.add(powerUp);
    }

    /**
     * Shuffles used cards.
     */

    public void shuffleUsedPowerUp() {
        Collections.shuffle(usedPowerUp);
    }

    /**
     *getUsedPowerUp
     * @return usedPowerUp's deck
     **/
    public LinkedList<PowerUpCard> getUsedPowerUp() {
        return usedPowerUp;
    }
}
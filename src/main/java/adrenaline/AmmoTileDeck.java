package adrenaline;

import java.util.Collections;
import java.util.LinkedList;

public class AmmoTileDeck{
    private final static int numMaxAmmoTyleForType = 3;
    private LinkedList<AmmoTile> deck;
    //here i put every AmmoTile which has already been used
    //when deck.size() will be 0 i will shaffle usedAmmotile and then deck=usedAmmotile
    //then i will remove every ammoTile from usedAmmoTile
    private LinkedList<AmmoTile> usedAmmoTile;
    public AmmoTileDeck() {
        LinkedList<AmmoTile>typeOfAmmoTile;
        deck = new LinkedList<AmmoTile>();
        usedAmmoTile=new LinkedList<AmmoTile>();
        typeOfAmmoTile=new LinkedList<AmmoTile>();
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.RED,AmmoCube.CubeColor.BLUE,AmmoCube.CubeColor.POWERUP));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.POWERUP));

        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.RED));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.YELLOW));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.BLUE));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.POWERUP));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.YELLOW));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.BLUE));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.POWERUP));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.RED));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.POWERUP));
        typeOfAmmoTile.add(new AmmoTile(AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.POWERUP));

        for (int i = 0; i <typeOfAmmoTile.size(); i++) {
           for(int j=0;j<numMaxAmmoTyleForType;j++){
               deck.add(typeOfAmmoTile.get(i));
           }



        }
    }
    /**
     *getDeck
     * @return LinkedList<AmmoTile>
     */
public LinkedList<AmmoTile> getDeck(){
        return this.deck;
}
//when there is an empty cell on the map i need to put an ammoTile
    /**
     *pickUpAmmoTIle
     * method to draw an AmmoTile
     * @return AmmoTile
     * if there aren't any AmmoTile in the deck
     * shuffle() whit usedAmmoTile deck
     */
public AmmoTile pickUpAmmoTile(){
        if(!deck.isEmpty()){
            setUsedAmmoTile(this.deck.getFirst());
            return deck.removeFirst();
        }

        else {
            shuffleUsedCards();
            deck.addAll(usedAmmoTile);
            usedAmmoTile.clear();
            return deck.removeFirst();
        }
}
    /**
     *setUsedAmmoTile
     * @param ammotile used AmmoTIle
     */
public void setUsedAmmoTile(AmmoTile ammotile){
        usedAmmoTile.add(ammotile);
}

    /**
     * Shuffles the given list.
     */
    public void shuffleCards() {
        Collections.shuffle(deck);
    }
     /**
     * Shuffles used cards.
     */
    public void shuffleUsedCards() {
        Collections.shuffle(usedAmmoTile);
    }
    /**
     *getUsedAmmoTile
     * @return LinkedList<AmmoTIle> all usedAmmoTiles
     */

    public LinkedList<AmmoTile> getUsedAmmoTile() {
        return usedAmmoTile;
    }
}

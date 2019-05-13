package adrenaline;

import adrenaline.ammoTile.*;

import java.util.LinkedList;

public class AmmoTileDeck extends Deck {
    private final static int numMaxAmmoTyleForType = 3;
    private LinkedList<AmmoTile> deck;
    private LinkedList<AmmoTile>typeOfAmmoTile;
    public AmmoTileDeck() {
        deck = new LinkedList<AmmoTile>();
        typeOfAmmoTile=new LinkedList<AmmoTile>();
       /** typeOfAmmoTile.add(new RedBluePowerUp());
        typeOfAmmoTile.add(new TwoBlueOnePowerUp());
        typeOfAmmoTile.add(new TwoBlueOneRed());
        typeOfAmmoTile.add(new TwoBlueOneYellow());
        typeOfAmmoTile.add(new TwoRedOneBlue());
        typeOfAmmoTile.add(new TwoRedOnePowerUp());
        typeOfAmmoTile.add(new TwoRedOneYellow());
        typeOfAmmoTile.add(new TwoYellowOneBlue());
        typeOfAmmoTile.add(new TwoYellowOnePowerUp());
        typeOfAmmoTile.add(new TwoYellowOneRed());
        typeOfAmmoTile.add(new YellowBluePowerUp());
        typeOfAmmoTile.add(new YellowRedPowerUp());**/
        for (int i = 0; i <typeOfAmmoTile.size(); i++) {
           for(int j=0;j<numMaxAmmoTyleForType;j++){
               deck.add(typeOfAmmoTile.get(i));
           }
        }
    }
}

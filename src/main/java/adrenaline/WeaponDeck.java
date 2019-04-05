package adrenaline;

import adrenaline.weapons.*;

import java.util.*;

/**
 * 
 */
public class WeaponDeck extends Deck {

   private LinkedList<WeaponCard> deck;


    public WeaponDeck() {
        deck =  new LinkedList<WeaponCard>();

        deck.add(new Cyberblade());
        deck.add(new Electroscythe());
        deck.add(new Flamethrower());
        deck.add(new Furnace());
        deck.add(new GrenadeLaucher());
        deck.add(new Heatseeker());
        deck.add(new Hellion());
        deck.add(new LockRifle());
        deck.add(new MachineGun());
        deck.add(new PlasmaGun());
        deck.add(new PowerGlove());
        deck.add(new Railgun());
        deck.add(new RocketLaucher());
        deck.add(new Shockwave());
        deck.add(new Shotgun());
        deck.add(new Sledgehammer());
        deck.add(new Thor());
        deck.add(new TractorBeam());
        deck.add(new VortexCannon());
        deck.add(new Whisper());
        deck.add(new Zx_2());
    }


}
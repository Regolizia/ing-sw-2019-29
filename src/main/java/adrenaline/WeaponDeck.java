package adrenaline;

import adrenaline.weapons.*;

import java.util.*;

/**
 * 
 */
public class WeaponDeck{

   private LinkedList<WeaponCard> deck;
    private LinkedList<WeaponCard> usedWeaponCard;

    public WeaponDeck() {
        deck =  new LinkedList<>();
        usedWeaponCard=new LinkedList<>();
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

    public LinkedList<WeaponCard> getList(){
        return deck;
    }
    /**
     * Shuffles the deck.
     */
    public void shuffleCards() {
        Collections.shuffle(deck);
    }



    public WeaponCard pickUpWeapon(){
        if(this.deck.size()>0){
            setUsedAmmoTile(this.deck.getFirst());
            return this.deck.getFirst();
        }

        else {
            shuffleUsedWeaponCards();
            deck=usedWeaponCard;
            usedWeaponCard.clear();
            return deck.getFirst();}
    }

    public void setUsedAmmoTile(WeaponCard weaponCard){
        usedWeaponCard.add(weaponCard);
    }

    /**
     * Shuffles used cards.
     */

    public void shuffleUsedWeaponCards() {
        Collections.shuffle(usedWeaponCard);
    }


}
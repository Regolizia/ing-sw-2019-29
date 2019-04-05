package adrenaline.weapons;

import adrenaline.AmmoCube;
import adrenaline.WeaponCard;

/**
 * 
 */
public class Sledgehammer extends WeaponCard {

    /**
     * Default constructor
     */
    public Sledgehammer() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.ALT,false));
    }

}
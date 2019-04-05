package adrenaline.weapons;

import adrenaline.AmmoCube;
import adrenaline.WeaponCard;

/**
 * 
 */
public class Cyberblade extends WeaponCard {

    /**
     * Default constructor
     */
    public Cyberblade() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.OP2,false));

    }

}
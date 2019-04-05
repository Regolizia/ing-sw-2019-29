package adrenaline.weapons;

import adrenaline.AmmoCube;
import adrenaline.WeaponCard;

/**
 * 
 */
public class RocketLaucher extends WeaponCard {

    /**
     * Default constructor
     */
    public RocketLaucher() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.OP1,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.OP2,false));
    }

}
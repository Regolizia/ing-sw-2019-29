package adrenaline.weapons;

import adrenaline.AmmoCube;
import adrenaline.WeaponCard;

/**
 * 
 */
public class VortexCannon extends WeaponCard {

    /**
     * Default constructor
     */
    public VortexCannon() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.OP1,false));
    }

}
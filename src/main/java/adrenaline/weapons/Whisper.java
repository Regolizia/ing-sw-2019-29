package adrenaline.weapons;

import adrenaline.AmmoCube;
import adrenaline.WeaponCard;

/**
 * 
 */
public class Whisper extends WeaponCard {

    /**
     * Default constructor
     */
    public Whisper() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
    }

}
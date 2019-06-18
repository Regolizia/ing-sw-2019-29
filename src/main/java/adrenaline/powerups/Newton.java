package adrenaline.powerups;

import adrenaline.AmmoCube;
import adrenaline.PowerUpCard;

public class Newton extends PowerUpCard {

    /**
     * Default constructor
     */
    public Newton(AmmoCube.CubeColor color) {
        setPowerUpColor(color);
        setCanBeUsedOnBot(true);
    }

    @Override
    public String toString() {
        return "Newton, "+getPowerUpColor();
    }


}
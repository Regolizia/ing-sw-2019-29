package adrenaline.powerups;

import adrenaline.AmmoCube;
import adrenaline.PowerUpCard;

public class TagbackGrenade extends PowerUpCard {

    /**
     * Default constructor
     */
    public TagbackGrenade(AmmoCube.CubeColor color) {
        setPowerUpColor(color);
        setCanBeUsedOnBot(true);
    }

    @Override
    public String toString() {
        return "TagbackGrenade, "+getPowerUpColor();
    }
}
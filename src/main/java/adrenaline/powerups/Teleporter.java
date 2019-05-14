package adrenaline.powerups;

import adrenaline.AmmoCube;
import adrenaline.PowerUpCard;

public class Teleporter  extends PowerUpCard {

    /**
     * Default constructor
     */
    public Teleporter(AmmoCube.CubeColor color) {
        setPowerUpColor(color);
    }

    @Override
    public String toString() {
        return "Teleporter, "+getPowerUpColor();
    }
}
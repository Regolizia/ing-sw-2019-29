package adrenaline.powerups;

import adrenaline.AmmoCube;
import adrenaline.PowerUpCard;

public class Teleporter  extends PowerUpCard {


    public Teleporter(AmmoCube.CubeColor color) {
        setPowerUpColor(color);
        setCanBeUsedOnBot(false);
    }

    @Override
    public String toString() {
        return "Teleporter, "+getPowerUpColor();
    }
}
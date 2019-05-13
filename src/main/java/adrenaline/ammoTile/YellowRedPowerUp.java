package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.Coordinates;
import adrenaline.CoordinatesWithRoom;

public class YellowRedPowerUp extends AmmoTile {
    public YellowRedPowerUp(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.POWERUP);
    }
}

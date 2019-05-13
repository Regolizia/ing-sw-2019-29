package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class TwoBlueOnePowerUp extends AmmoTile {
    public TwoBlueOnePowerUp(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.POWERUP);
    }
}

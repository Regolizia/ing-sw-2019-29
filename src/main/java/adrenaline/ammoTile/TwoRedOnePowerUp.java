package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class TwoRedOnePowerUp extends AmmoTile {
    public TwoRedOnePowerUp(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.POWERUP);
    }
}

package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class RedBluePowerUp extends AmmoTile {
    public RedBluePowerUp(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.POWERUP);
    }
}

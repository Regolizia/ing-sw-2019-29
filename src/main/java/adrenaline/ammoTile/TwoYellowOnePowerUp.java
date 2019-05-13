package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class TwoYellowOnePowerUp extends AmmoTile {
    public TwoYellowOnePowerUp(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.POWERUP);
    }
}

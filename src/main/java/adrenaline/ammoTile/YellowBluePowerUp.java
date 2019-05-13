package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.Coordinates;
import adrenaline.CoordinatesWithRoom;

public class YellowBluePowerUp extends AmmoTile {
    public YellowBluePowerUp(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.POWERUP);
    }
}

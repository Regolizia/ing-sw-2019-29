package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class TwoBlueOneRed extends AmmoTile {
    public TwoBlueOneRed(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.RED);
    }
}

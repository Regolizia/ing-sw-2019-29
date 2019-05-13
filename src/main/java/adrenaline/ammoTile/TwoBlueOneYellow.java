package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class TwoBlueOneYellow extends AmmoTile {
    public TwoBlueOneYellow(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.YELLOW);
    }
}

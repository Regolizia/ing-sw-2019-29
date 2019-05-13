package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class TwoRedOneYellow extends AmmoTile {
    public TwoRedOneYellow(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.YELLOW);
    }
}

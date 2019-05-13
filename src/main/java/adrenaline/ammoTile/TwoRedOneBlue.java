package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class TwoRedOneBlue extends AmmoTile {
    public TwoRedOneBlue(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.BLUE);
    }
}

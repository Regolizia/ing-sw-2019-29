package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class TwoYellowOneRed extends AmmoTile {


    public TwoYellowOneRed(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.RED);
    }
}

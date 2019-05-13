package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.CoordinatesWithRoom;

public class TwoYellowOneBlue extends AmmoTile {


    public TwoYellowOneBlue(CoordinatesWithRoom p){
        super(p, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.BLUE);
    }
}

package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.Coordinates;

public class TwoYellowOneBlue extends AmmoTile {


    public TwoYellowOneBlue(Coordinates p){
        super(p, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.BLUE);
    }
}

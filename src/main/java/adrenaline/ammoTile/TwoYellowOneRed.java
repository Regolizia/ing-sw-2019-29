package adrenaline.ammoTile;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.Coordinates;

public class TwoYellowOneRed extends AmmoTile {


    public TwoYellowOneRed(Coordinates p){
        super(p, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.RED);
    }
}

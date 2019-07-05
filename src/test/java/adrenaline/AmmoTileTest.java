package adrenaline;

import adrenaline.AmmoCube;
import adrenaline.AmmoTile;
import adrenaline.Coordinates;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AmmoTileTest {

    @Test
    public void testConstructor() {
        /*testing ammoTile creation and setting coordinates*/
        Coordinates c1 = new Coordinates(1,2);
        AmmoTile a = new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.BLUE);
        AmmoTile b = new AmmoTile(AmmoCube.CubeColor.POWERUP, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.BLUE);
        Coordinates d = a.getCoordinates();
        a.setCoordinates(2,3);
        List<AmmoCube> f = a.getAmmoCubes();
    }


}

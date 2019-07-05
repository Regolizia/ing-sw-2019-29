package adrenaline;

import adrenaline.AmmoCube;
import org.junit.jupiter.api.Test;

public class AmmoCubeTest {


    @Test
    public void testConstructor() {
            /*testing Ammocube colors,effects,payment*/
        AmmoCube c = new AmmoCube(AmmoCube.CubeColor.BLUE);
        c.setEffect(AmmoCube.Effect.ALT);
        c.setPaid(true);
        c.setCubeColor(AmmoCube.CubeColor.RED);
        AmmoCube.CubeColor a = c.getCubeColor();
        AmmoCube.Effect b = c.getEffect();
        Boolean d = c.getPaid();




    }
}

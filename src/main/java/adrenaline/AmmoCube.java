package adrenaline;

import java.util.*;

/**
 * 
 */
public class AmmoCube {

    public static enum CubeColor {
        RED, YELLOW, BLUE;
    }
    public static enum Effect {
        BASE, OP1, OP2, ALT;
    }

    public CubeColor color;
    public Effect effect;
    public boolean paid;


    /**
     * Default constructor
     */
    public AmmoCube() {
    }
    public AmmoCube(CubeColor c, Effect e, boolean p) {
        this.color = c;
        this.effect = e;
        this.paid = p;
    }


}
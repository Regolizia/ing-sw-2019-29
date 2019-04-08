package adrenaline;

import java.util.*;

/**
 * 
 */
public class AmmoCube {

    public enum CubeColor {
        RED, YELLOW, BLUE;
    }
    public enum Effect {
        BASE, OP1, OP2, ALT;
    }

    private CubeColor color;
    private Effect effect;
    private boolean paid;


    /**
     * Default constructor
     */
    public AmmoCube() {
    }

    public AmmoCube(CubeColor c) {
        this.color = c;
    }

    public AmmoCube(CubeColor c, Effect e, boolean p) {
        this.color = c;
        this.effect = e;
        this.paid = p;
    }

    public CubeColor getCubeColor(){
        return color;
    }
    public void setCubeColor(CubeColor c){
        this.color = c;
    }

    public Effect getEffect(){
        return effect;
    }
    public void setEffect(Effect e){
        this.effect = e;
    }

    public boolean getPaid(){
        return paid;
    }
    public void setPaid(boolean b){
        this.paid = b;
    }



}
package adrenaline;

/**
 * Is the class that represents the AmmoCube.
 * It has:
 * <ul>
 *     <li> a color
 *     <li> an effect
 *     <li> the state of the AmmoCube: paid or not
 * </ul>
 *
 * @author Eleonora Toscano
 * @version 1.0
 */
public class AmmoCube {

    public enum CubeColor {
        RED, YELLOW, BLUE, POWERUP, FREE
    }
    public enum Effect {
        BASE, OP1, OP2, ALT,GRAB
    }

    private CubeColor color;
    private Effect effect;
    private boolean paid;

    /**
     * Constructor of AmmoCube with just a color.
     *
     * @param c the color to set
     */
    public AmmoCube(CubeColor c) {
        this.color = c;
    }

    /**
     * Constructor of AmmoCube with color, effect and state.
     *
     * @param c the color to set
     * @param e the effect to set
     * @param p the state to set
     */
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
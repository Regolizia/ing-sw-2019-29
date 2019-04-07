package adrenaline;

import static adrenaline.PowerUpCard.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * 
 */
public class AmmoTile extends Tile {

    private int x;
    private int y;
    private LinkedList<Object> ammoTile;

    /**
     * Default constructor
     */
    public AmmoTile() {
        ammoTile = new LinkedList<Object>();
    }
    
    // AMMO + AMMO + AMMO
    public AmmoTile(int x, int y, AmmoCube a, AmmoCube.CubeColor ac, AmmoCube b, AmmoCube.CubeColor bc, AmmoCube c, AmmoCube.CubeColor cc) {
        this.x = x;
        this.y = y;
        ammoTile.add(new AmmoCube(ac));
        ammoTile.add(new AmmoCube(bc));
        ammoTile.add(new AmmoCube(cc));
    }
    // POWERUP + AMMO + AMMO
    public AmmoTile(int x, int y, PowerUpCard p, AmmoCube.CubeColor pc, AmmoCube a, AmmoCube.CubeColor ac, AmmoCube b, AmmoCube.CubeColor bc) {
        this.x = x;
        this.y = y;
        ammoTile.add(new PowerUpCard(pc));
        ammoTile.add(new AmmoCube(ac));
        ammoTile.add(new AmmoCube(bc));
    }


    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x = x;
    }
    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y = y;
    }
    public LinkedList<Object> getAmmoTile(){
        return ammoTile;
    }
    /*public void setAmmoTile(come costruttore){
        this.y = y;
    }*/


    /**
     * 
     */
    public void isTaken() {
        // TODO implement here
    }

}
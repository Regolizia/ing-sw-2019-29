package adrenaline;

import static adrenaline.PowerUpCard.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * 
 */
public class AmmoTile extends Tile {

    private Position position;
    private LinkedList<Object> ammoTile;

    /**
     * Default constructor
     */
    public AmmoTile() {
        ammoTile = new LinkedList<Object>();
    }

    // AMMO + AMMO + AMMO
    public AmmoTile(Position p, AmmoCube.CubeColor ac, AmmoCube.CubeColor bc,  AmmoCube.CubeColor cc) {
        this.position = p;
        ammoTile.add(new AmmoCube(ac));
        ammoTile.add(new AmmoCube(bc));
        ammoTile.add(new AmmoCube(cc));
    }
    // POWERUP + AMMO + AMMO
    public void AmmoTileP(Position p, AmmoCube.CubeColor pc, AmmoCube.CubeColor ac, AmmoCube.CubeColor bc) {
        this.position = p;
        ammoTile.add(new PowerUpCard(pc));
        ammoTile.add(new AmmoCube(ac));
        ammoTile.add(new AmmoCube(bc));
    }


    public Position getPosition(){
        return position;
    }
    public void setPosition(int x, int y){
        this.position.setPosition(x,y);
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
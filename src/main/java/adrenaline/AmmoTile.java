package adrenaline;

import java.util.LinkedList;

/**
 * 
 */
public class AmmoTile{

    private Coordinates coordinates;
    private LinkedList<AmmoCube> ammoTile;

    /**
     * Default constructor

    public AmmoTile() {
        ammoTile = new LinkedList<>();
    }
     */

    // AMMO + AMMO + AMMO or POWERUP + AMMO + AMMO (ONE CUBECOLOR MEANS PICK POWERUP)
    public AmmoTile(Coordinates p, AmmoCube.CubeColor ac, AmmoCube.CubeColor bc, AmmoCube.CubeColor cc) {
        this.coordinates = p;
        ammoTile = new LinkedList<>();
        ammoTile.add(new AmmoCube(ac));
        ammoTile.add(new AmmoCube(bc));
        ammoTile.add(new AmmoCube(cc));
    }



    public Coordinates getCoordinates(){
        return coordinates;
    }
    public void setCoordinates(int x, int y){
        this.coordinates.setCoordinates(x,y);
    }

    public LinkedList<AmmoCube> getAmmoTile(){
        return ammoTile;
    }
    /*public void setAmmoTile(come costruttore){
        this.y = y;
    }*/

}
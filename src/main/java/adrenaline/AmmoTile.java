package adrenaline;

import java.util.LinkedList;

/**
 * 
 */
public class AmmoTile extends Tile {

    private Coordinates coordinates;
    private LinkedList<Object> ammoTile;

    /**
     * Default constructor
     */
    public AmmoTile() {
        ammoTile = new LinkedList<>();
    }

    // AMMO + AMMO + AMMO
    public AmmoTile(Coordinates p, AmmoCube.CubeColor ac, AmmoCube.CubeColor bc, AmmoCube.CubeColor cc) {
        this.coordinates = p;
        ammoTile.add(new AmmoCube(ac));
        ammoTile.add(new AmmoCube(bc));
        ammoTile.add(new AmmoCube(cc));
    }
    // POWERUP + AMMO + AMMO
    public void ammoTileP(Coordinates p, AmmoCube.CubeColor pc, AmmoCube.CubeColor ac, AmmoCube.CubeColor bc) {
        this.coordinates = p;
        ammoTile.add(new PowerUpCard(pc));
        ammoTile.add(new AmmoCube(ac));
        ammoTile.add(new AmmoCube(bc));
    }


    public Coordinates getCoordinates(){
        return coordinates;
    }
    public void setPosition(int x, int y){
        this.coordinates.setPosition(x,y);
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
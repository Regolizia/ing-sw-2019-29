package adrenaline;

import java.util.LinkedList;
import java.util.List;

/**
 * Is the class that represents the tile of ammo.
 * The Tile can have 3 AmmoCubes or 2 AmmoCubes and
 * one indication to pick a PowerUp
 * It has:
 * <ul>
 *     <li> a position
 *     <li> a list of AmmoCubes
 * </ul>
 *
 * @author Eleonora Toscano
 * @author Giulia Valcamonica
 * @version 1.0
 */
public class AmmoTile{

    private Coordinates coordinates;
    private LinkedList<AmmoCube> ammoCubes;

    /**
     * Constructor of AmmoTile.
     * AMMO + AMMO + AMMO or AMMO + AMMO + POWERUP (ONE CUBECOLOR MEANS PICK POWERUP)
     *
     * @param ac the first AmmoCube
     * @param bc the second AmmoCube
     * @param cc the third AmmoCube
     */
public AmmoTile(AmmoCube.CubeColor ac, AmmoCube.CubeColor bc, AmmoCube.CubeColor cc) {
        coordinates=new Coordinates();
        ammoCubes = new LinkedList<>();
        ammoCubes.add(new AmmoCube(ac));
        ammoCubes.add(new AmmoCube(bc));
        ammoCubes.add(new AmmoCube(cc));
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }
    public void setCoordinates(int x, int y){
        this.coordinates.setCoordinates(x,y);
    }
    public List<AmmoCube> getAmmoCubes(){
        return ammoCubes;
    }

    @Override
    public String toString() {
        return this.ammoCubes.get(0).getCubeColor().toString()+ ", " + this.ammoCubes.get(1).getCubeColor().toString() + ", " + this.ammoCubes.get(2).getCubeColor().toString();
    }
}
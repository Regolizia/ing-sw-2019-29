package adrenaline;

import java.util.*;

/**
 * Is the class that represents the Rooms of the Board.
 * It contains:
 * <ul>
 *     <li> The width and the length as Coordinates
 *     <li> The list of AmmoTiles inside the Room
 *     <li> A token representing the Room
 * </ul>
 *
 * @author Eleonora Toscano
 * @version 1.0
 * @see Coordinates
 * @see AmmoTile
 */
public class Room {

    private Coordinates size;
    private LinkedList<AmmoTile> tiles;
    private int token;
    public LinkedList<Spawnpoint> spawnpoints;
    public LinkedList<SpawnpointDom> spawnpointsDom;

    /**
     * Default constructor
     */
    public Room() {
        this.size = new Coordinates();
    }

    /**
     * Constructor of a Room of size x y
     *
     * @param x the width to set
     * @param y the length to set
     */
    public Room(int x, int y) {

        spawnpoints = new LinkedList<Spawnpoint>();
        spawnpointsDom = new LinkedList<SpawnpointDom>();
        this.tiles = new LinkedList<AmmoTile>();
        this.size = new Coordinates(x,y);
        setToken(99);
    }


    //TODO AGGIUNGERE SPAWNPOINT
    public void addSpawnpoint(Spawnpoint s){

    }
    //TODO AGGIUNGERE SPAWNPOINT
    public void addSpawnpointDom(Spawnpoint s){

    }


    /**
     * Gets the list of Spawnpoints inside the Room.
     * The list could be empty. Usually a Room has at most
     * one Spawnpoint.
     *
     * @return the list of Spawnpoints
     * @see Spawnpoint
     */
    public List<Spawnpoint> getSpawnpoints(){
        return this.spawnpoints;
    }

    public List<SpawnpointDom> getSpawnpointDom(){
        return this.spawnpointsDom;
    }

    /**
     * Adds the given AmmoTile to the Room's list.
     *
     * @param t the tile to add
     * @see AmmoTile
     */
    public void addAmmoTile(AmmoTile t){
        tiles.add(t);
    }


    /**
     * Gets the AmmoTile at the given coordinates.
     *
     * @param coordinates a position inside the Room
     * @return the AmmoTile
     * @see Coordinates
     */
    public AmmoTile getAmmoTile(Coordinates coordinates){
        int index=0;
     /*   for(index=0;index<getTiles().size();index++)
        {
            //check the list until you find the AmmoTile
            if(getTiles().get(index).getCoordinates()==coordinates)
                break;
        }*/
        for (AmmoTile tile:getTiles()) {
            if (tile.getCoordinates().equals(coordinates))
                return tile;
        }
        //return getTiles().get(index);
        return null;
    }


    /**
     * Sets the room Token to the parameter i.
     *
     * @param i, an <code>int</code>
     */
    public void setToken(int i){
        this.token = i;
    }

    /**
     * Gets the width of the Room.
     *
     * @return the x-coordinate
     */
    public int getRoomSizeX(){
        return this.size.getX();
    }

    /**
     * Gets the length of the Room.
     *
     * @return the y-coordinate
     */
    public int getRoomSizeY(){
        return this.size.getY();
    }

    /**
     * Gets the token that represents the Room in the Board.
     *
     * @return the token of the Room
     */
    public int getToken(){
        return this.token;
    }

    /**
     * Gets the list of AmmoTiles that are in the Room.
     *
     * @return the list of AmmoTiles
     * @see AmmoTile
     */
    public LinkedList<AmmoTile> getTiles(){
        return tiles;
    }

    public Spawnpoint getSpawnpoint(int x, int y){
        for (Spawnpoint s: getSpawnpoints()) {
            if(s.getSpawnpointX()==x&& s.getSpawnpointY()== y)
                return s;
        }
        return null;
    }



}
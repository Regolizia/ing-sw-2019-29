package adrenaline;

import java.util.*;

/**
 * 
 */
public class Room {

    private Coordinates size;
    private LinkedList<AmmoTile> tiles;
    private int token;

    /**
     * Default constructor
     */
    public Room() {
        this.size = new Coordinates();
    }

    public Room(int x, int y) {

        this.tiles = new LinkedList<AmmoTile>();
        this.size = new Coordinates(x,y);
        setToken(99);
    }

    public void addSpawnpoint(Spawnpoint s){
    }



    public LinkedList<Spawnpoint> getSpawnpoints(){
        LinkedList<Spawnpoint> n = new LinkedList<>();
        return n;
    }

    public void addAmmoTile(AmmoTile t){
        tiles.add(t);
    }


    public AmmoTile getAmmoTile(Coordinates coordinates){
        int index=0;
        for(index=0;index<getTiles().size();index++)
        {
            //check the list until you find the AmmoTile
            if(getTiles().get(index).getCoordinates()==coordinates)
                break;
        }
        return getTiles().get(index);
    }



    public void setToken(int i){
        this.token = i;
    }

    public int getRoomSizeX(){
        return this.size.getX();
    }
    public int getRoomSizeY(){
        return this.size.getY();
    }
    public int getToken(){
        return this.token;
    }

    public LinkedList<AmmoTile> getTiles(){
        return tiles;
    }

}
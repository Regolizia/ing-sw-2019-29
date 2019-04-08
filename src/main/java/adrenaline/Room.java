package adrenaline;

import java.util.*;

/**
 * 
 */
public class Room {

    private Coordinates size;
    public LinkedList<AmmoTile> tiles;
    public LinkedList<Player> players;
    private int token;

    /**
     * Default constructor
     */
    public Room() {
        size.setX(0);
        size.setY(0);
    }

    public Room(int x, int y) {
        players = new LinkedList<Player>();
        tiles = new LinkedList<AmmoTile>();
        size.setCoordinates(x,y);
    }

    public void addSpawnpoint(Spawnpoint s){
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public void addAmmoTile(AmmoTile t){
        tiles.add(t);
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
}
package adrenaline;

import java.util.*;

/**
 * 
 */
public class Room {

    private int [][] roomMatrix;
    public LinkedList<AmmoTile> tiles;
    public LinkedList<Player> players;

    /**
     * Default constructor
     */
    public Room() {
    }

    public Room(int x, int y) {
        players = new LinkedList<Player>();
        tiles = new LinkedList<AmmoTile>();
        roomMatrix = new int[x][y];
    }

    public void addSpawnpoint(Spawnpoint s){
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public void addAmmoTile(AmmoTile t){
        tiles.add(t);
    }

    public int[][] getRoomMatrix(){
        return this.roomMatrix;
    }
}
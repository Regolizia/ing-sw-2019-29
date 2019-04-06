package adrenaline;

import java.util.*;

/**
 * 
 */
public class Room {

    public int [][] room;
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
        room = new int[x][y];
    }


    public void addPlayer(Player p){
        players.add(p);
    }

    public void addAmmoTile(AmmoTile t){
        tiles.add(t);
    }


}
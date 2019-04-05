package adrenaline;

import java.util.*;

/**
 *
 */
public class RoomDom {

    public int [][] room;
    public LinkedList<AmmoTile> tiles;
    public LinkedList<Player> players;
    public LinkedList<SpawnpointDom> spawnpoints;

    /**
     * Default constructor
     */
    public RoomDom(int x, int y) {

        spawnpoints = new LinkedList<SpawnpointDom>();
        players = new LinkedList<Player>();
        tiles = new LinkedList<AmmoTile>();
        room = new int[x][y];
    }

/*  NON SO SE SERVE, STESSO CODICE ANCHE IN ROOM

    public void addPlayer(Player p){
        players.add(p);
    }
    public void addSpawnpoint(Spawnpoint s){
        spawnpoints.add(s);
    }
    public void addAmmoTile(AmmoTile t){
        tiles.add(t);
    }

*/



}
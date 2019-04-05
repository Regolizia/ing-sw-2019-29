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





}
package adrenaline;

import java.util.*;

public class RoomDeath extends Room {


    public LinkedList<Spawnpoint> spawnpoints;


    /**
     * Default constructor
     */
    public RoomDeath(int x, int y) {
        super(x, y);
        spawnpoints = new LinkedList<Spawnpoint>();

    }


    public void addSpawnpoint(Spawnpoint s) {
        spawnpoints.add(s);
    }
}

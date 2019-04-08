package adrenaline;

import java.util.*;

public class RoomDeath extends Room {


    private LinkedList<Spawnpoint> spawnpoints;


    /**
     * Default constructor
     */
    public RoomDeath(int x, int y) {
        super(x, y);
        spawnpoints = new LinkedList<Spawnpoint>();

    }

    public LinkedList<Spawnpoint> getSpawnpoints(){
        return spawnpoints;
    }
    public void addSpawnpoint(Spawnpoint s) {
        spawnpoints.add(s);
    }
}

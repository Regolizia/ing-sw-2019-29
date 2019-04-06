package adrenaline;

import java.util.*;

/**
 *
 */
public class RoomDom extends Room{

    public LinkedList<SpawnpointDom> spawnpoints;

    /**
     * Default constructor
     */
    public RoomDom(int x, int y) {
        super(x, y);
        spawnpoints = new LinkedList<SpawnpointDom>();

    }


    public void addSpawnpoint(SpawnpointDom s){
        spawnpoints.add(s);
    }




}
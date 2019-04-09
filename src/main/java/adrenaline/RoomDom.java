package adrenaline;

import java.util.*;

/**
 *
 */
public class RoomDom extends Room{

    private LinkedList<SpawnpointDom> spawnpoints;

    /**
     * Default constructor
     */
    public RoomDom(int x, int y) {
        super(x, y);
        spawnpoints = new LinkedList<SpawnpointDom>();

    }
/*
    public LinkedList<SpawnpointDom> getSpawnpoints(){
        return spawnpoints;
    }*/
    public void addSpawnpoint(SpawnpointDom s){
        spawnpoints.add(s);
    }




}
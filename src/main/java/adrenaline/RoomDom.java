package adrenaline;

import java.util.*;

/**
 * Extends Room adding a list of SpawnpointsDom.
 *
 * @author Eleonora Toscano
 * @version 1.0
 */
public class RoomDom extends Room{

    private LinkedList<SpawnpointDom> spawnpoints;

    /**
     * Constructor of a Room that has also a list Of SpawnpointsDom.
     *
     * @param x the width
     * @param y the length
     * @see SpawnpointDom
     */
    public RoomDom(int x, int y) {
        super(x, y);
        spawnpoints = new LinkedList<SpawnpointDom>();

    }

    /**
     * Adds a SpawnpointDom to the Room's list.
     *
     * @param s the SpawnpointDom to add
     * @see SpawnpointDom
     */
    public void addSpawnpoint(Spawnpoint s){
        spawnpoints.add((SpawnpointDom) s);
    }




}
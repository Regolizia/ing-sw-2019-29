package adrenaline;

import java.util.*;

/**
 * Extends Room adding a list of Spawnpoints.
 *
 * @author Eleonora Toscano
 * @version 1.0
 */
public class RoomDeath extends Room {


    private LinkedList<Spawnpoint> spawnpoints;

    /**
     * Constructor of a Room that has also a list Of Spawnpoints.
     *
     * @param x the width
     * @param y the length
     * @see Spawnpoint
     */
    public RoomDeath(int x, int y) {
        super(x, y);
        spawnpoints = new LinkedList<Spawnpoint>();

    }

    /**
     * Adds a Spawnpoint to the Room's list.
     *
     * @param s the Spawnpoint to add
     * @see Spawnpoint
     */
    public void addSpawnpoint(Spawnpoint s) {

        spawnpoints.add(s);
    }
}

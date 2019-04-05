package adrenaline;

import java.util.*;

/**
 * 
 */
public class DeathmatchBoard extends GameBoard {



    private LinkedList<Room> rooms;
    /**
     * Default constructor
     */
    public DeathmatchBoard() {
        rooms = new LinkedList<Room>();
        doors = new LinkedList<Door>();
    }

}
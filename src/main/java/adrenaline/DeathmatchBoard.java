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
        super();
        rooms = new LinkedList<Room>();
    }


    public void addRoom(Room r){
        rooms.add(r);
    }

    public Room getRoom(int i){
       return rooms.get(i);
    }

}
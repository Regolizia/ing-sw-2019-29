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


    public void addRoom(Room r){
        rooms.add(r);
    }
    public void addDoor(Door d){
        doors.add(d);
    }

    public Room getRoom(int i){
       return rooms.get(i);
    }

}
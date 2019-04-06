package adrenaline;

import java.util.*;

/**
 * 
 */
public class DeathmatchBoard extends GameBoard {



    private LinkedList<RoomDeath> rooms;
    /**
     * Default constructor
     */
    public DeathmatchBoard() {
        super();
        rooms = new LinkedList<RoomDeath>();
    }


    public void addRoom(RoomDeath r){
        rooms.add(r);
    }

    public RoomDeath getRoom(int i){
       return rooms.get(i);
    }

}
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
    public DeathmatchBoard(//int numSkull
                           ) {
        super (//numSkull
        );
        rooms = new LinkedList<RoomDeath>();

    }


    public void addRoom(RoomDeath r){
        rooms.add(r);
        // The room's token is the index of the array
        r.setToken(rooms.indexOf(r));
    }


    public RoomDeath getRoom(int i){
       return rooms.get(i);
    }

}
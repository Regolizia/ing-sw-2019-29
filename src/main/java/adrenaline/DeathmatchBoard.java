package adrenaline;

import adrenaline.gameboard.GameBoard;

import java.util.*;

/**
 *
 *
 * @author Eleonora Toscano
 * @version 1.0
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
        rooms = new LinkedList<>();

    }

    /**
     * Adds a RoomDeath to the list and sets token to the list's index.
     *
     * @param r the RoomDeath to add
     * @see RoomDeath
     */
    @Override
    public void addRoom(RoomDeath r){
        rooms.add(r);
        // The room's token is the index of the array
        r.setToken(rooms.indexOf(r));
    }
    @Override
    public RoomDeath getRoom(int i){
       return rooms.get(i);
    }

    public List<RoomDeath> getRooms(){
        return rooms;
    }

}
package adrenaline;

import java.util.LinkedList;

/**
 * 
 */
public class DominationBoard extends GameBoard {


    private LinkedList<RoomDom> rooms;
    /**
     * Default constructor
     */
    public DominationBoard(int numSkull) {
        super(numSkull);
        rooms = new LinkedList<RoomDom>();

    }
    public void addRoom(RoomDom r){
        rooms.add(r);
        // The room's token is the index of the array
        r.setToken(rooms.indexOf(r));
    }

    public RoomDom getRoom(int i) {
        return rooms.get(i);
    }



}
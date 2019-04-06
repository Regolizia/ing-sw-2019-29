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
    public DominationBoard() {
        super();
        rooms = new LinkedList<RoomDom>();

    }
    public void addRoom(RoomDom r){
        rooms.add(r);
    }

    public RoomDom getRoomDom(int i){
        return rooms.get(i);
    }

    /**
     * 
     */
    //public void skulls;

    /**
     * 
     */
   // public void spawnpoints_tracks;

    /**
     * 
     */
    public void getDom() {
        // TODO implement here
    }

}
package adrenaline;

import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;

/**
 *
 *
 * @author Eleonora Toscano
 * @version 1.0
 */
public class DominationBoard extends GameBoard {

    private LinkedList<RoomDom> rooms;
    /**
     * Default constructor
     */
    public DominationBoard(//int numSkull
    ) {
        super(//numSkull
        );
        rooms = new LinkedList<>();

    }
    /**
     * Adds a RoomDom to the list and sets token to the list's index.
     *
     * @param r the RoomDom to add
     * @see RoomDom
     */
    @Override
    public void addRoom(RoomDom r){
        rooms.add(r);
        // The room's token is the index of the array
        r.setToken(rooms.indexOf(r));
    }
    @Override
    public RoomDom getRoom(int i) {
        return rooms.get(i);
    }

    @Override
    public int getNumberOfRooms(){return this.rooms.size();}

}
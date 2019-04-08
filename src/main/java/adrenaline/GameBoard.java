package adrenaline;

import java.util.*;

/**
 * 
 */
public class GameBoard {

    protected LinkedList<Door> doors;



    /**
     * Default constructor
     */
    public GameBoard() {
        doors = new LinkedList<Door>();
    }


// VORREI NON FARGLI FARE NULLA PERCHé I METODI SONO NEI RISPETTIVI BOARD MA VUOLE CHE RITORNI QUALCOSA
    public Room getRoom(int i) {
        return this.getRoom(i);
    }

    public void addRoom(Room r){
    }
    public void addRoom(RoomDeath r){
    }
    public void addRoom(RoomDom r){
    }

    public void addDoor(Door d){
        doors.add(d);
    }




    public LinkedList<CoordinatesWithRoom> oneTileDistant(CoordinatesWithRoom c) {

        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();

        // COORDINATES OF MY CELL
        int x = c.getX();
        int y = c.getY();
        // CHECKS IF NWSE COORDINATES ARE INSIDE THE ROOM
        if(x+1<=c.getRoom().getRoomSizeX())
            list.add(new CoordinatesWithRoom(x+1,y,c.getRoom()));

        if(y+1<=c.getRoom().getRoomSizeY())
            list.add(new CoordinatesWithRoom(x,y+1,c.getRoom()));

        if(x-1>=0)
            list.add(new CoordinatesWithRoom(x-1,y,c.getRoom()));

        if(y-1>=0)
            list.add(new CoordinatesWithRoom(x,y-1,c.getRoom()));

        // CHECKS IF CELL HAS A DOOR
        // SAME ROOM, SAME COORDINATES AS A ROOM IN THE DOOR CLASS, FOR BOTH SIDES
        for(int i=0;i<=doors.size();i++) {
            if ((c.getRoom().getToken() == doors.get(i).getRoom1().getToken() && c.getX() == doors.get(i).getCoordinates1().getX() && c.getY() == doors.get(i).getCoordinates1().getY()))
                list.add(new CoordinatesWithRoom(doors.get(i).getCoordinates2().getX(), doors.get(i).getCoordinates2().getY(), doors.get(i).getRoom2()));


            if ((c.getRoom().getToken() == doors.get(i).getRoom2().getToken() && c.getX() == doors.get(i).getCoordinates2().getX() && c.getY() == doors.get(i).getCoordinates2().getY()))
                list.add(new CoordinatesWithRoom(doors.get(i).getCoordinates1().getX(), doors.get(i).getCoordinates1().getY(), doors.get(i).getRoom1()));
        }

        return list;

    }



    /**
     * 
     */
    public void twoTilesDistant() {
        // TODO implement here
    }

    /**
     * 
     */
    public void threeTilesDistant() {
        // TODO implement here
    }

}
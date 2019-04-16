package adrenaline;

import java.util.*;

import static adrenaline.Door.Direction.*;

/**
 * 
 */
public class GameBoard {

    protected LinkedList<Door> doors;
    protected LinkedList<Wall> walls;



    /**
     * Default constructor
     */
    public GameBoard() {
        doors = new LinkedList<Door>();
        walls = new LinkedList<Wall>();
    }


// VORREI NON FARGLI FARE NULLA PERCHé I METODI SONO NEI RISPETTIVI BOARD MA VUOLE CHE RITORNI QUALCOSA
    public Room getRoom(int i) {
        return new Room();
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
    public LinkedList<Door> getDoors(){
        return doors;
    }

    public void addWall(Wall w){
        walls.add(w);
    }
    public LinkedList<Wall> getWall(){
        return walls;
    }

    // MOVED DISTANCE METHODS IN COORDINATES WITH ROOM

    // NS -> 1
    // SN -> 2
    // WE -> 3
    // EW -> 4
    // C AND C1 NEED TO BE NEAR EACH OTHER
    // FROM C TO C1
    public Door.Direction getDirection(CoordinatesWithRoom c, CoordinatesWithRoom c1) {
        if (c.getRoom().getToken() == c1.getRoom().getToken()) {   // SAME ROOM
            if (c1.getX() == c.getX() + 1) return WE;
            if (c1.getX() == c.getX() - 1) return EW;
            if (c1.getY() == c.getY() + 1) return NS;
            if (c1.getY() == c.getY() - 1) return SN;
        } else {
            for (int i = 0; i < getDoors().size(); i++) {
                if ((c1.getRoom().getToken() == getDoors().get(i).getCoordinates1().getRoom().getToken()
                        && c1.getX() == getDoors().get(i).getCoordinates1().getX()
                        && c1.getY() == getDoors().get(i).getCoordinates1().getY())
                        && c.getRoom().getToken() == getDoors().get(i).getCoordinates2().getRoom().getToken()
                        && c.getX() == getDoors().get(i).getCoordinates2().getX()
                        && c.getY() == getDoors().get(i).getCoordinates2().getY()) {
                    if (getDoors().get(i).getDir() == NS) return SN;
                    if (getDoors().get(i).getDir() == SN) return NS;
                    if (getDoors().get(i).getDir() == WE) return EW;
                    if (getDoors().get(i).getDir() == EW) return WE;
                }


                if (c1.getRoom().getToken() == getDoors().get(i).getCoordinates2().getRoom().getToken()
                        && c1.getX() == getDoors().get(i).getCoordinates2().getX()
                        && c1.getY() == getDoors().get(i).getCoordinates2().getY()
                        && c.getRoom().getToken() == getDoors().get(i).getCoordinates1().getRoom().getToken()
                        && c.getX() == getDoors().get(i).getCoordinates1().getX()
                        && c.getY() == getDoors().get(i).getCoordinates1().getY()) {

                    return getDoors().get(i).getDir();
                }
            }
        }
        return NS;
    }

 /*   // GIVEN DIRECTION OF MOVEMENT, SEARCHES A CELL AFTER THAT
    public void addNextCellToListGivenDirection(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom Cell, Door.Direction dir){
        if(dir==SN){

        }
    }
    */

}
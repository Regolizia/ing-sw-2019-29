package adrenaline.gameboard;

import adrenaline.CoordinatesWithRoom;
import adrenaline.Room;
import adrenaline.RoomDeath;
import adrenaline.RoomDom;

import java.util.*;
import static adrenaline.gameboard.Door.Direction.*;

/**
 * Is the class that represents the Board.
 * It contains:
 * <ul>
 *     <li> A list of Doors
 *     <li> A list of Walls
 * </ul>
 *
 * @author Eleonora Toscano
 * @version 1.0
 */
public class GameBoard {

    private LinkedList<Door> doors;
    private LinkedList<Wall> walls;
    int numSkull;

    /**
     * Default constructor
     */
    public GameBoard(//int numSkull
    ) {
        doors = new LinkedList<>();
        walls = new LinkedList<>();
        //this.numSkull=numSkull;
    }

    /**
     * Overridden.
     * @return r A Room
     */
    public Room getRoom(int i) {
        return new Room();
    }
    /**
     * Overridden.
     */
    public void addRoom(Room r){    }
    /**
     * Overridden.
     */
    public void addRoom(RoomDeath r){    }
    /**
     * Overridden.
     */
    public void addRoom(RoomDom r){    }
    /**
     * Adds a Door to the list.
     */
    public void addDoor(Door d){
        doors.add(d);
    }
    /**
     * Gets the Doors list
     */
    public LinkedList<Door> getDoors(){
        return doors;
    }
    /**
     * Adds a Wall to the list.
     */
    public void addWall(Wall w){
        walls.add(w);
    }
    /**
     * Gets the Walls list
     */
    public LinkedList<Wall> getWalls(){
        return walls;
    }

    /**
     * Gets the Direction between two adjoining cells.
     * C and C1 need to be adjacent.
     * The Direction is from C to C1.
     * The cells can be separated by a Wall or a Door.
     *
     * @param c the starting cell
     * @param c1 the final cell
     * @return a Direction
     * @see Door.Direction
     */
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

            //////////////// WALLS
            for (int i = 0; i < getWalls().size(); i++) {
                if ((c1.getRoom().getToken() == getWalls().get(i).getCoordinates1().getRoom().getToken()
                        && c1.getX() == getWalls().get(i).getCoordinates1().getX()
                        && c1.getY() == getWalls().get(i).getCoordinates1().getY())
                        && c.getRoom().getToken() == getWalls().get(i).getCoordinates2().getRoom().getToken()
                        && c.getX() == getWalls().get(i).getCoordinates2().getX()
                        && c.getY() == getWalls().get(i).getCoordinates2().getY()) {
                    if (getWalls().get(i).getDir() == NS) return SN;
                    if (getWalls().get(i).getDir() == SN) return NS;
                    if (getWalls().get(i).getDir() == WE) return EW;
                    if (getWalls().get(i).getDir() == EW) return WE;
                }


                if (c1.getRoom().getToken() == getWalls().get(i).getCoordinates2().getRoom().getToken()
                        && c1.getX() == getWalls().get(i).getCoordinates2().getX()
                        && c1.getY() == getWalls().get(i).getCoordinates2().getY()
                        && c.getRoom().getToken() == getWalls().get(i).getCoordinates1().getRoom().getToken()
                        && c.getX() == getWalls().get(i).getCoordinates1().getX()
                        && c.getY() == getWalls().get(i).getCoordinates1().getY()) {

                    return getWalls().get(i).getDir();
                }
            }
        }
        return NS;
    }

    public int getNumSkull(){
     return this.numSkull;
 }
    public void pickASkull(){
     this.numSkull--;
}
    public int getNumberOfRooms(){return 0;}
}
package adrenaline.gameboard;

import adrenaline.CoordinatesWithRoom;
import adrenaline.Room;


/**
 * Is the class that represents doors
 *
 */

public class Door{



    private CoordinatesWithRoom c1;
    private CoordinatesWithRoom c2;
    private Direction dir;

    public enum Direction{
        NS, SN, WE, EW
    }


    public Door(Room r1, int x1, int y1, Room r2, int x2, int y2, Direction d) {
        this.c1 = new CoordinatesWithRoom(x1,y1,r1);
        this.c2 = new CoordinatesWithRoom(x2,y2,r2);
        this.dir = d;

    }


    /**
     * getCoordinates1()
     *
     * @return door's coordinates by first room
     */
    public CoordinatesWithRoom getCoordinates1(){
        return c1;
    }
    /**
     * getCoordinates1()
     *
     * @return door's coordinates by second room
     */
    public CoordinatesWithRoom getCoordinates2(){
        return c2;
    }
    /**
     * getDir
     *
     * @return door's direction
     */
    public Direction getDir(){ return this.dir; }










}
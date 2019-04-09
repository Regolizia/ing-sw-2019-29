package adrenaline;

import java.util.*;

public class Door {

    private Room room1;
    private Room room2;
    private Coordinates c1;
    private Coordinates c2;

    /**
     * Default constructor
     *
     *   public Door() {
     *     }
     */


    public Door(Room r1, int x1, int y1, Room r2, int x2, int y2) {
        this.room1 = r1;
        this.room2 = r2;
        this.c1 = new Coordinates(x1,y1);
        this.c2 = new Coordinates(x2,y2);
    }

    public Room getRoom1(){
        return this.room1;
    }
    public Room getRoom2(){
        return this.room2;
    }
    public Coordinates getCoordinates1(){
        return c1;
    }
    public Coordinates getCoordinates2(){
        return c2;
    }
}
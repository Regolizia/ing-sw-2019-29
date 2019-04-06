package adrenaline;

import java.util.*;

public class Door {

    private Room room1;
    private Room room2;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    /**
     * Default constructor
     */
    public Door() {
    }

    public Door(Room r1, int x1, int y1, Room r2, int x2, int y2) {
        this.room1 = r1;
        this.room2 = r2;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }



}
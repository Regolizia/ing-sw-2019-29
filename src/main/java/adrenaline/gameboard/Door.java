package adrenaline.gameboard;

import adrenaline.CoordinatesWithRoom;
import adrenaline.Room;

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

    public CoordinatesWithRoom getCoordinates1(){
        return c1;
    }
    public CoordinatesWithRoom getCoordinates2(){
        return c2;
    }
    public Direction getDir(){ return this.dir; }










}
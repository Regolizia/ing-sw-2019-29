package adrenaline;

public class CoordinatesWithRoom extends Coordinates {
    private Room room;


    public CoordinatesWithRoom(){};
    public CoordinatesWithRoom(int x, int y, Room r){
        this.room = r;
        setX(x);
        setY(y);
    };

    public Room getRoom(){
        return this.room;
    }
    public void setRoom(Room r){
        this.room = r;
    }


}

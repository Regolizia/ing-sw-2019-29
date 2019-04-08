package adrenaline;

public class PositionWithRoom extends Position{
    private Room room;


    public PositionWithRoom(){};

    public Room getRoom(){
        return this.room;
    }
    public void setRoom(Room r){
        this.room = r;
    }
}

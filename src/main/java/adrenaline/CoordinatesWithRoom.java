package adrenaline;

import java.util.LinkedList;

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







    public LinkedList<CoordinatesWithRoom> oneTileDistant(GameBoard g) {

        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();

        // COORDINATES OF MY CELL
        int x = getX();
        int y = getY();
        // CHECKS IF NWSE COORDINATES ARE INSIDE THE ROOM
        if(x+1<=getRoom().getRoomSizeX())
            list.add(new CoordinatesWithRoom(x+1,y,getRoom()));

        if(y+1<=getRoom().getRoomSizeY())
            list.add(new CoordinatesWithRoom(x,y+1,getRoom()));

        if(x-1>=0)
            list.add(new CoordinatesWithRoom(x-1,y,getRoom()));

        if(y-1>=0)
            list.add(new CoordinatesWithRoom(x,y-1,getRoom()));

        // CHECKS IF CELL HAS A DOOR
        // SAME ROOM, SAME COORDINATES AS A ROOM IN THE DOOR CLASS, FOR BOTH SIDES
        for(int i=0;i<=g.getDoors().size();i++) {
            if ((getRoom().getToken() == g.getDoors().get(i).getRoom1().getToken() && getX() == g.getDoors().get(i).getCoordinates1().getX() && getY() == g.getDoors().get(i).getCoordinates1().getY()))
                list.add(new CoordinatesWithRoom(g.getDoors().get(i).getCoordinates2().getX(), g.getDoors().get(i).getCoordinates2().getY(), g.getDoors().get(i).getRoom2()));


            if ((getRoom().getToken() == g.getDoors().get(i).getRoom2().getToken() && getX() == g.getDoors().get(i).getCoordinates2().getX() && getY() == g.getDoors().get(i).getCoordinates2().getY()))
                list.add(new CoordinatesWithRoom(g.getDoors().get(i).getCoordinates1().getX(), g.getDoors().get(i).getCoordinates1().getY(), g.getDoors().get(i).getRoom1()));
        }

        return list;

    }



    /**
     *
     */
    public LinkedList<CoordinatesWithRoom> twoTilesDistant(GameBoard g) {

        LinkedList<CoordinatesWithRoom> listTwo = new LinkedList<>();
        LinkedList<CoordinatesWithRoom> listOne = oneTileDistant(g);

        for(int i=0;i<listOne.size();i++){
            listTwo.addAll(listOne.get(i).oneTileDistant(g));
        }
        for(int i=listTwo.size()-1;i>=0;i--){
            if(listTwo.get(i).getRoom().getToken()==this.getRoom().getToken() && listTwo.get(i).getX()==this.getX() && listTwo.get(i).getY()==this.getY()){
                listTwo.remove(i);
            }
        }
        return listTwo;
    }

    /**
     *
     */
    public LinkedList<CoordinatesWithRoom> threeTilesDistant(GameBoard g) {

    LinkedList<CoordinatesWithRoom> listThree = new LinkedList<>();
    LinkedList<CoordinatesWithRoom> listTwo = twoTilesDistant(g);

    for(int i=0;i<listTwo.size();i++){
        listThree.addAll(listTwo.get(i).oneTileDistant(g));
    }

        return listThree;
    }


}

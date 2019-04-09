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

        if(x-1>0)
            list.add(new CoordinatesWithRoom(x-1,y,getRoom()));

        if(y-1>0)
            list.add(new CoordinatesWithRoom(x,y-1,getRoom()));

        // CHECKS IF CELL HAS A DOOR
        // SAME ROOM, SAME COORDINATES AS A ROOM IN THE DOOR CLASS, FOR BOTH SIDES
        for(int i=0;i<g.getDoors().size();i++) {
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
    for(int i=0;i<listThree.size();i++){
        for (int j=i+1;j<listThree.size();j++){
            if(listThree.get(i).getRoom()==listThree.get(j).getRoom()
            && listThree.get(i).getX()==listThree.get(j).getX()
            && listThree.get(i).getY()==listThree.get(j).getY()){
                listThree.remove(j);
                j--;
            }
        }
    }
        return listThree;
    }


    public LinkedList<CoordinatesWithRoom> fourTilesDistant(GameBoard g) {

        LinkedList<CoordinatesWithRoom> listFour = new LinkedList<>();
        LinkedList<CoordinatesWithRoom> listThree = threeTilesDistant(g);

        for (int i = 0; i < listThree.size(); i++) {
            listFour.addAll(listThree.get(i).oneTileDistant(g));
        }

        for (int i = 0; i < listFour.size(); i++) {
            if (listFour.get(i).getRoom().getToken() == this.getRoom().getToken() && listFour.get(i).getX() == this.getX() && listFour.get(i).getY() == this.getY()) {
                listFour.remove(i);
                i--;
            }

            for (int k = 0; k < listFour.size(); k++) {
                for (int j = k + 1; j < listFour.size(); j++) {
                    if (listFour.get(k).getRoom() == listFour.get(j).getRoom()
                            && listFour.get(k).getX() == listFour.get(j).getX()
                            && listFour.get(k).getY() == listFour.get(j).getY()) {
                        listFour.remove(j);
                        j--;
                    }
                }
            }
        }
        return listFour;
        }


   /*     /////
        public LinkedList<CoordinatesWithRoom> insideTheRoom (GameBoard g){

        }*/


}

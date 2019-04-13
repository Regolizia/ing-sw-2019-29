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




/////////////////////////////////////////////////////////////////////////////


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


///////////////////////////////////////////////

    public LinkedList<CoordinatesWithRoom> removeThisCell(LinkedList<CoordinatesWithRoom> list){
        for(int i=list.size()-1;i>=0;i--) {
            if (list.get(i).getRoom().getToken() == this.getRoom().getToken() && list.get(i).getX() == this.getX() && list.get(i).getY() == this.getY()) {
                list.remove(i);
            }
        }
        return list;
    }

    public LinkedList<CoordinatesWithRoom> removeDuplicates(LinkedList<CoordinatesWithRoom> list){
        for (int k = 0; k < list.size(); k++) {
            for (int j = k + 1; j < list.size(); j++) {
                if (list.get(k).getRoom() == list.get(j).getRoom()
                        && list.get(k).getX() == list.get(j).getX()
                        && list.get(k).getY() == list.get(j).getY()) {
                    list.remove(j);
                    j--;
                }
            }
        }
      return list;
    }

///////////////////////////////////////////////////////////////////////

        public LinkedList<CoordinatesWithRoom> XTilesDistant(GameBoard g, int distance){

        LinkedList<CoordinatesWithRoom> listTemp;
        LinkedList<CoordinatesWithRoom> list =new LinkedList<>();
        LinkedList<CoordinatesWithRoom> listTemp2;
        list.add(this);

        for(int i=1;i<=distance;i++){
            listTemp = new LinkedList<>();
            for(CoordinatesWithRoom element : list){
                listTemp2 = element.oneTileDistant(g);
                listTemp.addAll(listTemp2);
            }
            list.clear();
            list = listTemp;
        }
        list = this.removeDuplicates(list);
        list = this.removeThisCell(list);
        return list;
    }

    public boolean isCWRInTwoLists(LinkedList<CoordinatesWithRoom> listMoves, LinkedList<CoordinatesWithRoom> listOriginalMoves, WeaponCard w, EffectAndNumber en, GameBoard g) {
        for (int i = 0; i < listMoves.size(); i++) {
            for (int l = 0; l < w.getPossibleTargetCells(this, en, g).size(); l++) {
                if (listMoves.get(i).getX() == listOriginalMoves.get(l).getX() &&
                        listMoves.get(i).getY() == listOriginalMoves.get(l).getY() &&
                        listMoves.get(i).getRoom().getToken() == listOriginalMoves.get(l).getRoom().getToken()) {
                    return true;
                }
            }
        }
        return false;
    }

    public LinkedList<CoordinatesWithRoom> addCellDistant2ThroughDoor(LinkedList<CoordinatesWithRoom> list,CoordinatesWithRoom c, GameBoard g){
        int x = c.getX();
        int y = c.getY();

        // ADDS CELLS IF DISTANT 2, THROUGH DOOR (IN THE SAME DIRECTION AS DISTANT 1)
        for(int k=0;k<list.size();k++) {
            for (int i = 0; i < g.getDoors().size(); i++) {
                if ((list.get(k).getRoom().getToken() == g.getDoors().get(i).getRoom1().getToken()
                        && list.get(k).getX() == g.getDoors().get(i).getCoordinates1().getX()
                        && list.get(k).getY() == g.getDoors().get(i).getCoordinates1().getY()) ||
                        (list.get(k).getRoom().getToken() == g.getDoors().get(i).getRoom2().getToken()
                                && list.get(k).getX() == g.getDoors().get(i).getCoordinates2().getX()
                                && list.get(k).getY() == g.getDoors().get(i).getCoordinates2().getY())){

                    // PASSAGE BETWEEN ROOM1 TO ROOM2
                    // 1 -> NS
                    // 2 -> SN
                    // 3 -> WE
                    // 4 -> EW
                    if (list.get(k).getX() == x + 1 && g.getDoors().get(i).hasDirection() == 3 ||
                            list.get(k).getX() == x - 1 && g.getDoors().get(i).hasDirection() == 4 ||
                            list.get(k).getY() == y + 1 && g.getDoors().get(i).hasDirection() == 1 ||
                            list.get(k).getY() == y - 1 && g.getDoors().get(i).hasDirection() == 2) {
                        list.add(new CoordinatesWithRoom(g.getDoors().get(i).getCoordinates2().getX(),
                                g.getDoors().get(i).getCoordinates2().getY(), g.getDoors().get(i).getRoom2()));
                    }
                }
            }
        }
        return list;
    }


}



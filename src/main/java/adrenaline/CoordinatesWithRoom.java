package adrenaline;

import java.util.LinkedList;

import static adrenaline.Door.Direction.*;

public class CoordinatesWithRoom extends Coordinates {
    private Room room;


    public CoordinatesWithRoom() {
    }

    ;

    public CoordinatesWithRoom(int x, int y, Room r) {
        this.room = r;
        setX(x);
        setY(y);
    }

    ;

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room r) {
        this.room = r;
    }

    @Override
    public String toString() {
        return this.getX() + ", " + this.getY() + " Room: " + this.getRoom().getToken();
    }

/////////////////////////////////////////////////////////////////////////////


    public LinkedList<CoordinatesWithRoom> oneTileDistant(GameBoard g) {

        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();

        // COORDINATES OF MY CELL
        int x = getX();
        int y = getY();
        // CHECKS IF NWSE COORDINATES ARE INSIDE THE ROOM
        if (x + 1 <= getRoom().getRoomSizeX())
            list.add(new CoordinatesWithRoom(x + 1, y, getRoom()));

        if (y + 1 <= getRoom().getRoomSizeY())
            list.add(new CoordinatesWithRoom(x, y + 1, getRoom()));

        if (x - 1 > 0)
            list.add(new CoordinatesWithRoom(x - 1, y, getRoom()));

        if (y - 1 > 0)
            list.add(new CoordinatesWithRoom(x, y - 1, getRoom()));

        // CHECKS IF CELL HAS A DOOR
        // SAME ROOM, SAME COORDINATES AS A ROOM IN THE DOOR CLASS, FOR BOTH SIDES
        for (int i = 0; i < g.getDoors().size(); i++) {
            if ((getRoom().getToken() == g.getDoors().get(i).getCoordinates1().getRoom().getToken() && getX() == g.getDoors().get(i).getCoordinates1().getX() && getY() == g.getDoors().get(i).getCoordinates1().getY()))
                list.add(new CoordinatesWithRoom(g.getDoors().get(i).getCoordinates2().getX(), g.getDoors().get(i).getCoordinates2().getY(), g.getDoors().get(i).getCoordinates2().getRoom()));


            if ((getRoom().getToken() == g.getDoors().get(i).getCoordinates2().getRoom().getToken() && getX() == g.getDoors().get(i).getCoordinates2().getX() && getY() == g.getDoors().get(i).getCoordinates2().getY()))
                list.add(new CoordinatesWithRoom(g.getDoors().get(i).getCoordinates1().getX(), g.getDoors().get(i).getCoordinates1().getY(), g.getDoors().get(i).getCoordinates1().getRoom()));
        }

        return list;

    }


///////////////////////////////////////////////

    public LinkedList<CoordinatesWithRoom> removeThisCell(LinkedList<CoordinatesWithRoom> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getRoom().getToken() == this.getRoom().getToken() && list.get(i).getX() == this.getX() && list.get(i).getY() == this.getY()) {
                list.remove(i);
            }
        }
        return list;
    }

///////////////////////////////////////////////////////////////////////

    public LinkedList<CoordinatesWithRoom> removeDuplicates(LinkedList<CoordinatesWithRoom> list) {
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

    public LinkedList<CoordinatesWithRoom> XTilesDistant(GameBoard g, int distance) {

        LinkedList<CoordinatesWithRoom> listTemp;
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        LinkedList<CoordinatesWithRoom> listTemp2;
        list.add(this);

        for (int i = 1; i <= distance; i++) {
            listTemp = new LinkedList<>();
            for (CoordinatesWithRoom element : list) {
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

    ///////////////////////////////////////////////////////////////////////

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


///////////////////////////////////////////////////////////////////////

    // SAME DIRECTION,  ------NOT----- THROUGH WALLS

    public LinkedList<CoordinatesWithRoom> tilesSameDirection(int moves, GameBoard g) {
        CoordinatesWithRoom c0;

        LinkedList<CoordinatesWithRoom> listOne = this.oneTileDistant(g); // CELL TO CHECK FOR NEXT
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(listOne);
        list.add(this);

        for (CoordinatesWithRoom c1 : listOne) {
            c0 = this;
            for(int j=1;j<moves;j++){
                CoordinatesWithRoom c2 = getNextCell(c0,c1,g);

                if(c2.getX()==0 || c2.getY()==0){
                    break;
                }
                list.add(c2);
                c0 = c1;
                c1 = c2;
            }
        }

        return list;
    }

/////////////////////////////////////////////////////////////

    // C PREVIOUS CELL, C1 THIS CELL, C2 NEXT CELL
    public CoordinatesWithRoom getNextCell(CoordinatesWithRoom c, CoordinatesWithRoom c1, GameBoard g){

                        if ((c1.getX() + 1) <= c1.getRoom().getRoomSizeX() && g.getDirection(c, c1) == WE) {
                            return (new CoordinatesWithRoom(c1.getX() + 1, c1.getY(), c1.getRoom()));

                        }
                        if ((c1.getX() - 1) > 0 && g.getDirection(c, c1) == EW) {
                            return (new CoordinatesWithRoom(c1.getX() - 1, c1.getY(), c1.getRoom()));
                        }

                        if ((c1.getY() + 1) <= c1.getRoom().getRoomSizeY() && g.getDirection(c, c1) == NS) {
                            return (new CoordinatesWithRoom(c1.getX() + 1, c1.getY(), c1.getRoom()));

                        }
                        if ((c1.getY() - 1) > 0 && g.getDirection(c, c1) == SN) {
                            return (new CoordinatesWithRoom(c1.getX() - 1, c1.getY(), c1.getRoom()));
                        }

                        for (int i = 0; i < g.getDoors().size(); i++) {

                            // C1 Room1 -> NOT C
                            if (c1.getRoom().getToken() == g.getDoors().get(i).getCoordinates1().getRoom().getToken()
                                    && c1.getX() == g.getDoors().get(i).getCoordinates1().getX()
                                    && c1.getY() == g.getDoors().get(i).getCoordinates1().getY()
                                    && c.getRoom().getToken() != g.getDoors().get(i).getCoordinates2().getRoom().getToken()
                                    && c.getX() != g.getDoors().get(i).getCoordinates2().getX()
                                    && c.getY() != g.getDoors().get(i).getCoordinates2().getY()) {

                                CoordinatesWithRoom c2 = new CoordinatesWithRoom(g.getDoors().get(i).getCoordinates2().getX(),
                                        g.getDoors().get(i).getCoordinates2().getY(), g.getDoors().get(i).getCoordinates2().getRoom());

                                if (g.getDirection(c1, c2) == EW && g.getDirection(c, c1) == EW && g.getDoors().get(i).getDir() == EW ||
                                        g.getDirection(c1, c2) == WE && g.getDirection(c, c1) == WE && g.getDoors().get(i).getDir() == WE ||
                                        g.getDirection(c1, c2) == SN && g.getDirection(c, c1) == SN && g.getDoors().get(i).getDir() == SN ||
                                        g.getDirection(c1, c2) == NS && g.getDirection(c, c1) == NS && g.getDoors().get(i).getDir() == NS) {

                                    return (new CoordinatesWithRoom(c2.getX(), c2.getY(), c2.getRoom()));
                                }
                            }
                            // C1 Room1 -> NOT C REVERSE
                            if (c1.getRoom().getToken() == g.getDoors().get(i).getCoordinates2().getRoom().getToken()
                                    && c1.getX() == g.getDoors().get(i).getCoordinates2().getX()
                                    && c1.getY() == g.getDoors().get(i).getCoordinates2().getY()
                                    && c.getRoom().getToken() != g.getDoors().get(i).getCoordinates1().getRoom().getToken()
                                    && c.getX() != g.getDoors().get(i).getCoordinates1().getX()
                                    && c.getY() != g.getDoors().get(i).getCoordinates1().getY()) {

                                CoordinatesWithRoom c3 = new CoordinatesWithRoom(g.getDoors().get(i).getCoordinates1().getX(),
                                        g.getDoors().get(i).getCoordinates1().getY(), g.getDoors().get(i).getCoordinates1().getRoom());

                                if (g.getDirection(c1, c3) == EW && g.getDirection(c, c1) == EW && g.getDoors().get(i).getDir() == WE ||
                                        g.getDirection(c1, c3) == WE && g.getDirection(c, c1) == WE && g.getDoors().get(i).getDir() == EW ||
                                        g.getDirection(c1, c3) == SN && g.getDirection(c, c1) == SN && g.getDoors().get(i).getDir() == NS ||
                                        g.getDirection(c1, c3) == NS && g.getDirection(c, c1) == NS && g.getDoors().get(i).getDir() == SN) {

                                    return (new CoordinatesWithRoom(c3.getX(), c3.getY(), c3.getRoom()));
                                }

                            }
                        }


                    return (new CoordinatesWithRoom(0, 0, c.getRoom()));
            }




////////////////////////////////////////////////////7
    /////// IF MOVES = 0 DO IT TILL YOU CAN????
public LinkedList<CoordinatesWithRoom> tilesSameDirectionWalls(int moves, GameBoard g) {
    CoordinatesWithRoom c0 = this;

    LinkedList<CoordinatesWithRoom> listOne = this.oneTileDistant(g); // CELL TO CHECK FOR NEXT
    LinkedList<CoordinatesWithRoom> list = new LinkedList<>(listOne);
////TODO
    return list;
}



}

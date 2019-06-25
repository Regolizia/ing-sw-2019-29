package adrenaline.powerups;

import adrenaline.*;

import java.util.LinkedList;
import java.util.List;

public class Teleporter  extends PowerUpCard {


    public Teleporter(AmmoCube.CubeColor color) {
        setPowerUpColor(color);
        setCanBeUsedOnBot(false);
    }

    @Override
    public String toString() {
        return "Teleporter, "+getPowerUpColor();
    }


    /**
     *getPossibleCells
     * @param m game model used
     * @param p target of teleporter
     * return a LInkedList<CoordinatesWithRoom> where the target can be moved
     */
    public List<CoordinatesWithRoom> getPossibleCells(GameModel m, Player p){

        List<CoordinatesWithRoom>cWr=new LinkedList<CoordinatesWithRoom>();
        for (Room r:m.getMapUsed().getGameBoard().getRooms()
             ) {
            for (int x=1;x<=r.getRoomSizeX();x++) {

                for (int y=1;y<=r.getRoomSizeY();y++)
                {
                    cWr.add(new CoordinatesWithRoom(x,y,r));
                }

            }

        }

        return cWr;
    }
}
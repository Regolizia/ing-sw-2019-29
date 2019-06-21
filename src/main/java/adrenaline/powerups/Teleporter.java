package adrenaline.powerups;

import adrenaline.*;

import java.util.LinkedList;

public class Teleporter  extends PowerUpCard {


    public Teleporter(AmmoCube.CubeColor color) {
        setPowerUpColor(color);
        setCanBeUsedOnBot(false);
    }

    @Override
    public String toString() {
        return "Teleporter, "+getPowerUpColor();
    }


    public LinkedList<CoordinatesWithRoom>getPossibleCells(GameModel m, Player p){

        LinkedList<CoordinatesWithRoom>cWr=new LinkedList<>();
        for (Room r:m.getMapUsed().getGameBoard().getRooms()
             ) {
            for (int x=0;x<r.getRoomSizeX();x++) {

                for (int y=0;y<r.getRoomSizeY();y++)
                {
                    cWr.add(new CoordinatesWithRoom(x,y,r));
                    if(cWr.equals(p.getCoordinatesWithRooms().getX()))
                        cWr.remove(p.getCoordinatesWithRooms());
                }

            }

        }

        return cWr;
    }
}
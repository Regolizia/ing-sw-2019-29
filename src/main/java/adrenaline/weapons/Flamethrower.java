package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Flamethrower extends WeaponCard {

    /**
     * Default constructor
     */
    public Flamethrower() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.ALT,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.ALT,false));
    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, AmmoCube.Effect e, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();

        // COORDINATES OF MY CELL
        int x = c.getX();
        int y = c.getY();

        // ADDS CELLS IF CELL HAS CELLS NEARBY
        if(x+1<=c.getRoom().getRoomSizeX())
            list.add(new CoordinatesWithRoom(x+1,y,c.getRoom()));

        if(y+1<=c.getRoom().getRoomSizeY())
            list.add(new CoordinatesWithRoom(x,y+1,c.getRoom()));

        if(x-1>0)
            list.add(new CoordinatesWithRoom(x-1,y,c.getRoom()));

        if(y-1>0)
            list.add(new CoordinatesWithRoom(x,y-1,c.getRoom()));

        //  ADDS CELLS IF CELL HAS DOORS
            for(int i=0;i<g.getDoors().size();i++) {
                if ((c.getRoom().getToken() == g.getDoors().get(i).getRoom1().getToken() && c.getX() == g.getDoors().get(i).getCoordinates1().getX() && c.getY() == g.getDoors().get(i).getCoordinates1().getY()))
                    list.add(new CoordinatesWithRoom(g.getDoors().get(i).getCoordinates2().getX(), g.getDoors().get(i).getCoordinates2().getY(), g.getDoors().get(i).getRoom2()));


                if ((c.getRoom().getToken() == g.getDoors().get(i).getRoom2().getToken() && c.getX() == g.getDoors().get(i).getCoordinates2().getX() && c.getY() == g.getDoors().get(i).getCoordinates2().getY()))
                    list.add(new CoordinatesWithRoom(g.getDoors().get(i).getCoordinates1().getX(), g.getDoors().get(i).getCoordinates1().getY(), g.getDoors().get(i).getRoom1()));
            }

        list = c.addCellDistant2ThroughDoor(list,c,g);


        // ADDS CELLS IF DISTANT 2, SAME DIRECTION
        if(x+2<=c.getRoom().getRoomSizeX())
            list.add(new CoordinatesWithRoom(x+1,y,c.getRoom()));

        if(y+2<=c.getRoom().getRoomSizeY())
            list.add(new CoordinatesWithRoom(x,y+1,c.getRoom()));

        if(x-2>0)
            list.add(new CoordinatesWithRoom(x-1,y,c.getRoom()));

        if(y-2>0)
            list.add(new CoordinatesWithRoom(x,y-1,c.getRoom()));

        return list;
    }



    // TODO TO BE CONTINUED... CHOICE OF TARGETS, APPLYDAMAGE

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 1-2 TARGETS, 2 SQUARES

                break;

            case ALT:   //
                break;


        }
    }
}
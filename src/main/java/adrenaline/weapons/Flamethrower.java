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
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
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

            en.setNumber(list.size());   // I KNOW HOW MANY CELLS ARE 1 DISTANT
            // I'LL NEED THIS INFO LATER WHEN I HAVE TO CHOOSE THE CELL

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

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {

        //ASK PLAYER TO CHOSE ONE OR TWO SQUARES (CHECK FIRST DISTANT 1, SECOND DISTANT 2, SAME DIR)
        // EN.GETNUMBER HAS THE NUMBER OF CELLS 1 DISTANT
        //IT'S EASIER THIS WAY

        LinkedList<Object> targets = new LinkedList<>();
        int targetsOneDistant = 0;

        if(en.getEffect()== AmmoCube.Effect.BASE){

        // MAKE PLAYER CHOOSE WHO IN FIRST SQUARE TO DAMAGE ( ...list.get(0)...)

                if(list.get(1)!=null){
                    // MAKE PLAYER CHOOSE WHO IN SECOND SQUARE TO DAMAGE ( ...list.get(1)...)
                }


            // TODO ADD POSSIBLE SPAWNPOINTS TO TARGETLIST

         return targets;
        }
        else {  // ALT EFFECT, ADD EVERYONE


            // FROM CELLS IN WEAPON RANGE GET ALL THE POSSIBLE TARGETS
            for(int k=0;k<m.getPlayers().size();k++) {
                for(int j=0;j<list.size();j++) {
                    if(m.getPlayers().get(k).getPlayerRoom()==list.get(j).getRoom() &&
                       m.getPlayers().get(k).getPlayerPositionX()==list.get(j).getX() &&
                       m.getPlayers().get(k).getPlayerPositionY()==list.get(j).getY()) {
                        if(j<en.getNumber()){
                           targetsOneDistant++; // TODO CHECK IF SPAWNPOINT
                        }

                        targets.add(m.getPlayers().get(k));
                        break;
                    }
                }
            }

            en.setNumber(targetsOneDistant);    // USED WHEN WE APPLY DAMAGE
            return targets;
        }
    }


    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 1-2 TARGETS, 2 SQUARES
                for(int j=0;j<targetList.size();j++){
                    if(targetList.get(j) instanceof Player) {
                        int i =((Player) targetList.get(j)).marksByShooter(p);
                        i++;
                        ((Player) targetList.get(j)).addDamageToTrack(p,i);
                    }

                    else {
                        // DAMAGE SPAWNPOINT
                    }

                }
                break;

            case ALT:   // 2 DAMAGE, EVERY TARGET FIRST SQUARE, 1 DAMAGE EVERY TARGET SECOND SQUARE
                        //EN.GETNUMBER HAS THE NUMBER OF TARGETS IN FIRST SQUARE
                for(int j=0;j<targetList.size();j++){
                    if(targetList.get(j) instanceof Player) {
                        int i =((Player) targetList.get(j)).marksByShooter(p);
                        if(j<e.getNumber()){
                            i++;
                        }
                        i++;
                        ((Player) targetList.get(j)).addDamageToTrack(p,i);
                    }

                    else {
                        // DAMAGE SPAWNPOINT
                    }

                }
                break;


        }
    }
}
package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class GrenadeLaucher extends WeaponCard {

    /**
     * Default constructor
     */
    public GrenadeLaucher() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.OP1,false));
    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        LinkedList<Object> targets = new LinkedList<>();
        if(en.getEffect()== AmmoCube.Effect.BASE){
             targets = super.fromCellsToTargets(list, c, g, p, m, en);

            // ASK WHICH TARGET TO DAMAGE, PUT IT IN TARGETS (REMOVE THE OTHERS)
            return targets;


        }
        else {    //  OP1 EFFECT

            // ASK WHICH CELL IN LIST TO DAMAGE, REMOVE THE OTHERS
            targets = super.fromCellsToTargets(list, c, g, p, m, en);

            return targets;

        }

    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 1 TARGET, THEN CAN MOVE IT
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+1;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                    moveOneSquare(((Player) targetList.get(0)).getPlayerPositionX(),
                            ((Player) targetList.get(0)).getPlayerPositionY(),((Player)
                                    targetList.get(0)).getPlayerRoom());


                }
                else {/*damage spawnpoint;*/}
                break;

            case OP1:   // 1 DAMAGE, EVERY PLAYER, 1 SQUARE
                int indexTarget=0;
                while(targetList.get(indexTarget)!=null)
                {
                    if(targetList.get(indexTarget) instanceof Player) {
                        int damage =((Player) targetList.get(indexTarget)).marksByShooter(p);
                        damage=damage+1;
                        ((Player) targetList.get(indexTarget)).addDamageToTrack(p,damage);
                    }
                    else {/*damage spawnpoint;*/}
                    indexTarget++;
                }
                break;


        }
    }


    public void moveOneSquare(int x,int y, Room room){
        boolean moveTarget=false;
        if(moveTarget==true)
            //move by one square
        if(moveTarget==false)
        return;}
}


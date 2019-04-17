package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Cyberblade extends WeaponCard {

    /**
     * Default constructor
     */
    public Cyberblade() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.FREE, AmmoCube.Effect.OP1,true)); // IF AN EFFECT IS FREE THIS IS ALWAYS TRUE
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.OP2,false));

    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        list.add(c);
        return list;
    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        LinkedList<Object> targets =  super.fromCellsToTargets(list, c, g, p, m, en);

        // ASK WHICH TARGET TO DAMAGE, REMOVE THE OTHERS

        return targets;

    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 2 DAMAGE, 1 TARGET
            case OP2:   // 2 DAMAGE, 1 DIFFERENT TARGET
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case OP1:   // MOVE 1-2 SQUARES, TODO REMOVE FROM HERE
                break;


        }
    }
}
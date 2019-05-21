package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

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
    public List<Object> fromCellsToTargets(List<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        List<Object> targets = new LinkedList<>();

        // I HAD TO COMMENT OUT THE IF BECAUSE, WITHOUT THE INPUT, IT WAS THE SAME

//        if(en.getEffect()== AmmoCube.Effect.BASE){
             targets = super.fromCellsToTargets(list, c, g, p, m, en);

            // ASK WHICH TARGET TO DAMAGE, PUT IT IN TARGETS (REMOVE THE OTHERS)
            return targets;


//        }        else {    //  OP1 EFFECT

            // ASK WHICH CELL IN LIST TO DAMAGE, REMOVE THE OTHERS
//            targets = super.fromCellsToTargets(list, c, g, p, m, en);

//            return targets;

//        }

    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 1 TARGET, THEN CAN MOVE IT
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i++;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case OP1:   // 1 DAMAGE, EVERY PLAYER, 1 SQUARE
                for (Object o : targetList) {
                    if (o instanceof Player) {
                        int i = ((Player) o).marksByShooter(p);
                        i++;
                        ((Player) o).addDamageToTrack(p, i);
                    } else {
                        // DAMAGE SPAWNPOINT
                    }
                }
                break;


        }
    }
}
package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

import static adrenaline.AmmoCube.Effect.BASE;

/**
 * 
 */
public class LockRifle extends WeaponCard {

    /**
     * Default constructor
     */
    public LockRifle() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.OP1,false));
    }


    @Override
    public List<Object> fromCellsToTargets(List<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        List<Object> targets = super.fromCellsToTargets(list, c, g, p, m, en);
        //ASK WHICH TARGET TO DAMAGE, REMOVE OTHERS
        return targets;
    }

    // TODO ADD MARKS
    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 2 DAMAGE, 1 MARK, 1 TARGET
                    if(targetList.get(0) instanceof Player){
                        int i =((Player) targetList.get(0)).marksByShooter(p);
                        i=i+2;
                        ((Player) targetList.get(0)).addDamageToTrack(p,i);

                        ((Player) targetList.get(0)).addMarks(p,1);

                        ((LinkedList)targetList).removeFirst();

                    }
                    else{
                        // DAMAGE SPAWNPOINT
                    }

                break;
            case OP1:   // 1 MARK, DIFFERENT TARGET
                if(targetList.get(0) instanceof Player){
                    ((Player) targetList.get(0)).addMarks(p,1);

                    ((LinkedList<Object>)targetList).removeFirst();

                }
                break;


        }

    }

    @Override
    public String toString() {
        return "LockRifle";
    }


}
package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Zx_2 extends WeaponCard {

    /**
     * Default constructor
     */
    public Zx_2() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,false));
    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        LinkedList<Object> targets = super.fromCellsToTargets(list, c, g, p, m, en);

        //CHOOSE 1 TARGET IF BASE EFFECT, UP TO 3 IF ALT, PUT THEM IN TARGETS
        return targets;
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 2 MARKS, 1 TARGET
                if(targetList.get(0) instanceof Player){
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i++;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                    ((Player) targetList.get(0)).addMarks(p,2);

                }
                else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case ALT:   // 1 MARK, UP TO 3 TARGETS
                for(int i = 0; i<e.getNumber(); i++){
                    ((Player)targetList.get(i)).addMarks(p,1);
                }
                // TODO CANNOT USE ON SPAWNPOINT ???
                break;


        }

    }

}
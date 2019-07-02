package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;


/**
 * represents Cyberblade WeaponCard
 */
public class Cyberblade extends WeaponCard {

    /**
     * Default constructor
     */
    public Cyberblade() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.OP2,false));

    }

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootOp1(){
        return true;
    }
    public boolean canShootOp2(){
        return true;
    }


    /**
     * getPossibleTargetCells()
     * @param c player coordinates
     * @param g used gameboard
     * @param en selected effect
     * @return possible cells where to shoot
    */

    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        list.add(c);
        return list;
    }

    /**
     * applyDamage()
     * @param p player who is doing damage
     * @param e selected effect
     * @param targetList selected targets
     */
    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {
setDamaged(targetList,p);
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

        }
    }

    @Override
    public String toString() {
        return "Cyberblade";
    }
}
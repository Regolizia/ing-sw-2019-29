package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

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

    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        list.add(c);
        return list;
    }


    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

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

            case OP1:   // MOVE 1 SQUARE, TODO REMOVE FROM HERE
                break;


        }
    }

    @Override
    public String toString() {
        return "Cyberblade";
    }
}
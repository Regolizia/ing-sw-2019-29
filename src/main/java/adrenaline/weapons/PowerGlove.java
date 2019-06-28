package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class PowerGlove extends WeaponCard {

    /**
     * Default constructor
     */
    public PowerGlove() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.ALT, false));
    }

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootAlt(){
        return true;
    }

    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
         return c.oneTileDistant(g, false);

    }


    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {
        setDamaged(targetList,p);
        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 2 MARKS, 1 TARGET

                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i++;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                    ((Player) targetList.get(0)).addMarks(p,2);
                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case ALT:   // 2 DAMAGE, 1-2 TARGETS

                for (int j = 0; j < e.getNumber(); j++) {
                    if (targetList.get(j) instanceof Player) {
                        int i = ((Player) targetList.get(j)).marksByShooter(p);
                        i = i + 2;
                        ((Player) targetList.get(j)).addDamageToTrack(p, i);

                    } else {
                        // DAMAGE SPAWNPOINT
                    }



                }
                break;
        }
    }
    @Override
    public String toString() {
        return "PowerGlove";
    }
}
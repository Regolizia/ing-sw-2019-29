package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.List;

/**
 * 
 */
public class VortexCannon extends WeaponCard {

    /**
     * Default constructor
     */
    public VortexCannon() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.OP1, false));
    }

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootOp1(){
        return true;
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
            case BASE:  // 2 DAMAGE, 1 TARGET, MOVE IT
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case OP1:   // 1 DAMAGE, UP TO 2 TARGETS
                for (Object o : targetList) {
                    if (o instanceof Player) {
                        int i = ((Player) o).marksByShooter(p);
                        i = i + 1;
                        ((Player) o).addDamageToTrack(p, i);
                    } else {
                        // DAMAGE SPAWNPOINT
                    }
                }
                break;


                //TODO MOVE THEM
        }
    }
    @Override
    public String toString() {
        return "VortexCannon";
    }
}
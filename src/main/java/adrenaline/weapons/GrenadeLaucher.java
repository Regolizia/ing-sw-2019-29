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

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootOp1(){
        return true;
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {
        setDamaged(targetList,p);
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
                    if (o instanceof Player && o!=p) { // TARGET DIFFERENT FROM PLAYER
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
    @Override
    public String toString() {
        return "GrenadeLauncher";
    }
}
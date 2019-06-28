package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.List;

/**
 * 
 */
public class MachineGun extends WeaponCard {

    /**
     * Default constructor
     */
    public MachineGun() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.OP1, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.OP2, false));
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
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {
        setDamaged(targetList,p);

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 1/2 TARGET
                for (Object o : targetList) {
                    if (targetList.get(0) instanceof Player) {
                        int i = ((Player) targetList.get(0)).marksByShooter(p);
                        i++;
                        ((Player) o).addDamageToTrack(p, i);

                    } else {
                        // DAMAGE SPAWNPOINT
                    }
                }
/*  IT'LL BE WHERE WE PUT THE CALL TO THIS METHOD
                targetList.removeFirst();
                if(targetList.size()==2){
                    targetList.removeFirst();
                }*/

                break;

            case OP1:   // 1 DAMAGE, 1 TARGET DIFFERENT FROM OP2 TARGET, ALSO IS ONE OF BASE TARGETS
            case OP2:   // 1 DAMAGE, 1 TARGET DIFFERENT FROM OP1 TARGET
                if (targetList.get(0) instanceof Player) {
                    int i = ((Player) targetList.get(0)).marksByShooter(p);
                    i++;
                    ((Player) targetList.get(0)).addDamageToTrack(p, i);

                } else {
                    // DAMAGE SPAWNPOINT
                }
/*

                targetList.removeFirst();
*/

                break;




        }
    }
    @Override
    public String toString() {
        return "MachineGun";
    }
}
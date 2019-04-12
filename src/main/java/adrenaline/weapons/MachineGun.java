package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

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


    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {


        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 1/2 TARGET
                for(int k=0;k<targetList.size();k++) {
                    if (targetList.get(0) instanceof Player) {
                        int i = ((Player) targetList.get(0)).marksByShooter(p);
                        i++;
                        ((Player) targetList.get(k)).addDamageToTrack(p, i);

                    } else {
                        // DAMAGE SPAWNPOINT
                    }
                }

                targetList.removeFirst();
                if(targetList.size()==2){
                    targetList.removeFirst();
                }

                break;

            // THEY ARE THE SAME WITH DIFFERENT TARGETS
            case OP1:   // 1 DAMAGE, 1 TARGET DIFFERENT FROM OP2 TARGET

            case OP2:   // 1 DAMAGE, 1 TARGET DIFFERENT FROM OP1 TARGET
                if (targetList.get(0) instanceof Player) {
                    int i = ((Player) targetList.get(0)).marksByShooter(p);
                    i++;
                    ((Player) targetList.get(0)).addDamageToTrack(p, i);

                } else {
                    // DAMAGE SPAWNPOINT
                }

                targetList.removeFirst();

                break;




        }
    }
}
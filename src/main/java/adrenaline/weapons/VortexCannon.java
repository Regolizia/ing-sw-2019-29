package adrenaline.weapons;

import adrenaline.AmmoCube;
import adrenaline.EffectAndNumber;
import adrenaline.Player;
import adrenaline.WeaponCard;

import java.util.LinkedList;

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

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

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
                for (int j = 0; j < targetList.size(); j++) {
                    if (targetList.get(j) instanceof Player) {
                        int i = ((Player) targetList.get(j)).marksByShooter(p);
                        i = i + 1;
                        ((Player) targetList.get(j)).addDamageToTrack(p, i);
                    } else {
                        // DAMAGE SPAWNPOINT
                    }
                }
                break;


                //TODO MOVE THEM
        }
    }
}
package adrenaline.weapons;

import adrenaline.AmmoCube;
import adrenaline.EffectAndNumber;
import adrenaline.Player;
import adrenaline.WeaponCard;

import java.util.LinkedList;

/**
 * 
 */
public class PlasmaGun extends WeaponCard {

    /**
     * Default constructor
     */
    public PlasmaGun() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.OP2,false));
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

            switch (e.getEffect()) {
                case BASE:  // 2 DAMAGE, 1 TARGET
                case OP2:   // 1 DAMAGE, SAME TARGET
                    if(targetList.get(0) instanceof Player) {
                        int i =((Player) targetList.get(0)).marksByShooter(p);

                        if(e.getEffect()== AmmoCube.Effect.BASE){
                            i++;
                        }
                        i++;

                        ((Player) targetList.get(0)).addDamageToTrack(p,i);
                    }

                    else {
                        // DAMAGE SPAWNPOINT
                    }
                    break;

                case OP1:   // MOVE 1 OR 2 SQUARES
                    // TODO
                    break;

            }


    }
}
package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Thor extends WeaponCard {

    /**
     * Default constructor
     */
    public Thor() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.OP1,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.OP2,false));
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

            switch (e.getEffect()) {
                case BASE:  // 2 DAMAGE, 1 TARGET
                case OP2:   // 2 DAMAGE, DIFFERENT TARGET
                    if(targetList.get(0) instanceof Player) {
                        int i =((Player) targetList.get(0)).marksByShooter(p);
                        i=i+2;
                        ((Player) targetList.get(0)).addDamageToTrack(p,i);
                    }
                    else {
                        // DAMAGE SPAWNPOINT
                    }
                    break;

                case OP1:   // 1 DAMAGE, 1 DIFFERENT TARGET
                    if(targetList.get(0) instanceof Player) {
                        int i =((Player) targetList.get(0)).marksByShooter(p);
                        i++;
                        ((Player) targetList.get(0)).addDamageToTrack(p,i);
                    }
                    else {
                        // DAMAGE SPAWNPOINT
                    }

                    break;


            }

        }

    }
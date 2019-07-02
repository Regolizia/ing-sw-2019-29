package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

/**
 * represent Electroscythe WeaponCard
 */
public class Electroscythe extends WeaponCard {

    /**
     * Default constructor
     */
    public Electroscythe() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.ALT,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.ALT,false));
    }

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootAlt(){
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
        List<CoordinatesWithRoom> list = new LinkedList<>();
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
            case BASE:  // 1 DAMAGE, EVERY PLAYER
            case ALT:   // 2 DAMAGE, EVERY PLAYER
                for (Object o : targetList) {
                    if (o instanceof Player) {
                        int i = ((Player) targetList.get(0)).marksByShooter(p);

                        if (e.getEffect() == AmmoCube.Effect.ALT) {
                            i++;
                        }
                        i++;

                        ((Player)o).addDamageToTrack(p, i);


                    } else {
                        // DAMAGE SPAWNPOINT
                    }
                }
                break;

        }
    }
    @Override
    public String toString() {
        return "Electroscythe";
    }
}
package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class Railgun extends WeaponCard {

    /**
     * Default constructor
     */
    public Railgun() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE, false));
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

        return c.tilesSameDirection(10,g,true);     // 10 IS BIG ENOUGH TO ADD ALL CELLS IN STRAIGHT LINES (MAX 10 LONG)

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
            case BASE:  // 3 DAMAGE, 1 TARGET
                if (targetList.get(0) instanceof Player) {
                    int i = ((Player) targetList.get(0)).marksByShooter(p);
                    i = i + 3;
                    ((Player) targetList.get(0)).addDamageToTrack(p, i);
                } else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case ALT:   // 2 DAMAGE, 1-2 TARGETS
                for (Object o : targetList) {
                    if (o instanceof Player) {
                        int i = ((Player) o).marksByShooter(p);
                        i = i + 2;
                        ((Player) o).addDamageToTrack(p, i);
                    } else {
                        // DAMAGE SPAWNPOINT
                    }

                }break;
                }
        }
    @Override
    public String toString() {
        return "Railgun";
    }
    }

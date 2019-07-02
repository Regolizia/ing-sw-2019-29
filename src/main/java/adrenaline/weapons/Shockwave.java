package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.List;

/**
 * 
 */
public class Shockwave extends WeaponCard {

    /**
     * Default constructor
     */
    public Shockwave() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.ALT, false));
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
        return c.oneTileDistant(g, false);
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
            case BASE:  // 1 DAMAGE, UP TO 3 TARGETS, DIFFERENT SQUARES
            case ALT:   // 1 DAMAGE, EVERY TARGET 1 MOVE AWAY
                for (Object o : targetList) {
                    if (o instanceof Player) {
                        int i = ((Player) o).marksByShooter(p);
                        i++;
                        ((Player) o).addDamageToTrack(p, i);

                    }
                }
                break;
        }
    }
    @Override
    public String toString() {
        return "Shockwave";
    }
}
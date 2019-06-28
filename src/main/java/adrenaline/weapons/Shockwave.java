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

    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        return c.oneTileDistant(g, false);
    }

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
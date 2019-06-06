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
        List<CoordinatesWithRoom> list = c.oneTileDistant(g, false);
        return list;
    }

    @Override
    public List<Object> fromCellsToTargets(List<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        List<Object> targets = super.fromCellsToTargets(list, c, g, p, m, en);

        if(en.getEffect()== AmmoCube.Effect.BASE) {
            // ASK WHICH TARGETS TO DAMAGE
            //CHECK IF THEY ARE IN DIFFERENT SQUARES, ELSE ASK AGAIN TO CHOOSE
            // PUT THEM IN TARGETS
        }
        return targets;
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

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
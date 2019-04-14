package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

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

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = c.oneTileDistant(g);
        return list;
    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        LinkedList<Object> targets = super.fromCellsToTargets(list, c, g, p, m, en);

        if(en.getEffect()== AmmoCube.Effect.BASE) {
            // ASK WHICH TARGETS TO DAMAGE
            //CHECK IF THEY ARE IN DIFFERENT SQUARES, ELSE ASK AGAIN TO CHOOSE
            // PUT THEM IN TARGETS
        }
        return targets;
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, UP TO 3 TARGETS, DIFFERENT SQUARES
            case ALT:   // 1 DAMAGE, EVERY TARGET 1 MOVE AWAY
                for (int j = 0; j < targetList.size(); j++) {
                    if (targetList.get(j) instanceof Player) {
                        int i = ((Player) targetList.get(j)).marksByShooter(p);
                        i++;
                        ((Player) targetList.get(j)).addDamageToTrack(p, i);

                        break;


                    }
                }
        }
    }
}
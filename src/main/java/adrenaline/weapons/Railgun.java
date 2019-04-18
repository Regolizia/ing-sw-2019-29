package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

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
        price.add(new AmmoCube(AmmoCube.CubeColor.FREE, AmmoCube.Effect.ALT, true));
        // DON'T CARE IF ALT IS PAID OR NOT BECAUSE IT'S FREE (IF YOU PAY BASE YOU ALREADY HAVE IT
        // BUT IT'S DIFFERENT FROM OP BECAUSE YOU CAN HAVE BASE OR ALT NOT BOTH)
    }


    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {

        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        list = c.tilesSameDirection(10,g,true);     // 10 IS BIG ENOUGH TO ADD ALL CELLS IN STRAIGHT LINES (MAX 10 LONG)

        return list;
    }


    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        LinkedList<Object> targets = super.fromCellsToTargets(list, c, g, p, m, en);

        /// ASK TO CHOOSE 1 (BASE) OR 1-2 (ALT) TARGETS, REMOVE OTHERS
        // THE 2 TARGETS OF ALT EFFECT HAVE TO BE IN THE SAME DIRECTION!!!!!
        // TODO CHECK THIS
        return targets;

    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

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
    }

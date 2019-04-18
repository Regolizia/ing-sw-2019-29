package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Shotgun extends WeaponCard {

    /**
     * Default constructor
     */
    public Shotgun() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.FREE, AmmoCube.Effect.ALT, true));
        // DON'T CARE IF ALT IS PAID OR NOT BECAUSE IT'S FREE (IF YOU PAY BASE YOU ALREADY HAVE IT
        // BUT IT'S DIFFERENT FROM OP BECAUSE YOU CAN HAVE BASE OR ALT NOT BOTH)
    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        if(en.getEffect() == AmmoCube.Effect.BASE){
            list.add(c);
        }
        if(en.getEffect() == AmmoCube.Effect.ALT){
            list = c.oneTileDistant(g, false);
        }
        return list;
    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        LinkedList<Object> targets = super.fromCellsToTargets(list, c, g, p, m, en);

        //CHOOSE 1 TARGET IF BASE EFFECT, 1 IF ALT, PUT IT IN TARGETS

        return targets;
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 3 DAMAGE, 1 TARGET
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+3;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                    break;
                }
                else {
                    // DAMAGE SPAWNPOINT
                }

                // TODO MOVE 1 IF YOU WANT
                break;

            case ALT:   // 2 DAMAGE, 1 TARGET
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                }
                else {
                    // DAMAGE SPAWNPOINT
                }
                break;
        }
    }
}
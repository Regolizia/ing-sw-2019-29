package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class RocketLaucher extends WeaponCard {

    /**
     * Default constructor
     */
    public RocketLaucher() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.OP1, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.OP2, false));
    }

    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        List list = super.getPossibleTargetCells(c, en, g);
        list = c.removeThisCell(list);
        return list;
    }

    @Override
    public List<Object> fromCellsToTargets(List<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        List<Object> targets = new LinkedList<>();

        // I HAD TO COMMENT OUT THE IF BECAUSE, WITHOUT THE INPUT, IT WAS THE SAME

//        if (en.getEffect() == AmmoCube.Effect.BASE) {
            targets = super.fromCellsToTargets(list, c, g, p, m, en);

            //ASK TO CHOOSE WHICH PLAYER TO DAMAGE, REMOVE THE OTHERS
            return targets;

//        } else {   // OP2 (OP1 WON'T CALL THESE METHODS)

//            targets = super.fromCellsToTargets(list, c, g, p, m, en); // TO BE REMOVED

            // ACTION CLASS GIVES ME THE OLD POSITION IN C
            //SELECT ALL THE PLAYERS IN C, PUT THEM IN TARGETS
            // ALSO ADD OLD TARGET

//            return targets;

//        }
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 2 DAMAGE, 1 TARGET, THEN YOU MAY MOVE IT 1 SQUARE (MOVE TARGET AFTER OP2 IF OP2 IS IN EFFECTSLIST)
                if (targetList.get(0) instanceof Player) {
                    int i = ((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    ((Player) targetList.get(0)).addDamageToTrack(p, i);
                } else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case OP2:   // 1 DAMAGE, EVERY TARGET IN TARGET'S FIRST SQUARE (ALSO THE TARGET)

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
}
package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class PowerGlove extends WeaponCard {

    /**
     * Default constructor
     */
    public PowerGlove() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.ALT, false));
    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();

            list.add(c);
            return list;

            // ALT: SECOND PART
            // IF PLAYER'S POSITION DIFFERENT FROM C
            // FIND CELL SAME DIRECTION MOVEMENT (CHECK ALSO DOORS)

    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {

        LinkedList<Object> targets = new LinkedList<>();

        if(en.getEffect() == AmmoCube.Effect.BASE) {
            targets = super.fromCellsToTargets(list, c, g, p, m, en);
            // ASK WHICH TARGET HERE TO DAMAGE, REMOVE THE OTHERS
            //TODO MOVE PLAYER THERE
            return targets;
        }
        else {
            en.setNumber(1);
            // CHOOSE TARGET FIRST SQUARE, REMOVE THE OTHERS
            // ASK IF WANT TO MOVE ONE MORE SQUARE SAME DIRECTION
            // IF SO en.setNumber(2);
            // NEEDED LATER
            //CHECK IF POSSIBLE
            //MOVE THERE
            //LinkedList<CoordinatesWithRoom> list = getPossibleTargetCells(c,en,g);    with new position
            //THEN CALL THE SUPERMETHOD FROMCELLSTOTARGETS WITH THIS LIST
            //ASK WHICH TARGET, REMOVE THE OTHERS
            return targets;

        }

    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 2 MARKS, 1 TARGET

                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                    ((Player) targetList.get(0)).addMarks(p,1);
                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case ALT:   // 2 DAMAGE, 1-2 TARGETS

                for (int j = 0; j < e.getNumber(); j++) {
                    if (targetList.get(j) instanceof Player) {
                        int i = ((Player) targetList.get(j)).marksByShooter(p);
                        i = i + 2;
                        ((Player) targetList.get(j)).addDamageToTrack(p, i);

                    } else {
                        // DAMAGE SPAWNPOINT
                    }
                    break;


                }
        }
    }
}
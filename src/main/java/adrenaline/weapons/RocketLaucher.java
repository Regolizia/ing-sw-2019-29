package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

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
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList list = super.getPossibleTargetCells(c, en, g);
        list = c.removeThisCell(list);
        return list;
    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        LinkedList<Object> targets = new LinkedList<>();
        if (en.getEffect() == AmmoCube.Effect.BASE) {
            targets = super.fromCellsToTargets(list, c, g, p, m, en);

            //ASK TO CHOOSE WHICH PLAYER TO DAMAGE, REMOVE THE OTHERS
            return targets;

        } else {   // OP2 (OP1 WON'T CALL THESE METHODS)

            // ACTION CLASS GIVES ME THE OLD POSITION IN C
            //SELECT ALL THE PLAYERS IN C, PUT THEM IN TARGETS
            // ALSO ADD OLD TARGET

            return targets;

        }
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 2 DAMAGE, 1 TARGET, THEN YOU MAY MOVE IT 1 SQUARE (MOVE TARGET AFTER OP2 IF OP2 IS IN EFFECTSLIST)
                if (targetList.get(0) instanceof Player) {
                    int i = ((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2; //2 damage
                    ((Player) targetList.get(0)).addDamageToTrack(p, i);
                } else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case OP1:   // MOVE 1-2 SQUARES (TODO, DELETE THESE CASES WITH MOVES, PUT THEM SOMEWHERE) we can put them in base and check with if
                //to get the paid option ?? like in Sledgehammer?? REMEMBER i've to buy the base if i want to buy the option
                break;

            case OP2:   // 1 DAMAGE, EVERY TARGET IN TARGET'S FIRST SQUARE (ALSO THE TARGET)
                int i=0;
                while(targetList.get(i)!=null)
                {
                    if (targetList.get(i) instanceof Player) {
                  int damage=((Player) targetList.get(i)).marksByShooter(p);
                  damage++;
                    ((Player) targetList.get(i)).addDamageToTrack(p, damage);
                    i++;}
                    else {}//targetDamage??
                }
               /* for (int j = 0; j < targetList.size(); j++) {
                    if (targetList.get(j) instanceof Player) {
                        int i = ((Player) targetList.get(j)).marksByShooter(p);
                        i++;
                        ((Player) targetList.get(j)).addDamageToTrack(p, i);


                        break;

                    }*/

                break;
        }
    }
}
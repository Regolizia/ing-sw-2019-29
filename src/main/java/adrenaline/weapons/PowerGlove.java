package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

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

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootAlt(){
        return true;
    }

    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        if(en.getEffect()== AmmoCube.Effect.BASE)
            return c.oneTileDistant(g, false);

          else {  // ALT: SECOND PART
            // IF PLAYER'S POSITION DIFFERENT FROM C
            // FIND CELL SAME DIRECTION MOVEMENT (CHECK ALSO DOORS)
            return c.oneTileDistant(g, false);// REMOVEEEEE
        }
    }

    @Override
    public List<Object> fromCellsToTargets(List<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {

        List<Object> targets = new LinkedList<>();

        if(en.getEffect() == AmmoCube.Effect.BASE) {
            targets = super.fromCellsToTargets(list, c, g, p, m, en);
            // ASK WHICH TARGET HERE TO DAMAGE, REMOVE THE OTHERS
            //TODO MOVE PLAYER THERE
            return targets;
        }
       // else {
       //     en.setNumber(1);
            // CHOOSE TARGET FIRST SQUARE, REMOVE THE OTHERS
            // ASK IF WANT TO MOVE ONE MORE SQUARE SAME DIRECTION
            // IF SO en.setNumber(2);
            // NEEDED LATER
            // CHECK IF POSSIBLE
            // MOVE THERE
            // original position C0
            // new position C1
       /*
            IF NEXT CELL EXISTS IT'S CALLED C2
            CoordinatesWithRoom c2;
             c2.getNextCell(c0,c1,g);
            PUT IT IN A LIST
            LinkedList<CoordinatesWithRoom> listOne = new LinkedList<>;
            listOne.add(c2);

            if(c2.getX()==0 || c2.getY()==0){
                IT DOESN'T EXIST
                return empty list
            }
                        */
            //  IF C2 EXISTS
            //THEN CALL THE SUPERMETHOD FROMCELLSTOTARGETS WITH THIS LIST (TO GET TARGETS)
            //ASK WHICH TARGET, REMOVE THE OTHERS
            return targets;

      //  }

    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 2 MARKS, 1 TARGET

                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i++;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                    ((Player) targetList.get(0)).addMarks(p,2);
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



                }
                break;
        }
    }
    @Override
    public String toString() {
        return "PowerGlove";
    }
}
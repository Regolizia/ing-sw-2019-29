package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Flamethrower extends WeaponCard {

    /**
     * Default constructor
     */
    public Flamethrower() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.ALT,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.ALT,false));
    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {

        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();


        list = c.tilesSameDirection(2,g,false);
        list.remove(c);

        return list;
    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {

        //ASK PLAYER TO CHOSE ONE OR TWO SQUARES (CHECK FIRST DISTANT 1, SECOND DISTANT 2, SAME DIR)

        LinkedList<Object> targets = new LinkedList<>();
        LinkedList<Object> targets1 = new LinkedList<>();
        LinkedList<Object> targets2 = new LinkedList<>();
        LinkedList<CoordinatesWithRoom> listOne = c.oneTileDistant(g, false);
        en.setNumber(listOne.size());

        if (en.getEffect() == AmmoCube.Effect.BASE) {
  /*
        // MAKE PLAYER CHOOSE WHO IN FIRST SQUARE TO DAMAGE ( ...list.get(0)...)

                if(list.get(1)!=null){
                    // MAKE PLAYER CHOOSE WHO IN SECOND SQUARE TO DAMAGE ( ...list.get(1)...)
                }
*/
            targets = super.fromCellsToTargets(list, c, g, p, m, en);
            // TODO ADD POSSIBLE SPAWNPOINTS TO TARGETLIST

            return targets;
        } else {  // ALT EFFECT, ADD EVERYONE
            targets1 = super.fromCellsToTargets(listOne, c, g, p, m, en);
            targets = super.fromCellsToTargets(list, c, g, p, m, en);
     targets.removeAll(targets1);
     targets1.addAll(targets);


            System.out.println(en.getNumber());
            return targets1;
        }
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 1-2 TARGETS, 2 SQUARES
                for(int j=0;j<targetList.size();j++){
                    if(targetList.get(j) instanceof Player) {
                        int i =((Player) targetList.get(j)).marksByShooter(p);
                        i++;
                        ((Player) targetList.get(j)).addDamageToTrack(p,i);
                    }

                    else {
                        // DAMAGE SPAWNPOINT
                    }

                }
                break;

            case ALT:   // 2 DAMAGE, EVERY TARGET FIRST SQUARE, 1 DAMAGE EVERY TARGET SECOND SQUARE
                        //EN.GETNUMBER HAS THE NUMBER OF TARGETS IN FIRST SQUARE
                for(int j=0;j<targetList.size();j++){
                    if(targetList.get(j) instanceof Player) {
                        int i =((Player) targetList.get(j)).marksByShooter(p);
                        if(j<e.getNumber()){
                            i++;
                        }
                        i++;
                        ((Player) targetList.get(j)).addDamageToTrack(p,i);
                    }

                    else {
                        // DAMAGE SPAWNPOINT
                    }

                }
                break;


        }
    }
}
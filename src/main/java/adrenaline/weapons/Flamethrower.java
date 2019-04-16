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
        LinkedList<CoordinatesWithRoom> temp = c.oneTileDistant(g);
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();


        en.setNumber(temp.size());   // I KNOW HOW MANY CELLS ARE 1 DISTANT
            // I'LL NEED THIS INFO LATER WHEN I HAVE TO CHOOSE THE CELL

        list = c.tilesSameDirection(2,g);
        list.remove(c);

        return list;
    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {

        //ASK PLAYER TO CHOSE ONE OR TWO SQUARES (CHECK FIRST DISTANT 1, SECOND DISTANT 2, SAME DIR)
        // EN.GETNUMBER HAS THE NUMBER OF CELLS 1 DISTANT
        //IT'S EASIER THIS WAY

        LinkedList<Object> targets = new LinkedList<>();
        LinkedList<Object> targets1 = new LinkedList<>();
        LinkedList<Object> targets2 = new LinkedList<>();
        LinkedList<CoordinatesWithRoom> listOne = c.oneTileDistant(g);
        int x = 0;

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

            targets = super.fromCellsToTargets(list, c, g, p, m, en);
            for (Object o : targets) {
             for(CoordinatesWithRoom c2 : listOne) {
                 CoordinatesWithRoom c1 = new CoordinatesWithRoom(((Player) o).getPlayerPositionX(),
                         ((Player) o).getPlayerPositionY(), ((Player) o).getPlayerRoom());
                 if (c1.getX()==c2.getX()&&c1.getY()==c2.getY()&&c1.getRoom().getToken()==c2.getRoom().getToken()) {
                            x++;                //NUMBER OF TARGETS DISTANT 1
                     targets1.add(o);
                 } else {
                     targets2.add(o);
                 }
             }
            }

            targets1.addAll(targets2);

            en.setNumber(x);    // USED WHEN WE APPLY DAMAGE
            return targets1;
        }
    }




      /*      // FROM CELLS IN WEAPON RANGE GET ALL THE POSSIBLE TARGETS
            for(int k=0;k<m.getPlayers().size();k++) {
                for(int j=0;j<list.size();j++) {
                    if(m.getPlayers().get(k).getPlayerRoom()==list.get(j).getRoom() &&
                       m.getPlayers().get(k).getPlayerPositionX()==list.get(j).getX() &&
                       m.getPlayers().get(k).getPlayerPositionY()==list.get(j).getY()) {
                        if(j<en.getNumber()){
                           targetsOneDistant++; // TODO CHECK IF SPAWNPOINT
                        }

                        targets.add(m.getPlayers().get(k));
                        break;*/



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
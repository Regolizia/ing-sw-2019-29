package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Hellion extends WeaponCard {

    /**
     * Default constructor
     */
    public Hellion() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.ALT,false));
    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList list = super.getPossibleTargetCells(c, en, g);
        LinkedList listOne = c.oneTileDistant(g);
        listOne.add(c);

        // TODO CHECK IF IT WORKS
        list.removeAll(listOne);

        return list;
    }

    @Override
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        LinkedList<Object> targets = super.fromCellsToTargets(list, c, g, p, m, en);

        // GET JUST ONE TARGET OUT OF FROMCELLSTOTARGETS
        // ASK WHICH TARGET

        // ADD THE OTHER PLAYERS THAT ARE IN THE SAME SPOT AS THE CHOSEN TARGET
        //  TODO ALSO ADD SPAWNPOINTS
        // chosenTarget =
        //int x= getPositionX
        //int y= getPositionY
        //int token= getRoom().getToken()
       /*
        for(int k=0;k<m.getPlayers().size();k++){
        if(m.getPlayers().get(k).getPlayerPositionX()==x && m.getPlayers().get(k).getPlayerPositionY()==y &&
                m.getPlayers().get(k).getPlayerRoom().getToken()==token){
            targets.add(m.getPlayers().get(k));
        }
        }
        */
        return targets;


    }



    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, 1 TARGET, 1 MARK, EVERY PLAYER IN THAT SQUARE
            case ALT:   // 1 DAMAGE, 1 TARGET, 2 MARKS, EVERY PLAYER IN THAT SQUARE
                for(int j=0;j<targetList.size();j++) {
                    if (targetList.get(j) instanceof Player) {
                        if(j==0) {
                            int i = ((Player) targetList.get(0)).marksByShooter(p);
                            i++;
                            ((Player) targetList.get(0)).addDamageToTrack(p, i);
                        }
                        if(e.getEffect()== AmmoCube.Effect.ALT){
                            ((Player) targetList.get(0)).addMarks(p,1);
                        }
                        ((Player) targetList.get(j)).addMarks(p,1);

                    } else {
                        // DAMAGE SPAWNPOINT
                    }

                }

                break;



        }
    }
}
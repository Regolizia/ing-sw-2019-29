package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class TractorBeam extends WeaponCard {

    /**
     * Default constructor
     */
    public TractorBeam() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.ALT,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.ALT,false));

    }

    // USED FOR ALT EFFECT
    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        if(en.getEffect()== AmmoCube.Effect.ALT){
            List<CoordinatesWithRoom> list = c.oneTileDistant(g, false);
            list.addAll(c.xTilesDistant(g,2));
            list.add(c);
            return list;
        }
        else {
            return super.getPossibleTargetCells(c, en, g);
        }
    }

    // USED FOR BASE EFFECT
    // MOVE 0-1-2 1 TARGET TO A CELL YOU SEE
    @Override
    public List<Object> fromCellsToTargets(List<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        if(en.getEffect()== AmmoCube.Effect.BASE) {
            LinkedList<Object> listOne = new LinkedList<>();
            List<CoordinatesWithRoom> listMoves;
            CoordinatesWithRoom c1 = new CoordinatesWithRoom();
            for (Player element : m.getPlayers()) {
                if (element.getColor() != p.getColor()) {   // ADD OTHER PLAYERS TO listOne IF, MOVING THEM, I SEE THEM
                    c1.setX(element.getPlayerPositionX());
                    c1.setY(element.getPlayerPositionY());
                    c1.setRoom(element.getPlayerRoom());
                    listMoves = c1.oneTileDistant(g, false);
                    listMoves.addAll(c1.xTilesDistant(g, 2));
                    listMoves.add(c1);  // MUST BE AFTER XTILES, ELSE IT IS REMOVED

                    if(c1.isCWRInTwoLists(listMoves,list)){
                        listOne.add(element);
                    }
                }
            }


            // ASK WHICH TARGET
            // (REMEMBER TO MOVE THE CHOSEN TARGET)
            // GET JUST ONE

            return listOne;
        }
        else{
            return super.fromCellsToTargets(list,c,g,p,m,en);
        }
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // MOVE 0-1-2 1 TARGET (NOT SPAWNPOINT)
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i++;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                }

                else {
                    // DAMAGE SPAWNPOINT
                }

                break;

            case ALT:   // MOVE 1 TARGET TO MY SQUARE, 3 DAMAGE
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+3;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;

        }

    }

}
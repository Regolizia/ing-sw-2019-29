package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.List;

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

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootAlt(){
        return true;
    }


    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {

        List<CoordinatesWithRoom> list = c.tilesSameDirection(2,g,false);
        list.remove(c);

        return list;
    }

    @Override
    public List<Object> fromCellsToTargets(List<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {

        List<Object> targets= super.fromCellsToTargets(list, c, g, p, m, en);
        List<CoordinatesWithRoom> listOne = c.oneTileDistant(g, false);
        en.setNumber(listOne.size());

        return targets;
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

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
    @Override
    public String toString() {
        return "Flamethrower";
    }
}
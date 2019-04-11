package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Electroscythe extends WeaponCard {

    /**
     * Default constructor
     */
    public Electroscythe() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.ALT,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.ALT,false));
    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, AmmoCube.Effect e, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        list.add(c);
        return list;
    }

    // TODO WE DON'T HAVE TO ASK IF THIS KIND OF WEAPON

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 1 DAMAGE, EVERY PLAYER
                for(int j=0;j<targetList.size();j++){
                    if(targetList.get(j) instanceof Player) {
                        for (int i = 0; i < ((Player) targetList.get(0)).getTrack().length; i++) {  //FIND FIRST EMPTY CELL OF TRACK
                            if (((Player) targetList.get(0)).getTrack()[i] == Figure.PlayerColor.NONE) {
                                ((Player) targetList.get(0)).getTrack()[i] = p.getColor();
                                break;
                            }
                        }
                    }
                    else{
                        // DAMAGE SPAWNPOINT
                    }
                }
                break;

            case ALT:   // 2 DAMAGE, EVERY PLAYER
                for(int j=0;j<targetList.size();j++){
                    if(targetList.get(j) instanceof Player) {
                        for (int i = 0; i < ((Player) targetList.get(0)).getTrack().length; i++) {  //FIND FIRST EMPTY CELL OF TRACK
                            if (((Player) targetList.get(0)).getTrack()[i] == Figure.PlayerColor.NONE) {
                                ((Player) targetList.get(0)).getTrack()[i] = p.getColor();
                                ((Player) targetList.get(0)).getTrack()[i+1] = p.getColor();
                                break;
                            }
                        }
                    }
                    else{
                        // DAMAGE SPAWNPOINT
                    }
                }
                break;


        }
    }
}
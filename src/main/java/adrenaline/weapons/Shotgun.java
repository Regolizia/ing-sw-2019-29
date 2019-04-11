package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Shotgun extends WeaponCard {

    /**
     * Default constructor
     */
    public Shotgun() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, AmmoCube.Effect e, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        if(e == AmmoCube.Effect.BASE){
            list.add(c);
        }
        if(e == AmmoCube.Effect.ALT){
            list = c.oneTileDistant(g);
        }
        return list;
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 3 DAMAGE, 1 TARGET
                if(targetList.get(0) instanceof Player) {
                    for(int i=0;i<((Player) targetList.get(0)).getTrack().length;i++) {  //FIND FIRST EMPTY CELL OF TRACK
                        if (((Player) targetList.get(0)).getTrack()[i] == Figure.PlayerColor.NONE) {
                            ((Player) targetList.get(0)).getTrack()[i] = p.getColor();
                            ((Player) targetList.get(0)).getTrack()[i + 1] = p.getColor();
                            if (((Player) targetList.get(0)).getTrack()[12] == Figure.PlayerColor.NONE){
                                ((Player) targetList.get(0)).getTrack()[i + 2] = p.getColor();}
                            break;
                        }
                    }
                }
                else {
                    // DAMAGE SPAWNPOINT
                }

                // TODO MOVE 1 IF YOU WANT
                break;
            case ALT:   // 2 DAMAGE, 1 TARGET
                if(targetList.get(0) instanceof Player) {
                    for(int i=0;i<((Player) targetList.get(0)).getTrack().length;i++) {  //FIND FIRST EMPTY CELL OF TRACK
                        if (((Player) targetList.get(0)).getTrack()[i] == Figure.PlayerColor.NONE) {
                            ((Player) targetList.get(0)).getTrack()[i] = p.getColor();
                            ((Player) targetList.get(0)).getTrack()[i + 1] = p.getColor();
                            break;
                        }
                    }
                }
                else {
                    // DAMAGE SPAWNPOINT
                }

        }
    }
}
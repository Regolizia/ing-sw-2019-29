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
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+3;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                    break;
                }
                else {
                    // DAMAGE SPAWNPOINT
                }

                // TODO MOVE 1 IF YOU WANT
                break;

            case ALT:   // 2 DAMAGE, 1 TARGET
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                }
                else {
                    // DAMAGE SPAWNPOINT
                }
                break;
        }
    }
}
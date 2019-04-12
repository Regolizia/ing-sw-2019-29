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
                        ((Player) targetList.get(j)).addDamageToTrack(p,1);


                    }
                    else{
                        // DAMAGE SPAWNPOINT
                    }
                }
                break;

            case ALT:   // 2 DAMAGE, EVERY PLAYER
                for(int j=0;j<targetList.size();j++){
                    if(targetList.get(j) instanceof Player) {
                        ((Player) targetList.get(j)).addDamageToTrack(p,2);
                    }
                    else{
                        // DAMAGE SPAWNPOINT
                    }
                }
                break;


        }
    }
}
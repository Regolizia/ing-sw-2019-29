package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

import static adrenaline.AmmoCube.Effect.BASE;

/**
 * 
 */
public class LockRifle extends WeaponCard {

    /**
     * Default constructor
     */
    public LockRifle() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.OP1,false));
    }

    // TODO ADD MARKS
    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 2 DAMAGE, 1 MARK, 1 TARGET
                    if(targetList.get(0) instanceof Player){
                        for(int i=0;i<((Player) targetList.get(0)).getTrack().length;i++){  //FIND FIRST EMPTY CELL OF TRACK
                            if(((Player) targetList.get(0)).getTrack()[i]== Figure.PlayerColor.NONE){
                                ((Player) targetList.get(0)).getTrack()[i] = p.getColor();
                                ((Player) targetList.get(0)).getTrack()[i+1] = p.getColor();

                                //  TODO

                                targetList.removeFirst();

                                break;
                            }
                        }

                    }
                    else{
                        // DAMAGE SPAWNPOINT
                    }



                break;
            case OP1:   // 1 MARK, DIFFERENT TARGET

                // TODO

                targetList.removeFirst();

                break;


        }

    }




}
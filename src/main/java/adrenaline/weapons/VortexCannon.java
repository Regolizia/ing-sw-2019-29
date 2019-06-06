package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.List;

/**
 * 
 */
public class VortexCannon extends WeaponCard {

    /**
     * Default constructor
     */
    public VortexCannon() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.OP1, false));
    }

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootOp1(){
        return true;
    }

    // THE COORDINATE PASSED C IS THE VORTEX
    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        List<CoordinatesWithRoom> list = super.getPossibleTargetCells(c,en,g);
/*
        // ASK PLAYER WHERE TO PUT VORTEX (CELL THAT ARE SEEN)
        CoordinatesWithRoom vortex = new CoordinatesWithRoom(); // TODO GET SELECTED CELL FROM LIST, PUT IT HERE
        list.clear();
        list = vortex.oneTileDistant(g);
        list.add(vortex);*/
        return list;
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 2 DAMAGE, 1 TARGET, MOVE IT
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case OP1:   // 1 DAMAGE, UP TO 2 TARGETS
                for (Object o : targetList) {
                    if (o instanceof Player) {
                        int i = ((Player) o).marksByShooter(p);
                        i = i + 1;
                        ((Player) o).addDamageToTrack(p, i);
                    } else {
                        // DAMAGE SPAWNPOINT
                    }
                }
                break;


                //TODO MOVE THEM
        }
    }
    @Override
    public String toString() {
        return "VortexCannon";
    }
}
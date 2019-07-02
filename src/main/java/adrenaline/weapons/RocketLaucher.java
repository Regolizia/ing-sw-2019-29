package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class RocketLaucher extends WeaponCard {

    /**
     * Default constructor
     */
    public RocketLaucher() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.OP1, false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.OP2, false));
    }

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootOp1(){
        return true;
    }
    public boolean canShootOp2(){
        return true;
    }
    /**
     * getPossibleTargetCells()
     * @param c player coordinates
     * @param g used gameboard
     * @param en selected effect
     * @return possible cells where to shoot
     */
    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        List list = super.getPossibleTargetCells(c, en, g);
        list = c.removeThisCell(list);
        return list;
    }

    /**
     * applyDamage()
     * @param p player who is doing damage
     * @param e selected effect
     * @param targetList selected targets
     */
    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {
        setDamaged(targetList,p);
        switch (e.getEffect()) {
            case BASE:  // 2 DAMAGE, 1 TARGET, THEN YOU MAY MOVE IT 1 SQUARE (MOVE TARGET AFTER OP2 IF OP2 IS IN EFFECTSLIST)
                if (targetList.get(0) instanceof Player) {
                    int i = ((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    ((Player) targetList.get(0)).addDamageToTrack(p, i);
                } else {
                    // DAMAGE SPAWNPOINT
                }
                break;

            case OP2:   // 1 DAMAGE, EVERY TARGET IN TARGET'S FIRST SQUARE (ALSO THE TARGET)

                for (Object o : targetList) {
                    if (o instanceof Player) {
                        int i = ((Player) o).marksByShooter(p);
                        i++;
                        ((Player) o).addDamageToTrack(p, i);

                    }
                }
                break;
        }
    }
    @Override
    public String toString() {
        return "RocketLauncher";
    }
}
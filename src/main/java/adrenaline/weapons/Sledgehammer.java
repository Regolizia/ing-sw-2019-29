package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class Sledgehammer extends WeaponCard {
    @Override
    public String toString() {
        return "Sledgehammer";
    }

    /**
     * Default constructor
     */
    public Sledgehammer() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.ALT, false));
    }

    public boolean canShootBase() {
        return true;
    }

    public boolean canShootAlt() {
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
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        list.add(c);
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
            case BASE:  // 2 DAMAGE, 1 TARGET
            case ALT:   // 3 DAMAGE, 1 TARGET
                if (targetList.get(0) instanceof Player) {
                    int i = ((Player) targetList.get(0)).marksByShooter(p);
                    i = i + 2;
                    if (e.getEffect() == AmmoCube.Effect.ALT) {
                        i++;
                    }
                    ((Player) targetList.get(0)).addDamageToTrack(p, i);
                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;


        }
    }
}

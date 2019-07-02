package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.List;

/**
 * 
 */
public class PlasmaGun extends WeaponCard {

    /**
     * Default constructor
     */
    public PlasmaGun() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.OP2,false));
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

        if(en.getEffect()== AmmoCube.Effect.OP1) {
            List<CoordinatesWithRoom> list = c.oneTileDistant(g,false);
            list.addAll(c.xTilesDistant(g, 2));
            list.remove(c);

            return list;
        }
        else{
            return super.getPossibleTargetCells(c,en,g);
        }
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
                case OP2:   // 1 DAMAGE, SAME TARGET
                    if(targetList.get(0) instanceof Player) {
                        int i =((Player) targetList.get(0)).marksByShooter(p);

                        if(e.getEffect()== AmmoCube.Effect.BASE){
                            i++;
                        }
                        i++;

                        ((Player) targetList.get(0)).addDamageToTrack(p,i);
                    }

                    else {
                        // DAMAGE SPAWNPOINT
                    }
                    break;

                case OP1:   // MOVE 1 OR 2 SQUARES
                    // TODO
                    break;

            }


    }
    @Override
    public String toString() {
        return "PlasmaGun";
    }
}
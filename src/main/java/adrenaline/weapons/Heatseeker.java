package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Heatseeker extends WeaponCard {

    /**
     * Default constructor
     */
    public Heatseeker() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
    }

    @Override
    public LinkedList<Object> proposeTargets(CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, AmmoCube.Effect e) {
        LinkedList<Object> list = super.proposeTargets(c, g, p, m, e);
        LinkedList<Object> listOthers = new LinkedList<>();


        // TODO CHECK IF IT WORKS
        listOthers.removeAll(list);

        return listOthers;
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:  // 3 DAMAGE, 1 TARGET
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+3;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                }
                else {
                    // DAMAGE SPAWNPOINT
                }
                break;


        }
    }
}
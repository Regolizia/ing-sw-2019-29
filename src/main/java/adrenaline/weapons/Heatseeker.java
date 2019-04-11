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
                break;


        }
    }
}
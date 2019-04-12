package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

/**
 * 
 */
public class Whisper extends WeaponCard {

    /**
     * Default constructor
     */
    public Whisper() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, AmmoCube.Effect e, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = super.getPossibleTargetCells(c, e, g);
        LinkedList<CoordinatesWithRoom> listOne = c.oneTileDistant(g);

        // TODO CHECK IF IT WORKS
        list.removeAll(listOne);

        return list;
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {


        switch (e.getEffect()) {
            case BASE:  // 3 DAMAGE, 1 MARK, 1 TARGET
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+3;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                    ((Player) targetList.get(0)).addMarks(p,1);
                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;


        }

    }

    }
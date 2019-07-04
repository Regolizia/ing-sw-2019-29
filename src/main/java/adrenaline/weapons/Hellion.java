package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class Hellion extends WeaponCard {

    /**
     * Default constructor
     */
    public Hellion() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.ALT,false));
    }

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootAlt(){
        return true;
    }

    // NOT YOUR SQUARE
    /**
     * getPossibleTargetCells()
     * @param c player coordinates
     * @param g used gameboard
     * @param en selected effect
     * @return possible cells where to shoot
     */
    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        List<CoordinatesWithRoom> list = super.getPossibleTargetCells(c, en, g);
        List<CoordinatesWithRoom> listOne = new LinkedList();
        listOne = c.oneTileDistant(g, false);
        listOne.add(c);

        for(int k=(list.size()-1);k>=0;k--){
            for(CoordinatesWithRoom c2: listOne){
                if(list.get(k).getX()==c2.getX() &&
                        list.get(k).getY()==c2.getY() &&
                        list.get(k).getRoom().getToken()==c2.getRoom().getToken()){
                    list.remove(k);
                }
            }
        }

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
            case BASE:  // 1 DAMAGE, 1 TARGET, 1 MARK, EVERY PLAYER IN THAT SQUARE
            case ALT:   // 1 DAMAGE, 1 TARGET, 2 MARKS, EVERY PLAYER IN THAT SQUARE
                for(int j=0;j<targetList.size();j++) {
                    if (targetList.get(j) instanceof Player) {
                        if(j==0) {
                            int i = ((Player) targetList.get(j)).marksByShooter(p);
                            i++;
                            ((Player) targetList.get(j)).addDamageToTrack(p, i);
                        }
                        if(e.getEffect()== AmmoCube.Effect.ALT){
                            ((Player) targetList.get(j)).addMarks(p,1);
                        }


                        ((Player) targetList.get(j)).addMarks(p,1);

                    } else {
                        // DAMAGE SPAWNPOINT
                    }

                }

                break;



        }
    }
    @Override
    public String toString() {
        return "Hellion";
    }
}
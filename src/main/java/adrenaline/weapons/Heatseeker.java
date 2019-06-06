package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

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

    public boolean canShootBase(){
        return true;
    }

    @Override
    public List<Object> fromCellsToTargets(List<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        List<Object> listOne = super.fromCellsToTargets(list,c, g, p, m, en);
        List<Object> listOthers = new LinkedList<>();

        // TODO ALL PLAYERS AND SPAWNPOINTS(IF DOMINATION) TO LISTOTHERS

        listOthers.addAll(m.getPlayers());
        for(int k=0;k<listOthers.size();k++){
            for(Object o: listOne){
                if(((Player)o).getColor()==((Player)listOthers.get(k)).getColor()){
                    listOthers.remove(k);
                }
            }
        }
        listOthers.remove(p);

        return listOthers;
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

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
    @Override
    public String toString() {
        return "Heatseeker";
    }
}
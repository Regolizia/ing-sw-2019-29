package adrenaline;

import java.sql.Struct;
import java.util.*;

public class WeaponCard extends Card{


    protected LinkedList<AmmoCube> price;
    private LinkedList<Object> targets;

    public WeaponCard() {
        price = new LinkedList<AmmoCube>();
    }


    // THEY ARE IN ORDER
                /*
                for(int i=0;i<effectsList.size();i++){
                proposeTargetsShoot(.....effectsList.get(i)...); -> view (SELECT THE APPROPRIATE NUMBER ONLY IF NECESSARY) // selectedTargets = (at least one)
                view -> list of targets for this effect
                shoot(....effectsList.get(i)...) (APPLIED DAMAGE)
                list of targets selected maybe it's removed from possible new targets
                }

                */

    // TO BE OVERRIDDEN
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, AmmoCube.Effect e){
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        return list;
    }

    // TO BE OVERRIDDEN
    public void weaponShoot(CoordinatesWithRoom c, Player p, LinkedList<AmmoCube.Effect> effectsList, GameModel m){
        for(int i=0;i<effectsList.size();i++){
            proposeTargets(c,m.mapUsed.getGameBoard(),p,m,effectsList.get(i));
            ///send proposedTargets to view, get back linkedList<Objects>
            applyDamage(targets,p,effectsList.get(i));
        }
    }



    // PROPOSE TARGETS (FROM HERE OR FROM DISTANCE 1 if adrenaline)
    public LinkedList<Object> proposeTargets(CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, AmmoCube.Effect e) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<CoordinatesWithRoom>(getPossibleTargetCells(c,e)); // CELLS IN WEAPON RANGE, DEPENDING ON THE WEAPON
        LinkedList<Object> targetList = new LinkedList<>();
        LinkedList<CoordinatesWithRoom> listTemp = new LinkedList<>(); // DISTANCE 1 FROM MY CELL

        // IF ADRENALINE SHOOT IS POSSIBLE, CAN MOVE ONCE
        if(p.checkDamage()==2){
            listTemp.addAll(c.XTilesDistant(g,1));

            for(int i=0;i<listTemp.size();i++) {
                list.addAll(getPossibleTargetCells(listTemp.get(i),e));
            }
        }
        // FROM CELLS IN WEAPON RANGE GET ALL THE POSSIBLE TARGETS
        for(int j=0;j<list.size();j++) {
            for(int k=0;k<m.getPlayers().size();k++) {
                if(m.getPlayers().get(k).getPlayerRoom()==list.get(j).getRoom() && m.getPlayers().get(k).getPlayerPositionX()==list.get(j).getX() && m.getPlayers().get(k).getPlayerPositionY()==list.get(j).getY()) {
                    targetList.add(m.getPlayers().get(k));
                }
            }
        }
        // TODO ADD POSSIBLE SPAWNPOINTS TO TARGETLIST
        return targetList;
    }

    // TO BE OVERRIDDEN
    public void applyDamage(LinkedList<Object> targetList, Player p, AmmoCube.Effect e){
    }

    public LinkedList<Object> getTargets(){
        return targets;
    }
    public LinkedList<AmmoCube> getPrice(){
        return price;
    }

}

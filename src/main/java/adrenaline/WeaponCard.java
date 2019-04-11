package adrenaline;

import java.sql.Struct;
import java.util.*;

public class WeaponCard extends Card{


    protected LinkedList<AmmoCube> price;
    private EffectAndNumber effectAndNumber;
    //private LinkedList<Object> targets;

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

    // CELLS IN WEAPON RANGE
    // TO BE OVERRIDDEN
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, AmmoCube.Effect e, GameBoard g){

        // CELLS OF EVERY ROOM I SEE
        // MAYBE OVERRIDDEN
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        int x = c.getRoom().getRoomSizeX();
        int y = c.getRoom().getRoomSizeY();

        for(int i=1;i<=x;i++){
            for(int j=1;j<=y;j++){
                list.add(new CoordinatesWithRoom(i,j,c.getRoom()));
            }
        }
        for(int k=0;k<g.getDoors().size();k++){
           if(c.getX()==g.getDoors().get(k).getCoordinates1().getX()&&
              c.getY()==g.getDoors().get(k).getCoordinates1().getY()&&
              c.getRoom().getToken()==g.getDoors().get(k).getRoom1().getToken()){

               for(int i=1;i<=x;i++){
                   for(int j=1;j<=y;j++){
                       list.add(new CoordinatesWithRoom(i,j,g.getDoors().get(k).getRoom2()));
                   }
               }
           }

            if(c.getX()==g.getDoors().get(k).getCoordinates2().getX()&&
              c.getY()==g.getDoors().get(k).getCoordinates2().getY()&&
              c.getRoom().getToken()==g.getDoors().get(k).getRoom2().getToken()){

                for(int i=1;i<=x;i++){
                    for(int j=1;j<=y;j++){
                        list.add(new CoordinatesWithRoom(i,j,g.getDoors().get(k).getRoom1()));
                    }
                }

            }

        }

        return list;
    }


    // GETS EVERYBODY IN WEAPON RANGE
    // PROPOSE TARGETS (FROM HERE OR FROM DISTANCE 1 if adrenaline)
    public LinkedList<Object> proposeTargets(CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, AmmoCube.Effect e) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<CoordinatesWithRoom>(getPossibleTargetCells(c,e,m.getMapUsed().getGameBoard())); // CELLS IN WEAPON RANGE, DEPENDING ON THE WEAPON
        LinkedList<Object> targetList = new LinkedList<>();
        LinkedList<CoordinatesWithRoom> listTemp = new LinkedList<>(); // DISTANCE 1 FROM MY CELL

        // IF ADRENALINE SHOOT IS POSSIBLE, CAN MOVE ONCE
        if(p.checkDamage()==2){
            listTemp.addAll(c.XTilesDistant(g,1));

            for(int i=0;i<listTemp.size();i++) {
                list.addAll(getPossibleTargetCells(listTemp.get(i),e,g));
                // TODO, WE HAVE TO CHECK IF PLAYER MOVED, AND THEN MOVE IT
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



    //THIS METHOD WILL HAVE ALL THE THINGS SINGLE CARDS NEED TO DO THEIR STUFF
    // TO BE OVERRIDDEN
    public void weaponShoot(LinkedList<Object> targets, CoordinatesWithRoom c, Player p, LinkedList<EffectAndNumber> effectsList, GameModel m){
        for(int i=0;i<effectsList.size();i++){
            //FOR EVERY EFFECT IT DAMAGES THE CORRESPONDING TARGETS
            applyDamage(targets,p,effectsList.get(i));
            effectsList.removeFirst();
        }
    }



    // TO BE OVERRIDDEN
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e){
    }

  /*  public LinkedList<Object> getTargets(){
        return targets;
    }*/
    public LinkedList<AmmoCube> getPrice(){
        return price;
    }

}

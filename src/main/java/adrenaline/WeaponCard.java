package adrenaline;

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
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g){

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
              c.getRoom().getToken()==g.getDoors().get(k).getCoordinates1().getRoom().getToken()){

               for(int i=1;i<=x;i++){
                   for(int j=1;j<=y;j++){
                       list.add(new CoordinatesWithRoom(i,j,g.getDoors().get(k).getCoordinates2().getRoom()));
                   }
               }
           }

            if(c.getX()==g.getDoors().get(k).getCoordinates2().getX()&&
              c.getY()==g.getDoors().get(k).getCoordinates2().getY()&&
              c.getRoom().getToken()==g.getDoors().get(k).getCoordinates2().getRoom().getToken()){

                for(int i=1;i<=x;i++){
                    for(int j=1;j<=y;j++){
                        list.add(new CoordinatesWithRoom(i,j,g.getDoors().get(k).getCoordinates1().getRoom()));
                    }
                }

            }

        }

        return list;
    }


    // GETS EVERYBODY IN WEAPON RANGE (DEPENDING ON THE WEAPON getPossibleTargetCells)
    // IT JUST TRANSFORMS CELLS INTO PLAYERS
    public LinkedList<Object> fromCellsToTargets(LinkedList<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        LinkedList<Object> targetList = new LinkedList<>();

        // FROM CELLS IN WEAPON RANGE GET ALL THE POSSIBLE TARGETS
           for(int k=0;k<m.getPlayers().size();k++) {
               for (CoordinatesWithRoom coordinatesWithRoom : list) {
                   if (m.getPlayers().get(k).getPlayerRoom() == coordinatesWithRoom.getRoom() &&
                           m.getPlayers().get(k).getPlayerPositionX() == coordinatesWithRoom.getX() &&
                           m.getPlayers().get(k).getPlayerPositionY() == coordinatesWithRoom.getY()) {
                       targetList.add(m.getPlayers().get(k));
                       break;
                   }
               }
        }
        // TODO ADD POSSIBLE SPAWNPOINTS TO TARGETLIST

        targetList.remove(p);

        return targetList;
    }



    //THIS METHOD WILL HAVE ALL THE THINGS SINGLE CARDS NEED TO DO THEIR STUFF
    // TO BE OVERRIDDEN
    public void weaponShoot(LinkedList<Object> targets, CoordinatesWithRoom c, Player p, LinkedList<EffectAndNumber> effectsList, GameModel m){
        for(int i=0;i<effectsList.size();i++){
            //FOR EVERY EFFECT IT DAMAGES THE CORRESPONDING TARGETS
            applyDamage(targets,p,effectsList.get(i));
            for(int j = 1; j<=effectsList.get(i).getNumber(); j++) {
                effectsList.removeFirst();
            }

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

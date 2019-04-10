package adrenaline;

import java.util.LinkedList;

public class Action {

//WeaponCard weapon;

    public static enum ActionType {
        GRAB, RUN, SHOOT, ADRENALINESHOOT;
    }

    private ActionType actionSelected;


    public Action(ActionType chosen, Player player, CoordinatesWithRoom c, GameBoard g, GameModel m){
        actionSelected = chosen;
        switch (actionSelected){
            case RUN:
                // PROPOSE WHERE TO GO, SELECT ONE (with proposeCellsRun method)
                // selectedCell =
                // run(player, selectedCell);
                break;

            case GRAB:
                // PROPOSE CELL WHERE TO GRAB (EVERY CELL HAS SOMETHING) (DISTANCE 0-1 OR 0-1-2) (with proposeCellsGrab)
                // selectedCell =
                // grab()
                break;

            case SHOOT:
                // selectedWeapon = getSelectedWeapon (THAT IS CHARGED, isLoaded method in Player)
                // PROPOSE TARGETS, SELECT THE APPROPRIATE NUMBER IF NECESSARY
                // selectedTargets = (at least one)
                // shoot()
                break;
            case ADRENALINESHOOT:
                break;

            default:
                //INVALID CHOICE

        }
    }

    // PROPOSE CELL WHERE TO GO (DISTANCE 1-2-3)
    public LinkedList<CoordinatesWithRoom> proposeCellsRun(CoordinatesWithRoom c, GameBoard g){
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.XTilesDistant(g,1));
        list.addAll(c.XTilesDistant(g,2));
        list.addAll(c.XTilesDistant(g,3));
        return list;
    }

    // RUN
    public void run(Player p,CoordinatesWithRoom c){
        p.setPlayerPosition(c.getX(),c.getY());
    }

    // PROPOSE CELLS WHERE TO GRAB (DISTANCE 0-1 OR 0-1-2 IF ADRENALINE)
    public LinkedList<CoordinatesWithRoom> proposeCellsGrab(CoordinatesWithRoom c, GameBoard g, Player p){
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.XTilesDistant(g,1));
        list.add(c);

        // IF ADRENALINE GRAB IS POSSIBLE
        if(p.checkDamage()==1){
            list.addAll(c.XTilesDistant(g,2));
        }
        return list;
    }

    // GRAB
    public void grab(Player p, CoordinatesWithRoom c, GameBoard g){
        // IF THERE IS A SPAWNPOINT HERE
        //CHOOSE WEAPON IF CANGRAB IT
        //IF THERE IS AMMOTILE
        //ADD STUFF IF CAN HAVE IT
    }

    // PROPOSE TARGETS (FROM HERE OR FROM DISTANCE 1)
    public LinkedList<Object> proposeTargetsShoot(CoordinatesWithRoom c, GameBoard g, Player p, WeaponCard w, GameModel m) {
    LinkedList<CoordinatesWithRoom> list = new LinkedList<CoordinatesWithRoom>(w.getPossibleTargetCells(c)); // CELLS IN WEAPON RANGE
    LinkedList<Object> targetList = new LinkedList<>();
    LinkedList<CoordinatesWithRoom> listTemp = new LinkedList<>(); // DISTANCE 1 FROM MY CELL

        // IF ADRENALINE SHOOT IS POSSIBLE, CAN MOVE ONCE
        if(p.checkDamage()==2){
            listTemp.addAll(c.XTilesDistant(g,1));

            for(int i=0;i<listTemp.size();i++) {
                list.addAll(w.getPossibleTargetCells(listTemp.get(i)));
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

    // SHOOT




/*

    public void shoot(Player player, WeaponCard weapon)
    {
        if (player.checkDamage()==2)
        {shootAdrenaline(player, weapon);
            return;}
*/

        // /*/ref options list*/ checkPayment(Player player, WeaponCard weapon);  checkPayment is in WeaponCard
        // /*/ref possible target list*/ canAim(Player player, // ref options list );
        // /*/ref target list/ aimTarget(ref possible target);
        // check if target=spawnpoint or target=player
        //damage(ref target list, WeaponCard weapon, ref options list) //todo check on target in damage
    //}

    //public void shootAdrenaline(Player player, WeaponCard weapon)
    //{
        // /*/ref options list*/ checkPayment(Player player, WeaponCard weapon);  checkPayment is in WeaponCard
        // /*/ref possible target list*/ canAim(Player player, // ref options list );
        // /*/ref target list/ aimTarget(ref possible target);
        // check if target=spawnpoint or target=player
        //shoot+adrenaline option
        //damage(ref target list, WeaponCard weapon, ref options list) //todo check on target in damage

   // }

// GRAB

// GRAB ADRENALINE






/*    public void reload(Player player)
    {
        *//*weapon=*//*player.getWeaponCard(player);

    }*/
    /*todo frenzyShoot frenzyRun frenzyGrab*/


    public ActionType getActionSelected() {
        return actionSelected;
    }
    public void setActionSelected(ActionType a){
        this.actionSelected = a;
    }



}


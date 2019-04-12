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
                // HERE JUST WEAPON AND PAYMENTS, EVERYTHING ELSE IN CORRECT WEAPONCARD
                // selectedWeapon = getSelectedWeapon (THAT IS CHARGED, isLoaded method in Player)
                // choose EFFECTS AND ACCEPT PAYMENT FOR THEM

                // LIST OF EFFECTS CHOSEN (BASE ALREADY IN, IT HAS BEEN PAID (if they want ALT remove BASE))
                // EFFECTS ADDED TO EFFECTSLIST

                // /*/ref options list*/ checkPayment(Player player, WeaponCard weapon);  checkPayment is in WeaponCard
                // /*/ref possible target list*/ canAim(Player player, // ref options list );
                // /*/ref target list/ aimTarget(ref possible target);
                break;

            default:
                //INVALID CHOICE

        }
    }
////////////////////////////////////////////////////
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

////////////////////////////////////////////////////
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

////////////////////////////////////////////////////
    // SHOOT
    public void shoot(WeaponCard w, CoordinatesWithRoom c, Player p, LinkedList<EffectAndNumber> effectsList, GameModel m){

        // JUST TO MAKE IT COMPILE, TO BE REMOVED
        LinkedList<Object> targets = new LinkedList<>();

        //HA SWITCH CASE IN BASE A CHE ARMA, SE NORMALI(QUELLO CHE VEDO) CASE COMUNE
        //w.getPossibleTargetCells();

        //w.proposeTargets();
        //GET TARGETS and THEN ADD NUMBER OF TARGETS TO EFFECTSLIST FOR EVERY EFFECT PAID FOR
        //TODO CHECK THAT IT FOLLOWS THE RULES
        //FOR EXAMPLE IN MACHINEGUN IF I WANT BASE+OP1+OP2 TARGET OP1 MUST BE DIFFERENT FROM TARGET OP2 ELSE I DON'T ADD IT
        // WEAPONSHOOT ACTS ONLY ON THE TARGETS OF THE SELECTED EFFECT AND DOESN'T CHECK IN BETWEEN EFFECTS

        // TARGETS ORDER
        // LockRifle, MachineGun, Thor(fagli scegliere un solo target alla volta, se ha pagato per le op fagli scegliere per op1
        // un target tra quello che vede il target della base, per op2 un target tra quello che vede il target di op1)

        // EFFECTS ORDER
        // Thor

        // ONLY WEAPONS WITH OP1 OR OP2 NEED THE REMOVAL OF TARGETS AFTER DOING DAMAGE
        w.weaponShoot(targets, c, p, effectsList, m);
    }


    public boolean Reload(Player p, WeaponCard w)
    {   int blue = p.getAmmoBox()[0];
        int red = p.getAmmoBox()[1];
        int yellow = p.getAmmoBox()[2];

        for(int i=0;i<w.price.size();i++) {
            if(w.price.get(i).getEffect()== AmmoCube.Effect.BASE){
            if(w.price.get(i).getCubeColor()== AmmoCube.CubeColor.BLUE)
                blue--;
            if(w.price.get(i).getCubeColor()== AmmoCube.CubeColor.RED)
                red--;
            if(w.price.get(i).getCubeColor()== AmmoCube.CubeColor.YELLOW)
                yellow--;
            }
        }
        if(blue>=0 && red>=0 && yellow>=0){
            for(int i=0;i<w.price.size();i++) {
                if(w.price.get(i).getEffect()== AmmoCube.Effect.BASE){
                    w.price.get(i).setPaid(true);
                }
            }
            p.getAmmoBox()[0] = blue;
            p.getAmmoBox()[1] = red;
            p.getAmmoBox()[2] = yellow;
            return true;
        }
        else return false;
    }

    /*todo frenzyShoot frenzyRun frenzyGrab*/


    public ActionType getActionSelected() {
        return actionSelected;
    }
    public void setActionSelected(ActionType a){
        this.actionSelected = a;
    }

}

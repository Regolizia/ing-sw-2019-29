package adrenaline;



import com.sun.scenario.effect.impl.sw.java.JSWBlend_BLUEPeer;

import java.util.LinkedList;
import java.util.Scanner;

public class Action {
final int numMaxAlternativeOptions=1;
final int numMaxOptions=2;
int resp=0;
GameBoard g=new GameBoard();
//WeaponCard weapon;
    Scanner scan=new Scanner(System.in);

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


        /* WHEN SOMEBODY CHOOSES ADRENALINE SHOOT WE ASK WHERE TO MOVE AND THEN WE DO THE STAUFF TO SHOOT
        // IF ADRENALINE SHOOT IS POSSIBLE, CAN MOVE ONCE
        if(p.checkDamage()==2){
            listTemp.addAll(c.XTilesDistant(g,1));

            for(int i=0;i<listTemp.size();i++) {
                list.addAll(getPossibleTargetCells(listTemp.get(i),e,g));
                // TODO REMOVE DUPLICATES
                // TODO, WE HAVE TO CHECK IF PLAYER MOVED, AND THEN MOVE IT
            }
        }*/

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
        int indexEffect=0;
        // JUST TO MAKE IT COMPILE, TO BE REMOVED
        LinkedList<Object> targets = new LinkedList<>();


        //if true the player has paid the base/alternative effect and can shoot or buy optional effect
        if(checkBasePayment(w,p)==false)
            return; //if false the player can't shoot
        // todo paidOptionsMethod to know the paid options
        System.out.println("Do you want to add other options?\n digit\n 1:yes\n2:no");
        resp=scan.nextInt();


        if(resp==1){
        //    w.weaponShoot(targets,c,p,effectsList,m);
            return;}
        if(resp==0)
        {
            while(effectsList.get(indexEffect)!=null){

            LinkedList<CoordinatesWithRoom> targetsCell=p.chooseTargets(w,p,c,effectsList.get(indexEffect),g);
            //targets=w.getPossibleTargetCells(c,effectsList.get(0),g.getGameboard()); todo choose targets
            }
            return;}


        //HA SWITCH CASE IN BASE A CHE ARMA, SE NORMALI(QUELLO CHE VEDO) CASE COMUNE
        //w.getPossibleTargetCells();

        //w.fromCellsToTargets();
        //GET TARGETS and THEN ADD NUMBER OF TARGETS TO EFFECTSLIST FOR EVERY EFFECT PAID FOR
        //TODO CHECK THAT IT FOLLOWS THE RULES
        //FOR EXAMPLE IN MACHINEGUN IF I WANT BASE+OP1+OP2 TARGET OP1 MUST BE DIFFERENT FROM TARGET OP2 ELSE I DON'T ADD IT
        // WEAPONSHOOT ACTS ONLY ON THE TARGETS OF THE SELECTED EFFECT AND DOESN'T CHECK IN BETWEEN EFFECTS

        // TARGETS ORDER
        // LockRifle
        // MachineGun
        // Thor(fagli scegliere un solo target alla volta, se ha pagato per le op fagli scegliere per op1
        // un target tra quello che vede il target della base, per op2 un target tra quello che vede il target di op1)
        // Tractor beam
        // VortexCannon(fagli scegliere tra le caselle che vede una chiamata vortex, prendi i giocatori nel vortex e 1 distanti da lì
        // e fagli scegliere il target) lo danneggi e sposti, uguale per op1
        // Furnace offri tutte le caselle delle stanze, prendi una sola stanza(cella, parametro di proposeTragets)

        // EFFECTS ORDER
        // Thor



      //  // ONLY WEAPONS WITH OP1 OR OP2 NEED THE REMOVAL OF TARGETS AFTER DOING DAMAGE
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

    public boolean checkBasePayment(WeaponCard w,Player p){
       boolean response=false;

       int indexList=0;
       boolean okChoosen=false;
       System.out.println("Do you want base option: digit 1\n for alternative option digit 0\n 2 to cancel shoot action");
        resp=scan.nextInt();
        if(resp==1)
            indexList=0;
        if(resp==2)
            return false;
        if(resp==0)
            {  while(indexList<=numMaxAlternativeOptions){
                System.out.println("Do you want this alternative?\ndigit\n1:yes\n2:no\n3:exit");//todo call to weapon to get alternative name + effect
                resp=scan.nextInt();
                if(resp==3||resp==1)
                {if(resp==1)
                    okChoosen=true;
                break;}
                if(resp==2) indexList ++;
            }
            if (okChoosen==false) return false;}


        switch(w.price.get(indexList).getCubeColor()){
            case BLUE: if(p.getCubeBlue(p)>=1) {
                p.setBlueCube(p,p.getCubeBlue(p)-1);
                return true;
            }
                else return false;

            case RED:if(p.getCubeRed(p)>=1) {
                p.setRedCube(p,p.getCubeRed(p)-1);
                return true;
            }
                else return false;
            case YELLOW:if(p.getCubeYellow(p)>=1) {
                p.setYellowCube(p,p.getCubeYellow(p)-1);
                return true;
            } else return false;}
        return false;
    }
    public ActionType getActionSelected() {
        return actionSelected;
    }
    public void setActionSelected(ActionType a){
        this.actionSelected = a;
    }

}

package adrenaline;


import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

import static adrenaline.AmmoCube.CubeColor.*;

/**
 * Is the class that describes players' turn.
 * @author Giulia Valcamonica
 * @version 1.0
 **/

public class Action {
    final private int numMaxAlternativeOptions = 2; //(0=base 1=alternative)
    final private int numMaxAmmoToPay = 3;//(0:first price 1:second price 2:second price)
    final private int numMaxWeaponYouCanHave=3;

    private int numOfRemainingPlayer;
   private boolean executedFirstAction;
    private  boolean executedSecondAction;
    private  boolean endTurn;
    private  boolean deletedAction;
    private int notDeleted;
    private int initialPlayerIndex;

    public static enum ActionType {
        GRAB, RUN, SHOOT, RELOAD;
    }

    public static enum PayOption {
        AMMO,AMMOPOWER,NONE;
    }


    /**
     * Class constructor.
     *  * @version 1.0
     */

    public Action(GameModel m) {
       this.executedFirstAction=false;
       this.executedSecondAction=false;

       setEndTurn(false);
       this. deletedAction=false;
        this.notDeleted=0;
        this.numOfRemainingPlayer=m.getPlayers().size();
       this.initialPlayerIndex=000;
    }

    /**
     * doAction : to do the action choosen by the player
     * can be repeat two times per turn
     * on the third time doAction accepts only reload
     * switch on @param actionSelected to get the right method to be called
     * @param  actionSelected : choosen action
     * @param  player: name of the player who wants to do the action
     * @param c: player's position on the board
     * @param g: gameboard choosen for the game
     * @param m: model choosen for the game
     * @param option: payment option choosen to buy weapons/reloads/...
     * @param frenzyMode: to know if frenzy mode is enabled
     */
    //________________________________DO ACTION_____________________________________________________________________//
    public void doAction(ActionType actionSelected, Player player, CoordinatesWithRoom c, GameBoard g, GameModel m, PayOption option, GameModel.FrenzyMode frenzyMode){
      //  actionSelected = chosen;
        if(!getEndturn()&&!endOfTheGame(g)) {
            switch (actionSelected) {
                case RUN:
                    doRun(c,g,player);
                    break;
                case GRAB:
                    doGrab(player,c,g,option,m);
                    break;
                case SHOOT:
                    doShoot(player,option,m,g,c);
                    break;

                default:
                    //INVALID CHOICE
            }

            if(executedFirstAction&&!deletedAction&&!executedSecondAction)this.executedSecondAction=true;
            else if(!executedFirstAction&&!deletedAction&&!executedSecondAction) {this.executedFirstAction=true;this.executedSecondAction=false;}
            if(executedFirstAction&&executedSecondAction)setEndTurn(true);
        }
        //HERE ENDS TURN
        doEndTurn(actionSelected,player,option,g,m,frenzyMode,c);
    }
    /**
     * doRun : to do run methods
     * @param  player: name of the player who wants to move
     * @param c: player's position on the board
     * @param g: gameboard choosen for the game
     * this action can't be deleted
     */
public void doRun(CoordinatesWithRoom c,GameBoard g,Player player)
{
    CoordinatesWithRoom coordinatesR=chooseCell(proposeCellsRun(c,g));
    run(player, coordinatesR);
    deletedAction=false;
}
    /**
     * doGrab : to do grab methods
     * @param  player: name of the player who wants to grab
     * @param c: player's position on the board
     * @param g: gameboard choosen for the game
     * @param option: payment option choosen to grab objects on the board
     * this action can be deleted if grab method returns false
     */
    public void doGrab(Player player,CoordinatesWithRoom c,GameBoard g,PayOption option,GameModel m){
        // PROPOSE CELL WHERE TO GRAB (EVERY CELL HAS SOMETHING) (DISTANCE 0-1 OR 0-1-2) (with proposeCellsGrab)
        CoordinatesWithRoom coordinatesG;
        if(player.checkDamage()==1||player.checkDamage()==2)
            coordinatesG=chooseCell(proposeCellsGrabAdrenaline(c, g, player));
        else coordinatesG=chooseCell(proposeCellsGrab(c,g,player));
        if(!(grab(player, coordinatesG, g,option,m)))
            deletedAction=true;
        else player.setPlayerPosition(coordinatesG.getX(),coordinatesG.getY(),coordinatesG.getRoom());
    }
    /**
     * doShoot : to do shoot methods
     * @param  player: name of the player who wants to shoot
     * @param c: player's position on the board
     * @param g: gameboard choosen for the game
     * @param m: choosen GameModel
     * @param g: choosen Gameboard
     * @param option: payment option choosen to pay weapon effects
     *
     * this action can be deleted if
     *              i) weapon is not reload and can't be reload
     *              ii)the player doesn't have selected any base/alternative effect
     */
    public void doShoot(Player player,PayOption option,GameModel m,GameBoard g,CoordinatesWithRoom c){
        // WeaponList player.checkWeapon() // GIVES ALL THE WEAPON OWNED BY THE PLAYER
        LinkedList<WeaponCard> hand = player.getHand();
        // WeaponCard chooseWeaponCard()// GIVES THE SELECTED WEAPON
        WeaponCard weapon = chooseWeaponCard(hand);

        if (!weapon.getReload()&&!canPayCard(weapon, player,option)) {
                deletedAction = true;
                return;
        }

        if(canPayCard(weapon,player,option)){
            List<EffectAndNumber> payEff = paidEffect(weapon, player,option);
            if(payEff==null)
            {deletedAction=true;
            return;}
            CoordinatesWithRoom cChoosen;
            if(player.checkDamage()==2)
                cChoosen=chooseCell(proposeCellsRunBeforeShootAdrenaline(c,g));
            else cChoosen=chooseCell(proposeCellsRunBeforeShoot(c,g));
            player.setPlayerPosition(cChoosen.getX(),cChoosen.getY(),cChoosen.getRoom());
            shoot(weapon, cChoosen, player, payEff, m,g);
            weapon.setNotReload();// i've lost base effect payment
            deletedAction=false;}
    }
    /**
     * doEndTurn : methods to end the turn/game
     * @param  player: name of the player who has ended his turn
     * @param actionSelected:action selected by the player
     * @param c: player's position on the board
     * @param g: gameboard choosen for the game
     * @param m: choosen GameModel
     * @param g: choosen Gameboard
     * @param frenzyMode: to know if frenzy mode is enable
     * @param option: payment option choosen to pay weapon effects
     *
     * this action can't be deleted
     *              if frenzyMode is off the only action accepeted is reload otherwise it doesn't considered the selected action
     *              if frenzyMode is on player get plus two actions, those depend on players' order
     * CONVENTIONS:
     *      i)if the player selected is the one who starts frenetic Mode his actions would be the same actions of the player before him
     *      ii)if the player selected is the one who starts the frenetic Mode and also he is the first player of the game all players' (even our player's)actions
     *              would be the same action of the player after him
     *
     */
    //todo better frenetic action
    public void doEndTurn(Action.ActionType actionSelected, Player player, PayOption option, GameBoard g, GameModel m, GameModel.FrenzyMode frenzyMode,CoordinatesWithRoom c){
        if(frenzyMode== GameModel.FrenzyMode.ON&&notDeleted<2&&endOfTheGame(g)) {
            if (initialPlayerIndex == 000)
                initialPlayerIndex = m.getPlayers().indexOf(player);
            FreneticAction fAction = new FreneticAction(m);
            //start by the player in the turn

            if (m.getPlayers().get(initialPlayerIndex) == ((LinkedList<Player>) m.getPlayers()).getFirst() && notDeleted < 2) {
                if (fAction.selectFrenzyAction(actionSelected, player, c, g, m, option, FreneticAction.PlayerOrder.AFTER))
                    notDeleted++;
            } else {
                if (initialPlayerIndex == m.getPlayers().indexOf(player) || m.getPlayers().indexOf(player) > initialPlayerIndex && notDeleted < 2) {
                    if (fAction.selectFrenzyAction(actionSelected, player, c, g, m, option, FreneticAction.PlayerOrder.FIRST))
                        notDeleted++;

                }
                if (m.getPlayers().indexOf(player) == 0 || m.getPlayers().indexOf(player) < initialPlayerIndex && notDeleted < 2) {
                    if (fAction.selectFrenzyAction(actionSelected, player, c, g, m, option, FreneticAction.PlayerOrder.AFTER))
                        notDeleted++;


                }


            }
        }

        else {

            if(actionSelected.equals(ActionType.RELOAD)){
                LinkedList<WeaponCard> weaponList = player.getHand();
                WeaponCard weaponToReload = chooseWeaponCard(weaponList);
                if (!weaponToReload.getReload())
                    reload(player, weaponToReload,option); }
            List<Player> players=m.getPlayers();
            LinkedList<Player>victims=new LinkedList<>();

            for(int index=0;index< players.size();index++)
            { if(players.get(index).isDead()){
                g.pickASkull();
                victims.add(players.get(index));
            }
                canGetPoints(victims,players);
                for(int indexVictims=0;indexVictims<victims.size();indexVictims++)
                    victims.get(index).newLife();}
            //ending no frenetic
        }
        /////
        if(endOfTheGame(g)&&notDeleted>=2&&getRemaingPlayer(numOfRemainingPlayer)<=0){
        //here we end frenetic game
        //points count
        }
    }
    /**
     * chooseCell: a method to select the desired cell to do an action
     * @param coordinates: a LinkedList of selectable cells
     * this action can't be deleted
     *                   only one can be selected
     */
    //______________________________CHOOSE CELL______________________________________________________________________//
    public CoordinatesWithRoom chooseCell(LinkedList<CoordinatesWithRoom>coordinates){
        CoordinatesWithRoom choosenCell=new CoordinatesWithRoom();
        for(int i=0;i<coordinates.size();i++)
        {
            //here select the one you choose and then break
        }
        return choosenCell;
    }

    /**
     * proposeCellsRun
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to run
     * @param c: player position
     * @param g: GameBoard choosen
     * this action can't be deleted
    CONV:
     *      i)this is the not Frenzy option method
     *      ii) move at least by one cell
     *      iii) move max by three cell
     */
    //___________________________ PROPOSE CELL WHERE TO GO (DISTANCE 1-2-3)___________________________________________//
    public LinkedList<CoordinatesWithRoom> proposeCellsRun(CoordinatesWithRoom c, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(g, 1));
        list.addAll(c.xTilesDistant(g, 2));
        list.addAll(c.xTilesDistant(g, 3));
        return list;
    }
    /**
     * proposeCellsRunBeforeShoot
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to run before shooting
     * @param c: player position
     * @param g: GameBoard choosen
     * this action can't be deleted
     * CONV:
     *      i)this is the not Frenzy option method
     *      ii) you can move only by one cell
     */
    //__________________________PROPOSE CELL TO MOVE BEFORE SHOOT NOT FRENZY_____________________________________________//

    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShoot(CoordinatesWithRoom c,GameBoard g){
        LinkedList<CoordinatesWithRoom>list=new LinkedList<>(c.xTilesDistant(g,1));
        list.add(c);
    return list;}

    /**
     * proposeCellsRunBeforeShootAdrenaline
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to run before shooting
     * @param c: player position
     * @param g: GameBoard choosen
     * this action can't be deleted
     * CONV:
     *      i)this is the not Frenzy option method
     *      ii) you can move by all the cells in proposeCellsRunBeforeShoot
     *      iii) you can move max by two cells
     */

    //_________________________PROPOSE CELL TO MOVE BEFORE SHOOT ADRENALINE_______________________________________________//
    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShootAdrenaline(CoordinatesWithRoom c,GameBoard g){
        LinkedList<CoordinatesWithRoom>list=new LinkedList<>(c.xTilesDistant(g,1));
        list.addAll(c.xTilesDistant(g,2));
        list.add(c);
        return list;
    }
    /**
     * run: to do effective run action
     * @param c: player position
     * @param p: running player
     * this action can't be deleted
     */
    //_______________________________________________RUN______________________________________________________________//
    public void run(Player p, CoordinatesWithRoom c) {

        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
    }
    /**
     * proposeCellsGrab
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to Grab
     * @param c: player position
     * @param g: GameBoard choosen
     * @param p: player who does the action
     * this action can't be deleted
     * CONV:
     *      i)this is the not Frenzy option method
     *      ii) you can grab on your position
     *      iii) you can grab max one cell distant (includes moving)
     */
    //_______________ PROPOSE CELLS WHERE TO GRAB (DISTANCE 0-1 OR 0-1-2 IF ADRENALINE)_______________________________//
    public LinkedList<CoordinatesWithRoom> proposeCellsGrab(CoordinatesWithRoom c, GameBoard g, Player p) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(g, 1));
        list.add(c);
        return list;
    }
    /**
     * proposeCellsGrabAdrenaline
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to Grab
     * @param c: player position
     * @param g: GameBoard choosen
     * @param p: player who does the action
     * this action can't be deleted
     * CONV:
     *      i)this is the not Frenzy option method
     *      ii) you can grab in the same cells of proposeCellsGrab
     *      iii) you can grab max two cells distant (includes moving)
     */
    //____________PROPOSE CELLS GRAB WITH ADRENALINE checkDamage=(1||2)
    public LinkedList<CoordinatesWithRoom> proposeCellsGrabAdrenaline(CoordinatesWithRoom c, GameBoard g, Player p) {
        LinkedList<CoordinatesWithRoom> list = proposeCellsGrab(c,g,p);
        list.addAll(c.xTilesDistant(g, 2));
        return list;
    }
    /**
     * proposeCellsGrabAdrenaline
     * check the selected cell where to grab and calls the right grabbing method
     * @return boolean : to know if the action is good ended
     * @param c: player position
     * @param g: GameBoard choosen
     * @param p: player who does the action
     * @param option: payment option to grab
     * this action can be deleted by returning false if player doesn't grab
     */
    //________________________________________________GRAB____________________________________________________________//
    public boolean grab(Player p, CoordinatesWithRoom c, GameBoard g,PayOption option,GameModel m) {
        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
        // IF THERE IS A SPAWNPOINT HERE
        LinkedList<Spawnpoint> spawnpoints=c.getRoom().getSpawnpoints();

     for (int i=0;i<spawnpoints.size();i++){
       if( c.getRoom()==p.getPlayerRoom()&&c.getX()==p.getPlayerPositionX()&&c.getY()==p.getPlayerPositionY()
            && c.getRoom().getSpawnpoints()!=null && c.getRoom().getSpawnpoints().get(i).getSpawnpointX()==c.getX()&&
       c.getRoom().getSpawnpoints().get(i).getSpawnpointY()== c.getY())
       {
        //CHOOSE WEAPON IF CANGRAB IT
        return   grabCard(p,p.getHand(),c,option,m);}}

        return grabTile(p,c,m);

    }
    /**
     * grabCard
     * this is the method to grab a weapon card
     *  @return boolean : to know if the action is good ended
     *@param c: player position
     * @param player: player who does the action
     * @param option: payment option to grab
     * this action can be deleted if the player can't grab the weapon
     *              CONV:
     *              i)if you can grab the card you can't delete the action
     *              ii) if you buy a weapon card you have already paid its BASE effect
     *                  so you can't use its ALT effect (if you can pay BASE)
     *              iii) if you can't pay the BASE effect check if you can pay ALT effect
     *                      in that case tou have ALT effect and you can't use BASE effect
     */
    //____________________________________________GRAB OPTIONS(WEAPON)________________________________________________________//

    public boolean grabCard(Player player,LinkedList<WeaponCard> hand, CoordinatesWithRoom c,PayOption option,GameModel m){
        int index;
        //TODO a way to convert propose to grab cells in weaponCard/Card
        LinkedList <WeaponCard> canBeGrabbedWeapon=new LinkedList<>();
        WeaponCard w;
        for (index = 0; index <canBeGrabbedWeapon.size() ; index++) {
            //WHEN A WEAPON IS CHOOSEN BREAK
        }
        if(!canPayCard(canBeGrabbedWeapon.get(index),player,option))
            return false;
        //todo a can pay base effect
        if(canPayCard(canBeGrabbedWeapon.get(index),player,option)&&hand.size()>=numMaxWeaponYouCanHave)
        dropWeaponCard(hand); //I need to drop that card
        hand.add(canBeGrabbedWeapon.get(index));
        m.populateMap(m.getMapUsed());//todo SEE IF IT DOES WORK HERE
        canBeGrabbedWeapon.get(index).setReload(); //when i grab a weapon i've already paid its base effect
        return true;
    }
    /**
     * dropWeaponCard
     * this method is called when a player wants to grab a weapon card but already has three of them, so
     * he needs to drop one
     * this action can't be deleted
     */
    public void dropWeaponCard(LinkedList<WeaponCard> hand){
        int index;
        for (index = 0; index < hand.size(); index++) {
            //WHEN A WEAPON IS CHOOSEN BREAK
        }
        hand.remove(index);
    }
    /**
     * grabTile
     * this is the method to grab an ammo tile
     * @return boolean : to know if the action is good ended
     * @param c: player position
     * @param player: player who does the action
     *
     * this action can be deleted if the player doesn't grab anything
     *
     */
    //_____________________________________GRAB OPTION TILE ___________________________________________________________//
    public boolean grabTile(Player player, CoordinatesWithRoom c,GameModel m){
        AmmoTile toBeGrabbedTile=new AmmoTile(RED,RED,RED);
        // grab ammo or powerUp

        //TODO a way to convert propose to grab cells in AmmoTile
      //  PowerUpCard toBeGrabbedPowerUp=null;
        for(int index = 0; index<toBeGrabbedTile.getAmmoCubes().size(); index++)
        {
            if(toBeGrabbedTile.getAmmoCubes().get(index).getCubeColor().equals(POWERUP))
                return (grabPowerUp(player,c,m)&&grabCube(player,c,toBeGrabbedTile));

        }
                    return grabCube(player,c, toBeGrabbedTile);
    }

    /**
     * grabCube
     * this is the method to grab a color cube
     * @return boolean : to know if the action is good ended
     * @param c: player position
     * @param player: player who does the action
     * @param a: ammo tile to be grabbed
     *
     *              CONV: if the player has already three  cube of one color, he can't pick up more cube of that color
     */


    //____________________________ADDING CUBE______________________________________________________________________//
    public boolean grabCube(Player player, CoordinatesWithRoom c, AmmoTile a){

        for(int i=0;i<3;i++)
        {
            switch (a.getAmmoCubes().get(i).getCubeColor())

                {
                    case YELLOW:
                        player.setCube(0,0,1);break;
                    case BLUE:
                        player.setCube(0,1,0);break;

                    case RED:
                        player.setCube(1,0,0);break;
                default:player.setCube(0,0,0);
                }
        }


        return true;
    }
    /**
     * grabPowerUp
     * this is the method to grab a power up card
     * it is called only when an ammo tile has field POWER
     *  @return boolean : to know if the action is good ended
     * @param c: player position  //todo delete position player
     * @param p: player who does the action
     * this action can be deleted if the player can't grab the power up
     *              CONV:
     *         i)     if you can grab the card you can't delete the action
     *         ii)    if you have already three powerups you can't pickup this ammotile
     */
public boolean grabPowerUp(Player p, CoordinatesWithRoom c, GameModel m){
        if(!p.canGrabPowerUp()) return false;
                p.getPowerUp().add(m.powerUpDeck.pickPowerUp());

        return true;

}
    /**
     * shoot
     * this is the method to shoot
     *@param c: player position
     * @param p: player who does the action
     * @param w: weapon card choosen to shoot
     * @param effectsList: shooting effect paid by the player
     * @param g :gameboard
     * @param m :model
     *      * this action can't
     *          CONV: you can shoot even at empty cells
     */

    //______________________________________SHOOT_____________________________________________________________________//
    public void shoot(WeaponCard w, CoordinatesWithRoom c, Player p, List<EffectAndNumber> effectsList, GameModel m,GameBoard g) {
        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
        EffectAndNumber effectNumber=new EffectAndNumber(AmmoCube.Effect.BASE,0);
        for(int index=0;index<effectsList.size();index++) {
            ///todo choose effect order
            switch (effectsList.get(index).getEffect()) {
                case BASE: effectNumber = new EffectAndNumber(AmmoCube.Effect.BASE, 0);
                case ALT: effectNumber = new EffectAndNumber(AmmoCube.Effect.ALT, 0);
                case OP1: effectNumber = new EffectAndNumber(AmmoCube.Effect.OP1, 0);
                case OP2: effectNumber = new EffectAndNumber(AmmoCube.Effect.OP2, 0);
            }
            List<CoordinatesWithRoom> target= w.getPossibleTargetCells(c, effectNumber, g);
           // w.fromCellsToTargets(target,c,g,p,m,effectNumber);
            //here controller gives back choosen opponents
            List<Object>effectiveTarget;
            effectNumber.setNumber(3);//just to remove bug
                effectiveTarget=chooseTargets(w.fromCellsToTargets(target,c,g,p,m,effectNumber),effectNumber.getNumber());
           w.weaponShoot(effectiveTarget,c,p,effectsList,m);
        }
        //HA SWITCH CASE IN BASE A CHE ARMA, SE NORMALI(QUELLO CHE VEDO) CASE COMUNE
        //w.getPossibleTargetCells();

        //w.fromCellsToTargets();
        //GET TARGETS and THEN ADD NUMBER OF TARGETS TO EFFECTSLIST FOR EVERY EFFECT PAID FOR
        //TODO CHECK THAT IT FOLLOWS THE RULES
        //FOR EXAMPLE IN MACHINEGUN IF I WANT BASE+OP1+OP2 TARGET OP1 MUST BE DIFFERENT FROM TARGET OP2 ELSE I DON'T ADD IT
        // WEAPONSHOOT ACTS ONLY ON THE TARGETS OF THE SELECTED EFFECT AND DOESN'T CHECK IN BETWEEN EFFECTS


                //è in weaponCard
        // TARGETS ORDER
        // LockRifle
        // MachineGun, CHECK TARGET OP1 IS ONE OF THE TARGETS OF BASE, CHECK TARGET OP2 IS DIFFERENT FROM TARGET OP1
        // Thor(fagli scegliere un solo target alla volta, se ha pagato per le op fagli scegliere per op1
        // un target tra quello che vede il target della base, per op2 un target tra quello che vede il target di op1)
        // Tractor beam
        // VortexCannon(fagli scegliere tra le caselle che vede una chiamata vortex, prendi i giocatori nel vortex e 1 distanti da lì
        // e fagli scegliere il target) lo danneggi e sposti, uguale per op1
        // Furnace offri tutte le caselle delle stanze, prendi una sola stanza(cella, parametro di proposeTragets)
        // Cyberblade target diversi CHECK
        //  RocketLaucher SAVE FIRST TARGET'S POSITION, IN OP2 I NEED IT (IT SHOULD BE PASSED AS c)

        // EFFECTS ORDER
        // Thor


        //  // ONLY WEAPONS WITH OP1 OR OP2 NEED THE REMOVAL OF TARGETS AFTER DOING DAMAGE
    }
    /**
     * reload
     * this is the method to reload a weapon card
     * you can choose if reload by AMMO or by AMMOPOWER
     *  @return boolean : to know if the action is good ended
     * @param p: player who does the action
     * @param option: payment option to reload
     * @param w: weapon card to be reload
     * this action can be deleted if the player can't reload the weapon
     *              CONV: if you can reload the card you can't delete the action
     */
//_____________________________________RELOAD________________________________________________________________________//
    public boolean reload(Player p, WeaponCard w,PayOption option) {


        switch(option){
            case AMMO:
                return reloadAmmo(p,w,p.getCubeBlue(),p.getCubeRed(),p.getCubeYellow());
            case AMMOPOWER:
                return reloadAmmoPower(p,w);
        }
        return false;
    }

    /**
     * chooseTargets
     * this is the method to choose targets for shooting action
     *
     * @return List<Object> : list of selected targets
     * @param possibleTarget : list of all targets that player can hit
     * @param number : number of the targets that player can hit
     *
     * CONV: targets can be other players or spawnpoints
     */
    ////////////////////////////_______________choose targets_________________________________________/////////////

    public List<Object>chooseTargets(List<Object> possibleTarget,int number){
        LinkedList<Object>effectiveTargets=new LinkedList<>();
        for(int i=0;i<possibleTarget.size();i++){
            //when a target is choosen add to effective player
            if(effectiveTargets.size()<=number)
                effectiveTargets.add(possibleTarget.get(i));
           else break;
        }
        return effectiveTargets;
    }

    /**
     * reloadAmmoPower
     * this is the method to reload a weapon card with powerUp+cubeColor
     *  @return boolean : to know if the action is good ended
     * @param player: player who does the action
     * @param weapon: weapon card to be reloaded
     * this action can be deleted if the player can't reload the weapon
     *              CONV: if you can reload the card you can't delete the action
     */


    ///////////////////////////_______________reloadAmmoPower_____________________________________///////////////////////////////////////////
    public boolean reloadAmmoPower(Player player,WeaponCard weapon){
        LinkedList<PowerUpCard>wallet=choosePowerUp(player);
        int redCube=player.getCubeRed();
        int blueCube=player.getCubeBlue();
        int yellowCube=player.getCubeYellow();
        for(int i=0;i<wallet.size();i++){
            switch (wallet.get(i).getPowerUpColor()){
                case RED: redCube++;break;
                case YELLOW:yellowCube++;break;
                case BLUE:blueCube++;break;
            }
        }
        if(!reloadAmmo(player,weapon,blueCube,redCube,yellowCube))
            return false;
        for(int i=0;i<wallet.size();i++)
            player.getPowerUp().remove(wallet.get(i));
        return true;
    }
    /**
     * reloadAmmoPower
     * this is the method to reload a weapon card with cubeColor (also with power up)
     *  @return boolean : to know if the action is good ended
     * @param p: player who does the action
     * @param w: weapon card to be reloaded
     * @param blue: number of blue cube color
     * @param red :number of red cube color
     * @param yellow :number of yellow cube color
     * this action can be deleted if the player can't reload the weapon
     *              CONV:
     *               i)if you can reload the card you can't delete the action
     *               ii)if you pay with power up blu,red,yellow get number of player's cube color plus powerup's colors
     */
///////////////////////////___________________reloadAmmo__________________________________/////////////////////////////////////
public boolean reloadAmmo(Player p,WeaponCard w,int blue,int red,int yellow){
    int blueToPay=0;
    int yellowToPay=0;
    int redToPay=0;
    for (int i = 0; i < w.price.size(); i++) {
        if (w.price.get(i).getEffect() == AmmoCube.Effect.BASE) {
            if (w.price.get(i).getCubeColor() == AmmoCube.CubeColor.BLUE)
                blueToPay++;
            if (w.price.get(i).getCubeColor() == AmmoCube.CubeColor.RED)
                redToPay++;
            if (w.price.get(i).getCubeColor() == AmmoCube.CubeColor.YELLOW)
                yellowToPay++;
        }
    }
    if(blue-blueToPay<0||red-redToPay<0||yellow-yellowToPay<0) {
        w.setNotReload();
        return false;//can't reload

    }
        for (int i = 0; i < w.price.size(); i++) {
            if (w.price.get(i).getEffect() == AmmoCube.Effect.BASE) {
                w.price.get(i).setPaid(true);
            }

        p.getAmmoBox()[0] = blue-blueToPay;
        p.getAmmoBox()[1] = red-redToPay;
        p.getAmmoBox()[2] = yellow-yellowToPay;
        w.setReload();

    }
return true;}
    /**
     * chooseWeaponCard
     * this is the method to choose a weapon card
     *  @return WeaponCard : weapon selected
     * @param hand: player's weapon hand
     * this action can't be deleted
     */
///////////////////////////______________choose weapon & canPay_________________////////////////////////////////////////

    public WeaponCard chooseWeaponCard(LinkedList<WeaponCard> hand) {
        int j=0;
       // for (j = 0; j < hand.size(); j++) {
            //WHEN A WEAPON IS CHOOSEN....,then BREAK
            return hand.get(j);
     /*   }


    return null;*/}
    /**
     * canPayCard
     * this is the method to know if player can pay a weapon card
     *  @return boolean: to know if method has a good end
     * @param weapon: weapon selected
     * @param player : player who does the action
     * @param option : payment option
     * this action can be deleted if can't pay weapon
     */
///____________________________________canPayCard(TRUE if can pay base effect)____________________________________///

    public boolean canPayCard(WeaponCard weapon, Player player,PayOption option) {
        if(option!=null){
        switch(option){

            case AMMOPOWER:{
                    return canPayAmmoPower(weapon,player,player.getPowerUp());
                    }

            case AMMO:{
               return canPayAmmo(weapon,player, player.getCubeRed(), player.getCubeYellow(), player.getCubeBlue());
                }//nope


        }}
      return false;
    }
    /**
     * canPayAmmo
     * this is the method to know if can pay a weapon card whit only cube colors (also power up)
     *  @return boolean: to know if good ended
     * @param player: player
     * @param red: number of player's red cube color
     * @param yellow :number of player's yellow cube color
     * @param blue :number of player's blue color
     *             this method can be deleted if player can't pay any BASE/ALT weapon card effect
     *  CONV:
     *             if payment method is with power up red,yellow,cube + powerup's colors
     */
    ///////////////////////________________canPayOnlyCube______________________________________________________________________////////////////////////

    public boolean canPayAmmo(WeaponCard weapon, Player player,int red,int yellow,int blue) {
        List<AmmoCube> cost = weapon.getPrice();
        int i;
        int redToPay=0;
        int blueToPay=0;
        int yellowToPay=0;
        boolean no = false;
        if(weapon.getReload())
            return true;
        //can you pay base effect or you can pay alt
        for (i = 0; i < cost.size(); i++) {
            if (((cost.get(i).getEffect().equals(AmmoCube.Effect.BASE) || cost.get(i).getEffect().equals(AmmoCube.Effect.ALT)) && !weapon.getReload()
            )) {

                for (int j = 0; j < numMaxAmmoToPay; j++) {

                    if (cost.get(i).getEffect().equals(cost.get(j).getEffect())) {


                        switch (cost.get(i).getCubeColor()) {
                            case RED:
                                redToPay++;
                                break;
                            case BLUE:
                                   blueToPay++;
                                break;
                            case YELLOW:
                                yellowToPay++;
                                break;
                                default:
                        }


                    }

                }

            }
            if(yellow-yellowToPay<0||red-redToPay<0||blue-blueToPay<0)
                return false;
        }
    return true;}
    /**
     * canPayAmmoPower
     * this is the method to know if can pay a weapon card whit only cube colors+ power up
     *  @return boolean: to know if good ended
     * @param player: player
     * @param weapon: weapon selected
     * @param powerUpCards: list of all player's powerups
     *             this method can be deleted if player can't pay any BASE/ALT weapon card effect
     *  CONV:
     *    if player selects a powerup that doesn't need to be used that power up is lost
     */
    ////////////////////___________________canPayAmmoPower pay bse effect with power up_____________________________________________________________//////////////

    public boolean canPayAmmoPower(WeaponCard weapon, Player player, LinkedList<PowerUpCard>powerUpCards){

       int redPower=0;
       int bluePower=0;
       int yellowPower=0;
        for(int i=0;i<powerUpCards.size();i++)
        {
            switch(powerUpCards.get(i).getPowerUpColor())
            {
                case RED: redPower++;
                    break;
                case BLUE: bluePower++;
                    break;
                case YELLOW: yellowPower++;
                    break;
                default:
            }
        }
        int redCube=player.getCubeRed()+redPower;
        int blueCube=player.getCubeBlue()+bluePower;
        int yellowCube=player.getCubeYellow()+yellowPower;
        return canPayAmmo(weapon,player,redCube,blueCube,yellowCube);

    }
    /**
     * choosePowerUp
     * this is the method to choose powerups to be used as a payment
     *             this method can't be deleted
     *
     */
    public LinkedList<PowerUpCard>choosePowerUp(Player player){
        int j;
        LinkedList<PowerUpCard>power=player.getPowerUp();
        LinkedList<PowerUpCard>powerChoosen=new LinkedList<>();
        for (j = 0; j < power.size(); j++) {
            //when a card is choosen
            powerChoosen.add(power.get(j));
        }
        return powerChoosen;

    }
    /**
     * paidEffect
     * this is the method to choose weapon's effect to pay and pay
     * @return  List<EffectAndNumber>: paid effects
     * @param player: player
     * @param weapon: weapon choosen
     * @param option: option payment
     *             this method can be deleted if returned list is null
     */
    //////////////////_____________________payMethods______________________________________________///////////////////////

    public List<EffectAndNumber> paidEffect(WeaponCard weapon, Player player,PayOption option) {

        switch(option){
            case AMMO: return payAmmo(player,weapon);
            case AMMOPOWER:return payAmmoPlusPowerUp(player,weapon,option);
            default:
        }
       return null; }
    /**
     * payAmmoPlusPowerUp
     * this is the method to choose weapon's effect to pay and pay with power up
     *  @return  List<EffectAndNumber>: paid effects
     * @param player: player
     * @param weapon: weapon card choosen
     * @param option : payment option //todo delete option
     *
     *               CONV:
     *               i) can pay only one between BASE and ALT
     *               ii) must pay i) before pay OPTIONS
     */
       //_________________________________________________payAmmoPlusPowerUp________________________________________________________//

    public List<EffectAndNumber>payAmmoPlusPowerUp(Player player,WeaponCard weapon,PayOption option){
       LinkedList<PowerUpCard>choosenPowerUp=choosePowerUp(player);
       EffectAndNumber effectAndNumber;
        if(!canPayAmmoPower(weapon,player,choosenPowerUp))
            return null;
        LinkedList<EffectAndNumber>paid=new LinkedList<>();
        if(!weapon.getReload()){
        for (int i = 0; i < numMaxAlternativeOptions; i++) {        //missing choose your effect base/alt here you source option position
            for (int j = 0; j < numMaxAmmoToPay; j++) {
                if (weapon.price.get(i).getEffect().equals(weapon.price.get(j).getEffect())&&((weapon.price.get(i).getEffect().equals(AmmoCube.Effect.BASE))||
                        (weapon.price.get(i).getEffect().equals(AmmoCube.Effect.ALT)))&&!weapon.getReload()) {
                    payPowerUp(weapon,choosenPowerUp,player);
                }
            }
            paid.get(0).setEffect(weapon.price.get(i).getEffect());
            if(paid.get(0).getEffect()==AmmoCube.Effect.BASE||paid.get(0).getEffect()==AmmoCube.Effect.ALT)
            break;  // i can pay only one
        }}
        if(weapon.getReload()){
            paid.get(0).setEffect(AmmoCube.Effect.BASE);
        }

        //now pay options

        for (int i = 0,k=1; i < weapon.getPrice().size(); i++,k++) {
            for (int j = 0; j < numMaxAmmoToPay; j++) {
                if (weapon.price.get(i).getEffect().equals(weapon.price.get(j).getEffect()) && (weapon.price.get(j).getEffect() .equals( AmmoCube.Effect.OP1) ||
                        weapon.price.get(j).getEffect() .equals( AmmoCube.Effect.OP2))) {
                    payPowerUp(weapon,choosenPowerUp,player);
                }


            }
            effectAndNumber=new EffectAndNumber(weapon.price.get(i).getEffect(),0);
            paid.add(k,effectAndNumber);
        }

        return paid;


    }  /**
     * payAmmoPlusPowerUp
     * this is the method to choose weapon's effect to pay and pay with only cube colors
     *  @return  List<EffectAndNumber>: paid effects
     * @param player: player
     * @param weapon: weapon card choosen
     *
     *
     *               CONV:
     *               i) can pay only one between BASE and ALT
     *               ii) must pay i) before pay OPTIONS
     */
        //_______________________________________________payOnlyAmmo________________________________________________________________//

    public List<EffectAndNumber>payAmmo(Player player,WeaponCard weapon){
        // if pay base don't psy alt
        int i = 0;
        LinkedList<EffectAndNumber> paid = new LinkedList<>();
        EffectAndNumber effectAndNumber;
        int k=0;
        List<AmmoCube> cost = weapon.getPrice();
        if(!weapon.getReload())
        {for (i = 0; i < numMaxAlternativeOptions; i++) {        //missing choose your effect base/alt here you source option position
            for (int j = 0; j < numMaxAmmoToPay; j++) {
                if (cost.get(i).getEffect().equals(cost.get(j).getEffect())&&((cost.get(i).getEffect().equals(AmmoCube.Effect.BASE))||
                        (cost.get(i).getEffect().equals(AmmoCube.Effect.ALT)))&&!weapon.getReload()) {
                    pay(player, cost.get(j));
                }
            }
            effectAndNumber=new EffectAndNumber(cost.get(i).getEffect(),0);
            paid.add(0,effectAndNumber);
        }}
        if(weapon.getReload()){
            effectAndNumber=new EffectAndNumber(AmmoCube.Effect.BASE,0);
            paid.add(0,effectAndNumber);
        }
        //then you can pay options
        for (i = 0,k=1; i < weapon.getPrice().size(); i++,k++) {
            for (int j = 0; j < numMaxAmmoToPay; j++) {
                if (cost.get(i).getEffect().equals(cost.get(j).getEffect()) && (cost.get(j).getEffect() .equals( AmmoCube.Effect.OP1) ||
                        cost.get(j).getEffect() .equals( AmmoCube.Effect.OP2))) {
                    pay(player, cost.get(j));
                }


            }
            effectAndNumber=new EffectAndNumber(weapon.price.get(i).getEffect(),0);
            paid.add(k,effectAndNumber);
        }

        return paid;

    }
    /**
     * pay
     * this is the method to pay
     * pay one cube at time
     * @param player: player
     * @param cube: color to pay
     *               CONV:
     *               doesn't check if can pay (already checked in previous methods)
     */
        //_______________________________________________effective pay_______________________________________________________________//

            public void pay (Player player, AmmoCube cube){
                switch (cube.getCubeColor()) {
                    case RED:
                        player.setCube(-1,0,0);
                        break;

                    case BLUE:
                        player.setCube(0,-1,0);
                        break;
                    case YELLOW:
                        player.setCube(0,0,-1);
                        break;
                        default:
                }
            }
    /**
     * payPowerUp
     * this is the method to pay whit cube colors and powerups
     * @param player: player
     * @param choosenPowerUp : selected powerups
     * @param weapon: selected weapon
     *               CONV:
     *            i)doesn't check if can pay (already checked in previous methods)
     *            ii)if player selects a powerup that doesn't need to be used that power up is lost
     */
            //_____________________________________effective pay with powerUp___________________________________________________//
    public void payPowerUp(WeaponCard weapon,List<PowerUpCard>choosenPowerUp,Player player){
        for(int index=0;index<choosenPowerUp.size();index++)
        {
            for(int j=0;j<weapon.getPrice().size();j++)
            {
                if(choosenPowerUp.get(index).getPowerUpColor()==(weapon.getPrice().get(j).getCubeColor())){

                    weapon.getPrice().remove(j);
                    player.getPowerUp().remove(index);
                    choosenPowerUp.remove(index);
                }
                else pay(player,weapon.getPrice().get(j));
            }
        }
    }
    /**
     * getEndTurn
     * @return  boolean: to know if turn is ended
     */
    //____________________________getter and setter________________//
public boolean getEndturn(){
        return this.endTurn;
}

    /**
     *setEndTurn
     * set when turn is ended
     */
public void setEndTurn(boolean bool){this.endTurn=bool;}
    /**
     * canGetPoints
     * this is the method to know if players can get points
     * @param allPlayers
     * @param victims
     *
     */
//________________________GIVE POINTS_______& ENDOFTHEGAME___________________________//
public void canGetPoints(List<Player> victims,List<Player>allPlayers){
   List<Player> bestPlayerOrderForVictim;
        for(int indexVictims=0;indexVictims<victims.size();indexVictims++) {
                bestPlayerOrderForVictim=bestShooterOrder(allPlayers,victims.get(indexVictims));
                givePoints(victims.get(indexVictims),bestPlayerOrderForVictim);
                //call to give points
        }



}
    /**
     * givePoints
     * this is the method to distribute points
     * @param victim: player who has been damaged
     * @param shooters: all players which have given damage to victim
     *
     */

    public void givePoints(Player victim,List<Player>shooters){
        // max point - 2 x death if maxpoint-2<=0 give 1 point
        victim.setMaxPointAssignableCounter(victim.numberOfDeaths());
        if(victim.getMaxPointAssignableCounter()>=victim.getTrackPointSize()){
                for (int indexPlayer=0; indexPlayer<shooters.size();indexPlayer++)
                {  shooters.get(indexPlayer).setPoints(1); //every player get 1 points
                 }
        return;}

        ((Player) ((LinkedList)shooters).getFirst()).setPoints(victim.getPointTrack()[victim.getMaxPointAssignableCounter()]);
        for(int indexPlayer=1;indexPlayer<shooters.size();indexPlayer++)
        {
            if(victim.getTrack()[0]==shooters.get(indexPlayer).getColor())
                shooters.get(indexPlayer).setPoints(1);//firstBloodPoints
            if(victim.getTrack()[victim.getTrackSize()-1]==shooters.get(indexPlayer).getColor())
                shooters.get(indexPlayer).addMarks(victim,1);//12°hit
            if(victim.damageByShooter(shooters.get(indexPlayer))==victim.damageByShooter(shooters.get(indexPlayer-1)))
                ((Player)((LinkedList)shooters).getFirst()).setPoints(victim.getPointTrack()[victim.getMaxPointAssignableCounter()]);
            else{
                victim.setMaxPointAssignableCounter(victim.getMaxPointAssignableCounter()+1);
                if(victim.getMaxPointAssignableCounter()>=victim.getTrackPointSize())
                    shooters.get(indexPlayer).setPoints(1);
                else
                    ((Player)((LinkedList)shooters).getFirst()).setPoints(victim.getPointTrack()[victim.getMaxPointAssignableCounter()]);
            }
        }

    }
    /**
     * endOfTheGame
     * @return boolean:to know if the game is over
     * **/
    public boolean endOfTheGame(GameBoard g){  //every time a player dies
        //8||5 skulls
        //if(countSkull-1<=0)

        if(g.getNumSkull()<=0)
        return true;
        //else return false
        else return false;
    }
/**
 * bestShooterOrder
 * Order by how much damage a player has done to victim
 * @return List<Player>: order list of player
 * @param players all players
 * @param victim: selected victim
 * **/
    public List<Player> bestShooterOrder(List<Player> players,Player victim){
        LinkedList<Player> bestShooterOrder=new LinkedList<>();
        int maxDamage=0;
        for (int i=0;i<players.size();i++){
            if(victim.damageByShooter(players.get(i))>0) {
                if(victim.damageByShooter(players.get(i))>=maxDamage) {
                    maxDamage = victim.damageByShooter(players.get(i));
                    bestShooterOrder.addFirst(players.get(i));
                }
                else
                    {for(int indexBestShooterOrder=0; indexBestShooterOrder<bestShooterOrder.size();indexBestShooterOrder++)
                        {
                    if (victim.damageByShooter(players.get(i))>=victim.damageByShooter(bestShooterOrder.get(indexBestShooterOrder)))
                    { bestShooterOrder.add(indexBestShooterOrder,players.get(i));
                        break;}
                        }
                     //here we get out from the for 2 options: our player is the last in the list or it is the one with least points of all and
                     // it's still to be add
                     if(!bestShooterOrder.contains(players.get(i)))
                         bestShooterOrder.addLast(players.get(i));
                }
            }

            //if ==0 no points to add to the player
        }


        return bestShooterOrder;
    }
    public int getRemaingPlayer(int numPlayer){
        return numPlayer--;
    }
}


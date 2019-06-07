package adrenaline;


import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

import static adrenaline.AmmoCube.CubeColor.*;

/**
 * Is the class that describes players' actions
 * @author Giulia Valcamonica
 * @version 2.0
 **/
public class Action {


    private int numOfRemainingPlayer;

    private  boolean endTurn;

    private GameModel model;

    public static enum ActionType {
        GRAB, RUN, SHOOT, RELOAD
    }

    public static enum PayOption {
        AMMO,AMMOPOWER,NONE
    }

/*
* -------------------------------CHANGE IN PROGRESS----------------------------------------------
*now the effective controller is the Server
* Action contains methods' implementation
*
* scheme is :  SERVER ->CONTROLLER    CONTROLLER->MODEL  CLIENT->SERVER
*                      ->MODEL                                 <-
* -----------------------------------------------------------------------------------------------
* */

    /**
     * Class constructor.
     *  * @version 2.0
     */

    public Action(GameModel m)
    {
      this.model=m;
    }


    //--------------------------------------PROPOSING CELLS-----------------------------------------------------------//


    /**
     * proposeCellsRun
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to run
     * @param c: player position
     * this action can't be deleted
    CONV:
     *      i)this is the not Frenzy option method
     *      ii) move at least by one cell
     *      iii) move max by three cell
     */
    //___________________________ PROPOSE CELL WHERE TO GO (DISTANCE 1-2-3)___________________________________________//
    public LinkedList<CoordinatesWithRoom> proposeCellsRun(CoordinatesWithRoom c) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 1));
        list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 2));
        list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 3));
        return list;
    }


    /**
     * proposeCellsRunBeforeShoot
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to run before shooting
     * @param player: player in turn
     * this action can't be deleted
     * CONV:
     *      i)this is the not Frenzy option method
     *      ii) you can move only by one cell if not adrenaline
     *      iii)you can move up to two cells if adrenaline (calls adrenaline option)
     *      iv)you can delete moving by selecting your cell
     */
    //__________________________PROPOSE CELL TO MOVE BEFORE SHOOT ____________________________________________________//

    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShoot(Player player){
        LinkedList<CoordinatesWithRoom>list=new LinkedList<>(player.getCoordinatesWithRooms().xTilesDistant(getModel().getMapUsed().getGameBoard(),1));
        list.add(player.getCoordinatesWithRooms());
        if(player.checkDamage()>=2)
            list.addAll(proposeCellsRunBeforeShootAdrenaline(player));
    return list;}


    /**
     * proposeCellsRunBeforeShootAdrenaline
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to run before shooting
     * @param player: player in turn
     * this action can't be deleted
     * CONV:
     *      i)this is the not Frenzy option method
     *      ii)you can move up two cells
     *      iii) option of proposeCellsRunBeforeShoot
     */

    //_________________________PROPOSE CELL TO MOVE BEFORE SHOOT ADRENALINE___________________________________________//
    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShootAdrenaline(Player player){
       LinkedList<CoordinatesWithRoom>list=new LinkedList<>(player.getCoordinatesWithRooms().xTilesDistant(getModel().getMapUsed().getGameBoard(),2));
        return list;
    }


    /**
     * proposeCellsGrab
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to Grab
     * @param player: player in turn
     *
     * this action can't be deleted
     * CONV:
     *      i)this is the not Frenzy option method
     *      ii) you can grab on your position
     *      iii) you can grab max one cell distant (includes moving)
     *      iv) you can grab max 2 cell distant if adrenaline (call adrenaline option)
     *      v)you can delete movement option by selecting your starting cell
     */

    //_______________ PROPOSE CELLS WHERE TO GRAB (DISTANCE 0-1 OR 0-1-2 IF ADRENALINE)_______________________________//
    public LinkedList<CoordinatesWithRoom> proposeCellsGrab(Player player) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(player.getCoordinatesWithRooms().xTilesDistant(getModel().getMapUsed().getGameBoard(), 1));
        list.add(player.getCoordinatesWithRooms());
        if(player.checkDamage()>=1)
            list.addAll(proposeCellsGrabAdrenaline(player));
        return list;
    }
    /**
     * proposeCellsGrabAdrenaline
     * @return LinkedList<CoordinatesWithRoom> : a LinkedList of selectable cells where to Grab
     * @param player: player in turn
     * this action can't be deleted
     * CONV:
     *      i)this is the not Frenzy option method
     *      ii) you can grab in the same cells of proposeCellsGrab
     *      iii) you can grab max two cells distant (includes moving)
     *      iv)option of proposeCellsGrab method
     */
    //____________PROPOSE CELLS GRAB WITH ADRENALINE checkDamage=(1||2)
    public LinkedList<CoordinatesWithRoom> proposeCellsGrabAdrenaline(Player player) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(player.getCoordinatesWithRooms().xTilesDistant(getModel().getMapUsed().getGameBoard(), 2));
        return list;
    }


    //---------------------------------------RUNNING/CHANGING POSITION------------------------------------------------//

    /**
     * run: to do effective run action
     * @param c: player position
     * @param p: running player
     * this action can't be deleted
     *         CONV:
     *         i)c position is contain in proposeCellsRun
     *         ii) it's used by grabbing methods and by some weapon effects
     */
    //_______________________________________________RUN______________________________________________________________//
    public void run(Player p, CoordinatesWithRoom c) {

        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
    }


    //-------------------------------------GRABBING METHODS-----------------------------------------------------------//


    /**
     * grabCard
     * this is the method to grab a weapon card & reload
     * @return boolean : to know if the action is good ended

     * @param player: player who does the action
     * @param option: payment option to grab
     * @param firstOptionToPay effect to pay
     * @param wChoosen: selected card from selected spawnpoint
     *
     * this action can be deleted if the player can't grab the weapon
     *
     *              CONV:
     *              i)if you can grab the card you can't delete the action
     *              ii) if you buy a weapon card you have already paid its BASE effect
     *                  so you can't use its ALT effect (if you can pay BASE)
     *              iii) if you don't want to pay the BASE effect check if you can pay ALT effect
     *                      in that case tou have ALT effect and you can't use BASE effect
     *              iv)firstOptionToPay can be BASE or ALT
     *              v) player has <3 weaponCard
     *
     */
    //____________________________________________GRAB OPTIONS(WEAPON)________________________________________________//

    public boolean grabCard(Player player, PayOption option, AmmoCube.Effect firstOptionToPay, WeaponCard wChoosen){

        if(!canPayCard(wChoosen,player,option,firstOptionToPay))
            return false;
        player.getHand().add(wChoosen);

        switch (firstOptionToPay) {
            case BASE: wChoosen.setReload(); //when i grab a weapon i've already paid its base effect or alt
                        break;

            case ALT:  wChoosen.setReloadAlt(true);
                                break;
            default: return false;
        }

        return true;
    }


    /**
     * grabTile
     * this is the method to grab an ammo tile
     * @return boolean : to know if the action is good ended
     * @param c: grab position
     * @param player: player who does the action
     *
     * this action can't be deleted if the player doesn't grab anything
     *
     * CONV:
     *              i)c is in proposeCellsGrab
     *              ii)this action includes player movement
     */
    //_____________________________________GRAB OPTION TILE __________________________________________________________//
    public boolean grabTile(Player player, CoordinatesWithRoom c){
        AmmoTile toBeGrabbedTile=c.getRoom().getAmmoTile(c);
        // grab ammo or powerUp
        run(player,c);
        for (AmmoCube cube: toBeGrabbedTile.getAmmoCubes()
             ) {
            if(cube.getCubeColor()== POWERUP)
                grabPowerUp(player);
        }
                    return grabCube(player, toBeGrabbedTile);


    }

    /**
     * grabCube
     * this is the method to grab a color cube
     * @return boolean : to know if the action is good ended

     * @param player: player who does the action
     * @param a: ammo tile to be grabbed
     *
     *              CONV:
     *            i) if the player has already three  cube of one color, he can't pick up more cube of that color
     *            ii) even if the player has already max number of all colors this action can't be deleted
     */


    //_______________________________ADDING CUBE______________________________________________________________________//
    public boolean grabCube(Player player, AmmoTile a){

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
       player.getCoordinatesWithRooms().xTilesDistant(getModel().getMapUsed().getGameBoard(),0).equals(a.getCoordinates());
        getModel().ammoTileDeck.getDeck().addLast(a);

        return true;

    }
    /**
     * grabPowerUp
     * this is the method to grab a power up card
     * it is called only when an ammo tile has field POWER
     *  @return boolean : to know if the action is good ended

     * @param p: player who does the action
     * this action can be deleted if the player can't grab the power up
     *              CONV:
     *         i)     if you can grab the card you can't delete the action
     *         ii)    if you have already three powerups you can't pickup this ammotile
     */
public void grabPowerUp(Player p){
        if(p.canGrabPowerUp()){
            PowerUpCard power=getModel().powerUpDeck.pickPowerUp();
            p.getPowerUp().add(power);
            getModel().powerUpDeck.getPowerUpDeck().remove(power);}
}

//-----------------------------------------SHOOTING & WEAPONS METHODS-------------------------------------------------//

    /**
     * shoot
     * this is the method to shoot
     * @param p: player who does the action
     * @param w: weapon card choosen to shoot
     * @param victim: player's targets with the same effect
     * @param attackEffect: one effect paid by player

     *      * this action can't
     *          CONV:
     *                    i)you can shoot even at empty cells
     *                    ii)victim coordinatesWithRooms is contained in weapon possible targets
     *
     */
    //______________________________________SHOOT_____________________________________________________________________//
    public void shoot(WeaponCard w, Player p, EffectAndNumber attackEffect,LinkedList<Object> victim) {

           // w.weaponShoot(victim,p.getCoordinatesWithRooms(),p,attackEffect,getModel());
        }
        //HA SWITCH CASE IN BASE A CHE ARMA, SE NORMALI(QUELLO CHE VEDO) CASE COMUNE

        //GET TARGETS and THEN ADD NUMBER OF TARGETS TO EFFECTSLIST FOR EVERY EFFECT PAID FOR

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


    /**
     * reload
     * this is the method to reload a weapon card
     * you can choose if reload by AMMO or by AMMOPOWER
     * @return boolean : to know if the action is good ended
     * @param p: player who does the action
     * @param option: payment option to reload
     * @param wallet: list of choosen powerUps to be used as a payment (can be empty)
     * @param w: weapon card to be reload
     * this action can be deleted if the player can't reload the weapon
     *              CONV: if you can reload the card you can't delete the action
     */
//_____________________________________RELOAD________________________________________________________________________//
    public boolean reload(Player p, WeaponCard w, PayOption option, AmmoCube.Effect firstOptionToPay,LinkedList<PowerUpCard>wallet) {


        switch(option){
            case AMMO:
                return reloadAmmo(p,w,p.getCubeBlue(),p.getCubeRed(),p.getCubeYellow(),firstOptionToPay);
            case AMMOPOWER:
                return reloadAmmoPower(p,w,firstOptionToPay,wallet);
            default: return false;
        }

    }


    /**
     * reloadAmmoPower
     * this is the method to reload a weapon card with powerUp+cubeColor
     * @return boolean : to know if the action is good ended
     * @param player: player who does the action
     * @param weapon: weapon card to be reloaded
     * @param wallet: list of choosen powerUps to be used as a payment
     * this action can be deleted if the player can't reload the weapon
     *              CONV:
     *              i)if you can reload the card you can't delete the action
     *              ii)even if a powerUp isn't used it would be deleted
     *              iii)if wallet is empty check if I can pay with only ammoCube
     */

    //__________________________________reloadAmmoPower_______________________________________________________________//

    public boolean reloadAmmoPower(Player player, WeaponCard weapon, AmmoCube.Effect firstOptionToPay,LinkedList<PowerUpCard> wallet){

        int redCube=player.getCubeRed();
        int blueCube=player.getCubeBlue();
        int yellowCube=player.getCubeYellow();

        for (PowerUpCard power:wallet) {
            switch (power.getPowerUpColor()){
                case RED: redCube++;break;
                case YELLOW:yellowCube++;break;
                case BLUE:blueCube++;break;
                case POWERUP: //invalid
                    break;
                case FREE://invalid
                    break;
            }
        }

        if(!reloadAmmo(player,weapon,blueCube,redCube,yellowCube,firstOptionToPay))
            return false;
        for (PowerUpCard power:wallet) {
            player.getPowerUp().remove(power);
            getModel().powerUpDeck.getPowerUpDeck().addLast(power);
        }

        return true;
    }
    /**
     * reloadAmmoPower
     * this is the method to reload a weapon card with cubeColor (also with power up)
     * @return boolean : to know if the action is good ended
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
//_____________________________________reloadAmmo_____________________________________________________________________//
public boolean reloadAmmo(Player p, WeaponCard w, int blue, int red, int yellow, AmmoCube.Effect firstOptionToPay){
    int blueToPay=0;
    int yellowToPay=0;
    int redToPay=0;

    for (AmmoCube ammoCube:w.getPrice()) {
        if(ammoCube.getEffect().equals(firstOptionToPay)){
            if (ammoCube.getCubeColor() == AmmoCube.CubeColor.BLUE)
                blueToPay++;
            if (ammoCube.getCubeColor() == AmmoCube.CubeColor.RED)
                redToPay++;
            if (ammoCube.getCubeColor() == AmmoCube.CubeColor.YELLOW)
                yellowToPay++;
        }
    }

    if(blue-blueToPay<0||red-redToPay<0||yellow-yellowToPay<0) {
        return false;
    }

               switch (firstOptionToPay)
               {
                   case ALT: w.setReloadAlt(true); break;
                   case BASE:w.setReload(); break;
                   default: //nothing

               }
        p.getAmmoBox()[0] = blue-blueToPay;
        p.getAmmoBox()[1] = red-redToPay;
        p.getAmmoBox()[2] = yellow-yellowToPay;


return true;}
//---------------------------------------PAYMENT METHODS--------------------------------------------------------------//


    /**
     * canPayCard
     * this is the method to know if player can pay a weapon card
     * @return boolean: to know if method has a good end
     * @param weapon: weapon selected
     * @param player : player who does the action
     * @param option : payment option
     * this action can be deleted if can't pay weapon
     */
//_____________________________________canPayCard(TRUE if can pay or alt base effect)_________________________________//

    public boolean canPayCard(WeaponCard weapon, Player player, PayOption option, AmmoCube.Effect firstOptionToPay) {
        if(option!=PayOption.AMMO||option!=PayOption.AMMOPOWER||option==PayOption.NONE){
        switch(option){

            case AMMOPOWER:{
                    return canPayAmmoPower(weapon,player,player.getPowerUp(),firstOptionToPay);
                    }

            case AMMO:{
               return canPayAmmo(weapon, player.getCubeRed(), player.getCubeYellow(), player.getCubeBlue(),firstOptionToPay);
                }//nope

            case NONE: //invalid
                break;
        }}
      return false;
    }
    /**
     * canPayAmmo
     * this is the method to know if can pay a weapon card effect whit only cube colors (also power up)
     * @return boolean: to know if good ended
     * @param red: number of player's red cube color
     * @param yellow :number of player's yellow cube color
     * @param blue :number of player's blue color
     *             this method can be deleted if player can't pay any BASE/ALT/OP weapon card effect
     *  CONV:
     *             if payment method is with power up red,yellow,cube + powerup's colors
     */
    //______________________________________________canPayOnlyCube____________________________________________________//

    public boolean canPayAmmo(WeaponCard weapon, int red, int yellow, int blue, AmmoCube.Effect firstOptionToPay) {
        List<AmmoCube> cost = weapon.getPrice();
        int redToPay=0;
        int blueToPay=0;
        int yellowToPay=0;

        //can you pay base effect or you can pay alt
        for (AmmoCube ammoCube:cost
             ) {
            if(ammoCube.getEffect().equals(firstOptionToPay)){
                switch (ammoCube.getCubeColor()) {
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
            if(yellow-yellowToPay<0||red-redToPay<0||blue-blueToPay<0)
                return false;
        }
    return true;}

    /**
     * canPayAmmoPower
     * this is the method to know if can pay a weapon card whit only cube colors+ power up
     * @return boolean: to know if good ended
     * @param player: player
     * @param weapon: weapon selected
     * @param powerUpCards: list of all player's powerups (if it is empty check if can pay with ammoTile)
     *             this method can be deleted if player can't pay any BASE/ALT weapon card effect
     *  CONV:
     *    if player selects a powerup that doesn't need to be used that power up is lost
     */
    ////////////////////___________________canPayAmmoPower pay bse effect with power up_____________________________________________________________//////////////

    public boolean canPayAmmoPower(WeaponCard weapon, Player player, LinkedList<PowerUpCard>powerUpCards,AmmoCube.Effect firstOptionToPay){

       int redPower=0;
       int bluePower=0;
       int yellowPower=0;
        for (PowerUpCard power:powerUpCards
             ) {
            switch(power.getPowerUpColor())
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

        return canPayAmmo(weapon,redCube,blueCube,yellowCube,firstOptionToPay);

    }


    /**
     * paidEffect
     * this is the method to choose weapon's effect to pay and pay
     * @return  List<EffectAndNumber>: paid effects
     * @param player: player
     * @param number: a param used to indicate number of victims or other things
     * @param weapon: weapon choosen
     * @param firstOptionToPay: selected effect
     * @param option: option payment
     *             this method can be deleted if returned list is null
     */
    //_____________________________________payMethods_________________________________________________________________//

    public EffectAndNumber paidEffect(WeaponCard weapon, Player player, PayOption option, AmmoCube.Effect firstOptionToPay,LinkedList<PowerUpCard>wallet,int number) {

        switch (option) {
            case AMMO:
                return payAmmo(player, weapon, firstOptionToPay,number);
            case AMMOPOWER:
                return payPowerUp(weapon,wallet,player,firstOptionToPay,number);
            default: return null;
        }
    }



       /**
     * payAmmoPlusPowerUp
     * this is the method to choose weapon's effect to pay and pay with only cube colors
     * @return  List<EffectAndNumber>: paid effects
     * @param player: player
     * @param weapon: weapon card choosen
     * @param number: a param used to indicate number of victims or other things
     *
     *
     *               CONV:
     *               i) can pay only one between BASE and ALT
     *               ii) must pay i) before pay OPTIONS
     */
       //_______________________________________________payOnlyAmmo__________________________________________________//

    public EffectAndNumber payAmmo(Player player,WeaponCard weapon,AmmoCube.Effect firstOptionToPay,int number){
        // if pay base don't psy alt
        List<AmmoCube> cost = weapon.getPrice();

        for (AmmoCube ammoCube:cost) {

            if(ammoCube.getEffect().equals(firstOptionToPay)){
                pay(player,ammoCube);
            }

        }
        return new EffectAndNumber(firstOptionToPay,number);

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
     * @param number: a param used to indicate number of victims or other things
     * @param effect : effect to pay
     * CONV:
     *            i)doesn't check if can pay (already checked in previous methods)
     *            ii)if player selects a powerup that doesn't need to be used that power up is lost
     */
            //_____________________________________effective pay with powerUp___________________________________________________//
    public EffectAndNumber payPowerUp(WeaponCard weapon, List<PowerUpCard>choosenPowerUp, Player player, AmmoCube.Effect effect,int number){
        int yellow=0;
        int red=0;
        int blue=0;

        for (PowerUpCard power:choosenPowerUp) {
           switch (power.getPowerUpColor()){
               case RED: red++; break;
               case YELLOW: yellow++; break;
               case BLUE: blue++ ; break;
                   default://
           }
        }
                payPower(player,weapon.getPrice(),red,blue,yellow,effect);

        return new EffectAndNumber(effect,number);
    }
    /**
     * payPower
     * effective method to pay with powerUp
     * @param red : red "cubes" given by used powerUp
     * @param blue: blue "cubes" given by used powerUp
     * @param yellow: yellow "cubes" given by used powerUp
     * @param player: player in turn
     * @param effect: effect to pay
     * @param price : price of weaponCard*/
    public void payPower (Player player, List<AmmoCube> price, int red,int blue,int yellow,AmmoCube.Effect effect){
        for (AmmoCube cube: price) {

        if(cube.getEffect().equals(effect)){
        switch (cube.getCubeColor()) {
            case RED:
                if(red<=0)
                    player.setCube(-1,0,0);
                else
                    red--;
                break;

            case BLUE:
                if(blue<=0)
                    player.setCube(0,-1,0);
                else
                    blue--;
                break;
            case YELLOW:
                if(yellow<=0)
                    player.setCube(0,0,-1);
                else
                    yellow--;
                break;
            default:
        }}}
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

        return (g.getNumSkull()<=0);

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
    public int getRemaingPlayer(){
        return numOfRemainingPlayer;
    }
    public void setRemainigPlayerMinus(int num){
       numOfRemainingPlayer= numOfRemainingPlayer-num;
    }

    public GameModel getModel(){
        return this.model;
    }


}


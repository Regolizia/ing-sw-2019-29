package adrenaline;


import java.util.LinkedList;
import java.util.List;

import static adrenaline.AmmoCube.CubeColor.*;


public class Action {
    final private int numMaxAlternativeOptions = 2; //(0=base 1=alternative)
    final private int numMaxAmmoToPay = 3;//(0:first price 1:second price 2:second price)
    final private int numMaxWeaponYouCanHave=3;


   private boolean executedFirstAction=false;
    private  boolean executedSecondAction=false;
    private  boolean endTurn=false;
    private  boolean newStart=false;
    private  boolean deletedAction=false;
    private int notDeleted;

    public static enum ActionType {
        GRAB, RUN, SHOOT, RELOAD;      //reload is an  optional action //ADRENALINESHOOT
    }

    public static enum PayOption {
        AMMO,AMMOPOWER,NONE;      //reload is an  optional action //ADRENALINESHOOT
    }

  //  private ActionType actionSelected;


    public Action(boolean newStart) {
        this.newStart=newStart;
        if(this.newStart){
       this.executedFirstAction=false;
       this.executedSecondAction=false;
       setEndTurn(false);
       this. deletedAction=false;
        this.notDeleted=0;}
    }
    public Action(){}

    //________________________________DO ACTION_____________________________________________________________________//
    public void doAction(ActionType actionSelected, Player player, CoordinatesWithRoom c, GameBoard g, GameModel m,PayOption option,GameModel.FrenzyMode frenzyMode){
      //  actionSelected = chosen;

        if(!getEndturn()&&!endOfTheGame(g)) {
            switch (actionSelected) {
                case RUN:
                    // PROPOSE WHERE TO GO, SELECT ONE (with proposeCellsRun method)
                    //proposeCellsRun(c, g);
                    CoordinatesWithRoom coordinatesR=chooseCell(proposeCellsRun(c,g));
                    run(player, coordinatesR);
                    deletedAction=false;
                    break;
                case GRAB:
                    // PROPOSE CELL WHERE TO GRAB (EVERY CELL HAS SOMETHING) (DISTANCE 0-1 OR 0-1-2) (with proposeCellsGrab)
                    CoordinatesWithRoom coordinatesG;
                    if(player.checkDamage()==1||player.checkDamage()==2)
                    coordinatesG=chooseCell(proposeCellsGrabAdrenaline(c, g, player));
                    else coordinatesG=chooseCell(proposeCellsGrab(c,g,player));
                    if(!(grab(player, coordinatesG, g,option)))
                    deletedAction=true;
                    else player.setPlayerPosition(coordinatesG.getX(),coordinatesG.getY(),coordinatesG.getRoom());
                    break;

                case SHOOT:
                    // HERE JUST WEAPON AND PAYMENTS, EVERYTHING ELSE IN CORRECT WEAPONCARD

                    // WeaponList player.checkWeapon() // GIVES ALL THE WEAPON OWNED BY THE PLAYER
                    LinkedList<WeaponCard> hand = player.getHand();
                    // WeaponCard chooseWeaponCard()// GIVES THE SELECTED WEAPON
                    WeaponCard weapon = chooseWeaponCard(hand);
                    // Boolean payCard()
                    if (weapon.getReload() == false) {
                        if (!canPayCard(weapon, player,option))
                            deletedAction=true;
                        break;
                    }
                    if(canPayCard(weapon,player,option)){
                        List<EffectAndNumber> payEff = paidEffect(weapon, player,option);
                        if(payEff==null)
                        {deletedAction=true;break;}
                        CoordinatesWithRoom cChoosen;
                        if(player.checkDamage()==2)
                            cChoosen=chooseCell(proposeCellsRunBeforeShootAdrenaline(c,g));
                        else cChoosen=chooseCell(proposeCellsRunBeforeShoot(c,g));
                        player.setPlayerPosition(cChoosen.getX(),cChoosen.getY(),cChoosen.getRoom());
                        shoot(weapon, cChoosen, player, payEff, m,g);
                        weapon.setNotReload();// i've lost base effect payment
                        deletedAction=false;
                    }                    break;

                default:
                    //INVALID CHOICE

            }

            if(executedFirstAction&&!deletedAction&&!executedSecondAction)this.executedSecondAction=true;
            else if(!executedFirstAction&&deletedAction&&!executedSecondAction){this.executedFirstAction=false;this.executedSecondAction=false;}
            else if(executedFirstAction&&deletedAction&&!executedSecondAction)this.executedSecondAction=false;
            else if(!executedFirstAction&&!deletedAction&&!executedSecondAction) {this.executedFirstAction=true;this.executedSecondAction=false;}
            if(executedFirstAction&&executedSecondAction)setEndTurn(true);
        }
        //HERE ENDS TURN
        else if(getEndturn()){

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
        }
        /////
        if(endOfTheGame(g)){
            if(frenzyMode== GameModel.FrenzyMode.ON)
            {

                FreneticAction fAction=new FreneticAction();
                //start by the player in the turn
                List<Player> players=m.getPlayers();
                int initialPlayerIndex=players.indexOf(player);
                for(int indexPlayer=initialPlayerIndex;indexPlayer<initialPlayerIndex+players.size();indexPlayer++)
                {
                    if(players.get(initialPlayerIndex)==((LinkedList<Player>)m.getPlayers()).getFirst()&&notDeleted<2) {
                        if (fAction.selectFrenzyAction(actionSelected,player,c,g,m,option, FreneticAction.PlayerOrder.AFTER))
                            notDeleted++;
                    }

                    else{
                        if(players.get(initialPlayerIndex)==players.get(indexPlayer)||indexPlayer>initialPlayerIndex&& notDeleted<2)
                        {
                            if(fAction.selectFrenzyAction(actionSelected,player,c,g,m,option, FreneticAction.PlayerOrder.FIRST))
                                notDeleted++;

                        }
                        if(players.get(indexPlayer)==(((LinkedList<Player>)m.getPlayers())).getFirst()||indexPlayer<initialPlayerIndex&&notDeleted<2){
                                if(fAction.selectFrenzyAction(actionSelected, player, c, g, m, option, FreneticAction.PlayerOrder.AFTER))
                                    notDeleted++;


                         }


                    }
                    if(notDeleted>=2)
                        notDeleted=0; //reset for next player
                    else
                        indexPlayer--;//repeat this player until he doesn't have two actions

                }

            }
            //if endOfTheGame && frenzy then for everyplayer calls a frenzy action
            //then stop the game
        }
    }

    //______________________________CHOOSE CELL______________________________________________________________________//
    public CoordinatesWithRoom chooseCell(LinkedList<CoordinatesWithRoom>coordinates){
        CoordinatesWithRoom choosenCell=new CoordinatesWithRoom();
        for(int i=0;i<coordinates.size();i++)
        {
            //here select the one you choose and then break
        }
        return choosenCell;
    }


    //___________________________ PROPOSE CELL WHERE TO GO (DISTANCE 1-2-3)___________________________________________//
    public LinkedList<CoordinatesWithRoom> proposeCellsRun(CoordinatesWithRoom c, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(g, 1));
        list.addAll(c.xTilesDistant(g, 2));
        list.addAll(c.xTilesDistant(g, 3));
        return list;
    }
    //__________________________PROPOSE CELL TO MOVE BEFORE SHOOT NOT FRENZY_____________________________________________//

    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShoot(CoordinatesWithRoom c,GameBoard g){
        LinkedList<CoordinatesWithRoom>list=new LinkedList<>(c.xTilesDistant(g,1));
        list.add(c);
    return list;}
    //_________________________PROPOSE CELL TO MOVE BEFORE SHOOT ADRENALINE_______________________________________________//
    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShootAdrenaline(CoordinatesWithRoom c,GameBoard g){
        LinkedList<CoordinatesWithRoom>list=new LinkedList<>(c.xTilesDistant(g,1));
        list.addAll(c.xTilesDistant(g,2));
        list.add(c);
        return list;
    }

    //_______________________________________________RUN______________________________________________________________//
    public void run(Player p, CoordinatesWithRoom c) {

        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
    }

    //_______________ PROPOSE CELLS WHERE TO GRAB (DISTANCE 0-1 OR 0-1-2 IF ADRENALINE)_______________________________//
    public LinkedList<CoordinatesWithRoom> proposeCellsGrab(CoordinatesWithRoom c, GameBoard g, Player p) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(g, 1));
        list.add(c);
        return list;
    }
    //____________PROPOSE CELLS GRAB WITH ADRENALINE checkDamage=(1||2)
    public LinkedList<CoordinatesWithRoom> proposeCellsGrabAdrenaline(CoordinatesWithRoom c, GameBoard g, Player p) {
        LinkedList<CoordinatesWithRoom> list = proposeCellsGrab(c,g,p);
        list.addAll(c.xTilesDistant(g, 2));
        return list;
    }

    //________________________________________________GRAB____________________________________________________________//
    public boolean grab(Player p, CoordinatesWithRoom c, GameBoard g,PayOption option) {
        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
        // IF THERE IS A SPAWNPOINT HERE
        LinkedList<Spawnpoint> spawnpoints=c.getRoom().getSpawnpoints();

     for (int i=0;i<spawnpoints.size();i++){
       if( c.getRoom()==p.getPlayerRoom()&&c.getX()==p.getPlayerPositionX()&&c.getY()==p.getPlayerPositionY()
            && c.getRoom().getSpawnpoints()!=null && c.getRoom().getSpawnpoints().get(i).getSpawnpointX()==c.getX()&&
       c.getRoom().getSpawnpoints().get(i).getSpawnpointY()== c.getY())
       {
        //CHOOSE WEAPON IF CANGRAB IT
        return   grabCard(p,p.getHand(),c,option);}}

        return grabTile(p,c);

    }

    //____________________________________________GRAB OPTIONS(WEAPON)________________________________________________________//

    public boolean grabCard(Player player,LinkedList<WeaponCard> hand, CoordinatesWithRoom c,PayOption option){
        int index;
        //TODO a way to convert propose to grab cells in weaponCard/Card
        LinkedList <WeaponCard> canBeGrabbedWeapon=new LinkedList<>();
        WeaponCard w;
        for (index = 0; index <canBeGrabbedWeapon.size() ; index++) {
            //WHEN A WEAPON IS CHOOSEN BREAK
        }
        if(!canPayCard(canBeGrabbedWeapon.get(index),player,option))
            return false;
        if(canPayCard(canBeGrabbedWeapon.get(index),player,option)&&hand.size()>=numMaxWeaponYouCanHave)
        dropWeaponCard(hand); //I need to drop that card


        hand.add(canBeGrabbedWeapon.get(index));
        canBeGrabbedWeapon.get(index).setReload(); //when i grab a weapon i've already paid its base effect
        return true;
    }
    public void dropWeaponCard(LinkedList<WeaponCard> hand){
        int index;
        for (index = 0; index < hand.size(); index++) {
            //WHEN A WEAPON IS CHOOSEN BREAK
        }
        hand.remove(index);
    }
    //_____________________________________GRAB OPTION TILE ___________________________________________________________//
    public boolean grabTile(Player player, CoordinatesWithRoom c){
        AmmoTile toBeGrabbedTile=new AmmoTile(RED,RED,RED);
        // grab ammo or powerUp

        //TODO a way to convert propose to grab cells in AmmoTile
      //  PowerUpCard toBeGrabbedPowerUp=null;
        for(int index = 0; index<toBeGrabbedTile.getAmmoCubes().size(); index++)
        {
            if(toBeGrabbedTile.getAmmoCubes().get(index).getCubeColor().equals(POWERUP))
                return (grabPowerUp(player,c)&&grabCube(player,c,toBeGrabbedTile));

        }
                    return grabCube(player,c, toBeGrabbedTile);
    }
    //____________________________ADDING CUBE______________________________________________________________________//
    public boolean grabCube(Player player, CoordinatesWithRoom c, AmmoTile a){

        for(int i=0;i<3;i++)
        {
            if(player.getAmmoBox()[i]>=3)
                return false;
        }
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
public boolean grabPowerUp(Player p, CoordinatesWithRoom c){
        if(!p.canGrabPowerUp()) return false;

        PowerUpCard power=new PowerUpCard();
        p.getPowerUp().add(power);
        //missing method to get PowerUpCard from deck
        return true;

}

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

    /*todo frenzyShoot frenzyRun frenzyGrab*/
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
///////////////////////////______________choose weapon & canPay_________________////////////////////////////////////////

    public WeaponCard chooseWeaponCard(LinkedList<WeaponCard> hand) {
        int j=0;
       // for (j = 0; j < hand.size(); j++) {
            //WHEN A WEAPON IS CHOOSEN....,then BREAK
            return hand.get(j);
     /*   }


    return null;*/}
///____________________________________canPayCard(TRUE if can pay base effect)____________________________________///

    public boolean canPayCard(WeaponCard weapon, Player player,PayOption option) {

        //todo ask pay option: only ammo or ammo+power-up
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

    public LinkedList<PowerUpCard>choosePowerUp(Player player){
        int j=0;
        LinkedList<PowerUpCard>power=player.getPowerUp();
        LinkedList<PowerUpCard>powerChoosen=new LinkedList<>();
        for (j = 0; j < power.size(); j++) {
            //when a card is choosen
            powerChoosen.add(power.get(j));
        }
        return powerChoosen;

    }

    //////////////////_____________________payMethods______________________________________________///////////////////////

    public List<EffectAndNumber> paidEffect(WeaponCard weapon, Player player,PayOption option) {

        switch(option){
            case AMMO: return payAmmo(player,weapon);
            case AMMOPOWER:return payAmmoPlusPowerUp(player,weapon,option);
            default:
        }
       return null; }
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


    }
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
    //____________________________getter and setter________________//
public boolean getEndturn(){
        return this.endTurn;
}


public void setEndTurn(boolean bool){this.endTurn=bool;}

//________________________GIVE POINTS_______& ENDOFTHEGAME___________________________//
public void canGetPoints(List<Player> victims,List<Player>allPlayers){
   List<Player> bestPlayerOrderForVictim;
        for(int indexVictims=0;indexVictims<victims.size();indexVictims++) {
                bestPlayerOrderForVictim=bestShooterOrder(allPlayers,victims.get(indexVictims));
                givePoints(victims.get(indexVictims),bestPlayerOrderForVictim);
                //call to give points
        }



}

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

    public boolean endOfTheGame(GameBoard g){  //every time a player dies
        //8||5 skulls
        //if(countSkull-1<=0)

        if(g.getNumSkull()<=0)
        return true;
        //else return false
        else return false;
    }

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
}


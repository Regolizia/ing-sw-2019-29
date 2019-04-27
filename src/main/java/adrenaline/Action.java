package adrenaline;


import adrenaline.weapons.RocketLaucher;

import java.util.LinkedList;

import static adrenaline.AmmoCube.CubeColor.YELLOW;


public class Action {
    final private int numMaxAlternativeOptions = 1; //(0=base 1=alternative)
    final private int numMaxAmmoToPay = 2;
    final private int numMaxWeaponYouCanHave=3;
    GameBoard g = new GameBoard();


    public static enum ActionType {
        GRAB, RUN, SHOOT, RELOAD;      //reload is an  optional action //ADRENALINESHOOT
    }

    public static enum PayOption {
        AMMO,AMMOPOWER,NONE;      //reload is an  optional action //ADRENALINESHOOT
    }

    private ActionType actionSelected;


    public Action(ActionType chosen, Player player, CoordinatesWithRoom c, GameBoard g, GameModel m,PayOption option) {
        actionSelected = chosen;
        boolean executedFirstAction=false;
        boolean executedSecondAction=false;
        boolean endTurn=false;
        boolean deletedAction=false;
        while(!endTurn) {
            switch (actionSelected) {
                case RUN:
                    // PROPOSE WHERE TO GO, SELECT ONE (with proposeCellsRun method)
                    proposeCellsRun(c, g);
                    CoordinatesWithRoom coordinatesR = null;
                    run(player, coordinatesR);
                    deletedAction=false;
                    break;
                case GRAB:
                    // PROPOSE CELL WHERE TO GRAB (EVERY CELL HAS SOMETHING) (DISTANCE 0-1 OR 0-1-2) (with proposeCellsGrab)
                    proposeCellsGrab(c, g, player);
                    CoordinatesWithRoom coordinatesG = null;
                    deletedAction=!(grab(player, coordinatesG, g,option));
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
                    LinkedList<EffectAndNumber> payEff = paidEffect(weapon, player);

                    shoot(weapon, c, player, payEff, m);
                    weapon.setNotReload();// i've lost base effect payment
                        deletedAction=false;
                         }
                    break;


                //rembember this action doesn't increment #action


                //IF RETURNS FALSE GO TO SELECT ACTION
                // selectedWeapon = getSelectedWeapon (THAT IS CHARGED, isLoaded method in Player)
                // choose EFFECTS AND ACCEPT PAYMENT FOR THEM

                // LIST OF EFFECTS CHOSEN (BASE ALREADY IN, IT HAS BEEN PAID (if they want ALT remove BASE))
                // EFFECTS ADDED TO EFFECTSLIST


        /* WHEN SOMEBODY CHOOSES ADRENALINE SHOOT WE ASK WHERE TO MOVE AND THEN WE DO THE STAFF TO SHOOT
        // IF ADRENALINE SHOOT IS POSSIBLE, CAN MOVE ONCE
        if(p.checkDamage()==2){
            listTemp.addAll(c.XTilesDistant(g,1));

            for(int i=0;i<listTemp.size();i++) {
                list.addAll(getPossibleTargetCells(listTemp.get(i),e,g));
                // TODO REMOVE DUPLICATES
                // TODO, WE HAVE TO CHECK IF PLAYER MOVED, AND THEN MOVE IT
            }
        }*/


                default:
                    //INVALID CHOICE

            }
            if(!executedFirstAction&&deletedAction==false) executedFirstAction=true;
            else if(executedFirstAction&&deletedAction==false) executedSecondAction=true;
            if(executedFirstAction&&executedSecondAction)endTurn=true;
        }
        //HERE ENDS TURN
        if(actionSelected.equals(ActionType.RELOAD)){
        LinkedList<WeaponCard> weaponList = player.getHand();
        WeaponCard weaponToReload = chooseWeaponCard(weaponList);
        if (!weaponToReload.getReload())
        reload(player, weaponToReload); }
        LinkedList<Player> players=m.getPlayers();
        //public LinkedList<Player> getPlayers()

        for(int index=0;index< players.size();index++)
        { if(players.get(index).isDead())
                players.get(index).newLife();
        }
    }


    //___________________________ PROPOSE CELL WHERE TO GO (DISTANCE 1-2-3)___________________________________________//
    public LinkedList<CoordinatesWithRoom> proposeCellsRun(CoordinatesWithRoom c, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.XTilesDistant(g, 1));
        list.addAll(c.XTilesDistant(g, 2));
        list.addAll(c.XTilesDistant(g, 3));
        return list;
    }

    //_______________________________________________RUN______________________________________________________________//
    public void run(Player p, CoordinatesWithRoom c) {

        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
    }

    //_______________ PROPOSE CELLS WHERE TO GRAB (DISTANCE 0-1 OR 0-1-2 IF ADRENALINE)_______________________________//
    public LinkedList<CoordinatesWithRoom> proposeCellsGrab(CoordinatesWithRoom c, GameBoard g, Player p) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.XTilesDistant(g, 1));
        list.add(c);

        // IF ADRENALINE GRAB IS POSSIBLE
        if (p.checkDamage() == 1) {
            list.addAll(c.XTilesDistant(g, 2));
        }
        return list;
    }

    //________________________________________________GRAB____________________________________________________________//
    public boolean grab(Player p, CoordinatesWithRoom c, GameBoard g,PayOption option) {
        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
        // IF THERE IS A SPAWNPOINT HERE
       if( c.getRoom()==p.getPlayerRoom()&&c.getX()==p.getPlayerPositionX()&&c.getY()==p.getPlayerPositionY()
            && c.getRoom().getSpawnpoints()!=null && c.getRoom().getSpawnpoints().get(0).getSpawnpointX()==c.getX()&&
       c.getRoom().getSpawnpoints().get(0).getSpawnpointY()== c.getY())
       {
        //CHOOSE WEAPON IF CANGRAB IT
        return   grabCard(p,p.getHand(),c,option);}
       else
        return grabTile(p,c);

    }

    //____________________________________________GRAB OPTIONS(WEAPON)________________________________________________________//

    public boolean grabCard(Player player,LinkedList<WeaponCard> hand, CoordinatesWithRoom c,PayOption option){
        int index;
        LinkedList <WeaponCard> canBeGrabbedWeapon=null;
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
        AmmoTile toBeGrabbedTile=null;
        // grab ammo or powerUp
        PowerUpCard toBeGrabbedPowerUp=null;
        if(toBeGrabbedTile.getAmmoTile().get(0)!=null&&toBeGrabbedTile.getAmmoTile().get(1)==null&&toBeGrabbedTile.getAmmoTile().get(2)==null)
            //is a powerUp
            return grabPowerUp(player,c,toBeGrabbedPowerUp);

        if (toBeGrabbedTile.getAmmoTile().get(1) != null || toBeGrabbedTile.getAmmoTile().get(2) != null )
                    return grabCube(player,c, toBeGrabbedTile);

        return false;

    }
    public boolean grabCube(Player player, CoordinatesWithRoom c, AmmoTile a){

        for(int i=0;i<3;i++)
        {
            if(player.getAmmoBox()[i]>=3)
                return false;
        }
        for(int i=1;i<3;i++)
        {
            switch (a.getAmmoTile().get(i).getCubeColor())

                {
                    case YELLOW: player.setCube(0,0,1);break;
                    case BLUE:   player.setCube(0,1,0);break;
                    case RED:    player.setCube(1,0,0);break;
                }
        }


        return true;
    }
public boolean grabPowerUp(Player p, CoordinatesWithRoom c,PowerUpCard a){
        if(!p.canGrabPowerUp()) return false;


        p.getPowerUp().add(a);
        return true;

}

    //______________________________________SHOOT_____________________________________________________________________//
    public void shoot(WeaponCard w, CoordinatesWithRoom c, Player p, LinkedList<EffectAndNumber> effectsList, GameModel m) {
        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
        EffectAndNumber effectNumber=null;
        for(int index=0;index<effectsList.size();index++) {
            ///todo choose effect order
            switch (effectsList.get(index).getEffect()) {
                case BASE: effectNumber = new EffectAndNumber(AmmoCube.Effect.BASE, 0);
                case ALT: effectNumber = new EffectAndNumber(AmmoCube.Effect.ALT, 0);
                case OP1: effectNumber = new EffectAndNumber(AmmoCube.Effect.OP1, 0);
                case OP2: effectNumber = new EffectAndNumber(AmmoCube.Effect.OP2, 0);
            }
            LinkedList<CoordinatesWithRoom> target= w.getPossibleTargetCells(c, effectNumber, g);
            w.fromCellsToTargets(target,c,g,p,m,effectNumber);
            //here controller gives back choosen opponents
            LinkedList<Object>effectiveTarget=null;
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
    public boolean reload(Player p, WeaponCard w) {
        int blue = p.getAmmoBox()[0];
        int red = p.getAmmoBox()[1];
        int yellow = p.getAmmoBox()[2];

        for (int i = 0; i < w.price.size(); i++) {
            if (w.price.get(i).getEffect() == AmmoCube.Effect.BASE) {
                if (w.price.get(i).getCubeColor() == AmmoCube.CubeColor.BLUE)
                    blue--;
                if (w.price.get(i).getCubeColor() == AmmoCube.CubeColor.RED)
                    red--;
                if (w.price.get(i).getCubeColor() == YELLOW)
                    yellow--;
            }
        }
        if (blue >= 0 && red >= 0 && yellow >= 0) {
            for (int i = 0; i < w.price.size(); i++) {
                if (w.price.get(i).getEffect() == AmmoCube.Effect.BASE) {
                    w.price.get(i).setPaid(true);
                }
            }
            p.getAmmoBox()[0] = blue;
            p.getAmmoBox()[1] = red;
            p.getAmmoBox()[2] = yellow;
            w.setReload();
            return true;
        } else {
            w.setNotReload();
        return false;}
    }

    /*todo frenzyShoot frenzyRun frenzyGrab*/


///////////////////////////______________choose weapon & canPay_________________////////////////////////////////////////

    public WeaponCard chooseWeaponCard(LinkedList<WeaponCard> hand) {
        int j;
        for (j = 0; j < hand.size(); j++) {
            //WHEN A WEAPON IS CHOOSEN BREAK
        }


        return hand.get(j);
    }
///____________________________________canPayCard(TRUE if can pay base effect)____________________________________///

    public boolean canPayCard(WeaponCard weapon, Player player,PayOption option) {

        //todo ask pay option: only ammo or ammo+power-up

        switch(option){

            case AMMOPOWER:{
                    return canPayAmmoPower(weapon,player);
                    }

            case AMMO:{
               return canPayAmmo(weapon,player,player.getCubeRed(),player.getCubeBlue(),player.getCubeYellow());
                }
        }
      return false;
    }
    ///////////////////////________________canPayOnlyCube______________________________________________________________________////////////////////////

    public boolean canPayAmmo(WeaponCard weapon, Player player, int red,int blue,int yellow) {
        LinkedList<AmmoCube> cost = weapon.getPrice();
        int i;
        boolean no = false;
        //can you pay base effect or you can pay alt
        for (i = 0; i < cost.size(); i++) {
            if (((cost.get(i).getEffect().equals(AmmoCube.Effect.BASE) || cost.get(i).getEffect().equals(AmmoCube.Effect.ALT)) && !weapon.getReload()
            ) || (((cost.get(i).getEffect().equals(AmmoCube.Effect.ALT))) && weapon.getReload())) {

                for (int j = 0; j < numMaxAmmoToPay; j++) {

                    if (cost.get(i).getEffect().equals(cost.get(j).getEffect())) {


                        switch (cost.get(i).getCubeColor()) {
                            case RED:
                                if (player.getCubeRed() - 1 < 0)
                                    no = true;
                                else no = false;
                                break;

                            case BLUE:
                                if (player.getCubeBlue() - 1 < 0)
                                    no = true;
                                else no = false;
                                break;

                            case YELLOW:
                                if (player.getCubeYellow() - 1 < 0)
                                    no = true;
                                else no = false;
                                break;
                        }

                        if (no == true && cost.get(i).getEffect().equals(AmmoCube.Effect.BASE) && !weapon.getReload())
                            return false;

                    }

                }

            }
        }
    return true;}

    ////////////////////___________________canPayAmmoPower pay bse effect with power up_____________________________________________________________//////////////

    public boolean canPayAmmoPower(WeaponCard weapon, Player player){
        LinkedList<PowerUpCard>powerUpCards=player.getPowerUp();
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
            }
        }
        int redCube=player.getCubeRed()+redPower;
        int blueCube=player.getCubeBlue()+bluePower;
        int yellowCube=player.getCubeYellow()+yellowPower;
        return canPayAmmo(weapon,player,redCube,blueCube,yellowCube);

    }

    public PowerUpCard choosePowerUp(Player player){
        int j=0;
        LinkedList<PowerUpCard>power=player.getPowerUp();
        for (j = 0; j < power.size(); j++) {
            //WHEN A power IS CHOOSEN BREAK
        }
        return power.get(j);

    }

    //////////////////_____________________payMethods______________________________________________///////////////////////

    public LinkedList<EffectAndNumber> paidEffect(WeaponCard weapon, Player player) {
        LinkedList<EffectAndNumber> paid = new LinkedList<>(null);
// if pay base don't psy alt
        int i = 0;
        int k=0;
        LinkedList<AmmoCube> cost = weapon.getPrice();
        for (i = 0; i < numMaxAlternativeOptions; i++) {        //missing choose your effect base/alt here you source option position
            for (int j = 0; j < numMaxAmmoToPay; j++) {
                if (cost.get(i).getEffect().equals(cost.get(j).getEffect())&&((cost.get(i).getEffect().equals(AmmoCube.Effect.BASE))||
                        (cost.get(i).getEffect().equals(AmmoCube.Effect.ALT)))&&!weapon.getReload()) {
                    pay(player, cost.get(j));
                }
                }
            paid.get(0).setEffect(cost.get(i).getEffect()); break;  // i can pay only one
            }
            //then you can pay options
        for (i = 0,k=1; i < weapon.getPrice().size(); i++,k++) {
            for (int j = 0; j < numMaxAmmoToPay; j++) {
                if (cost.get(i).getEffect().equals(cost.get(j).getEffect()) && (cost.get(j).getEffect() == AmmoCube.Effect.OP1 ||
                        cost.get(j).getEffect() == AmmoCube.Effect.OP2)) {
                    pay(player, cost.get(j));
                }


            }
            paid.get(k).setEffect(cost.get(i).getEffect());
        }

        return paid;}


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
                }
            }
    }




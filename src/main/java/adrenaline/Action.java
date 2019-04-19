package adrenaline;


import adrenaline.weapons.RocketLaucher;

import java.util.LinkedList;


public class Action {
    final private int numMaxAlternativeOptions = 1;
    final private int numMaxAmmoToPay = 2;
    final private int numMaxWeaponYouCanChoose=3;
    GameBoard g = new GameBoard();


    public static enum ActionType {
        GRAB, RUN, SHOOT, ADRENALINESHOOT;
    }

    private ActionType actionSelected;


    public Action(ActionType chosen, Player player, CoordinatesWithRoom c, GameBoard g, GameModel m) {
        actionSelected = chosen;
        boolean executedAction;
        switch (actionSelected) {
            case RUN:
                // PROPOSE WHERE TO GO, SELECT ONE (with proposeCellsRun method)
                proposeCellsRun(c,g);
                CoordinatesWithRoom coordinatesR=null;
                run(player,coordinatesR);
                break;
            case GRAB:
                // PROPOSE CELL WHERE TO GRAB (EVERY CELL HAS SOMETHING) (DISTANCE 0-1 OR 0-1-2) (with proposeCellsGrab)
                proposeCellsGrab(c,g,player);
                CoordinatesWithRoom coordinatesG=null;
                grab(player,coordinatesG,g);
                break;

            case SHOOT:
                // HERE JUST WEAPON AND PAYMENTS, EVERYTHING ELSE IN CORRECT WEAPONCARD

                // WeaponList player.checkWeapon() // GIVES ALL THE WEAPON OWNED BY THE PLAYER
                LinkedList<WeaponCard> hand = player.getHand();
                // WeaponCard chooseWeaponCard()// GIVES THE SELECTED WEAPON
                WeaponCard weapon = chooseWeaponCard(hand);
                // Boolean payCard()
                if (!canPayCard(weapon, player))
                    break;

                LinkedList<EffectAndNumber> payEff = paidEffect(weapon, player);

                shoot(weapon,c,player,payEff,m);
                break;

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
        p.setPlayerPosition(c.getX(), c.getY());
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
    public void grab(Player p, CoordinatesWithRoom c, GameBoard g) {
        // IF THERE IS A SPAWNPOINT HERE
       if( c.getRoom()==p.getPlayerRoom()&&c.getX()==p.getPlayerPositionX()&&c.getY()==p.getPlayerPositionY()
            && c.getRoom().getSpawnpoints()!=null && c.getRoom().getSpawnpoints().get(0).getSpawnpointX()==c.getX()&&
       c.getRoom().getSpawnpoints().get(0).getSpawnpointY()== c.getY())
       {   grabCard(p.getHand(),c);
        //CHOOSE WEAPON IF CANGRAB IT
        return;}
        AmmoTile toBeGrabbedTile=null;
        if( c.getRoom()==p.getPlayerRoom()&&c.getX()==p.getPlayerPositionX()&&c.getY()==p.getPlayerPositionY())
            toBeGrabbedTile=c.getRoom().getAmmoTile(c);  // now i can grab that tile
        // grab ammo or powerUp
        if(toBeGrabbedTile.getAmmoTile().get(0)!=null&&toBeGrabbedTile.getAmmoTile().get(1)==null&&toBeGrabbedTile.getAmmoTile().get(2)==null)
            //is a powerUp
            ;
        if(toBeGrabbedTile.getAmmoTile().get(1)!=null||toBeGrabbedTile.getAmmoTile().get(2)!=null)
            //is a ammo/cube
            ;
    }

    //____________________________________________GRAB OPTIONS________________________________________________________//

    public void grabCard(LinkedList<WeaponCard> hand, CoordinatesWithRoom c){
        int index;
        LinkedList <WeaponCard> canBeGrabbedWeapon=null;
        WeaponCard w;
        for(int i=0;i<numMaxWeaponYouCanChoose;i++){
            //here you get all the WeaponCard in that position
           // todo method to get weapon   canBeGrabbedWeapon.get(i)=w.getWeapon;
        }
        if(hand.size()>=3)
            dropWeaponCard(hand); //I need to drop that card

        for (index = 0; index <canBeGrabbedWeapon.size() ; index++) {
            //WHEN A WEAPON IS CHOOSEN BREAK
        }
        hand.add(canBeGrabbedWeapon.get(index));
    }
    public void dropWeaponCard(LinkedList<WeaponCard> hand){
        int index;
        for (index = 0; index < hand.size(); index++) {
            //WHEN A WEAPON IS CHOOSEN BREAK
        }
        hand.remove(index);
    }


    //______________________________________SHOOT_____________________________________________________________________//
    public void shoot(WeaponCard w, CoordinatesWithRoom c, Player p, LinkedList<EffectAndNumber> effectsList, GameModel m) {
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
    public boolean Reload(Player p, WeaponCard w) {
        int blue = p.getAmmoBox()[0];
        int red = p.getAmmoBox()[1];
        int yellow = p.getAmmoBox()[2];

        for (int i = 0; i < w.price.size(); i++) {
            if (w.price.get(i).getEffect() == AmmoCube.Effect.BASE) {
                if (w.price.get(i).getCubeColor() == AmmoCube.CubeColor.BLUE)
                    blue--;
                if (w.price.get(i).getCubeColor() == AmmoCube.CubeColor.RED)
                    red--;
                if (w.price.get(i).getCubeColor() == AmmoCube.CubeColor.YELLOW)
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
            return true;
        } else return false;
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


    public boolean canPayCard(WeaponCard weapon, Player player) {
        //can you pay base effect or you can pay alt
        LinkedList<AmmoCube> cost = weapon.getPrice();
        int i = 0;
        boolean no = false;
        for (i = 0; i < cost.size(); i++) {
            if (cost.get(i).getEffect().equals(AmmoCube.Effect.BASE) || cost.get(i).getEffect().equals(AmmoCube.Effect.ALT)) {
                for (int j = 0; j < numMaxAmmoToPay; j++) {
                    if (cost.get(i).getEffect().equals(cost.get(j).getEffect())) {
                        switch (cost.get(i).getCubeColor()) {
                            case RED:
                                if (player.getCubeRed(player) - 1 < 0)
                                    no = true;
                                else no = false;
                                break;

                            case BLUE:
                                if (player.getCubeBlue(player) - 1 < 0)
                                    no = true;
                                else no = false;
                                break;

                            case YELLOW:
                                if (player.getCubeYellow(player) - 1 < 0)
                                    no = true;
                                else no = false;
                                break;
                        }
                    }
                }
                if (no == false) break;
            }

        }
        if (no == true) //CANT PAY BASE NEITHER ALT
            return false;
        else return true;
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
                if (cost.get(i).getEffect().equals(cost.get(j).getEffect())) {
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
                        player.addRedCube(player, -1);
                        player.setCube(player);
                        break;

                    case BLUE:
                        player.addBlueCube(player, -1);
                        player.setCube(player);
                        break;

                    case YELLOW:
                        player.addYellowCube(player, -1);
                        player.setCube(player);
                        break;
                }
            }
    }




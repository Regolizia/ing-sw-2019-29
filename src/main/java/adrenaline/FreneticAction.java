package adrenaline;

import java.util.*;

/**
 * 
 */// only with frenzy mode and at the endOfTheGame
    //Players without damage turn over their board
    //every player gets a freneticTorn (2xFreneticAction)
    // look for the one with the first card
    // if a player plays before firstPlayer chooses an action
    // else chooses another action
public class FreneticAction extends Action {

    public FreneticAction(GameModel m){
        super(m);
    }

    public static enum PlayerOrder {
        FIRST,AFTER      //reload is an  optional action //ADRENALINESHOOT
    }

    public boolean selectFrenzyAction(ActionType actionSelected,Player player,CoordinatesWithRoom c,GameBoard g,GameModel m,PayOption paymentOption,PlayerOrder order){
/*FIRST
* move up to 4 squares
* move uo to 2 squares and grab
* move up to 1 squares reload and shoot*/
        /*AFTER
         * move up to 3 squares and grab
         * move up to 2 squares reload and shoot*/
switch (actionSelected){
    case RUN:CoordinatesWithRoom coordinatesR;
                if(order==PlayerOrder.FIRST)
                    coordinatesR=chooseCell(proposeCellsRunFrenzy(c,g));
                else
                    return false;
                run(player, coordinatesR);
                return true;
    case GRAB: CoordinatesWithRoom coordinatesG;
                    if(order==PlayerOrder.FIRST)
                        coordinatesG=chooseCell(proposeCellsGrabFrenzy(c, g, player));
                    else
                        coordinatesG=chooseCell(proposeCellsGrabFrenzy(c,g,player,order));
               if((grab(player, coordinatesG, g,paymentOption,m))){
                   player.setPlayerPosition(coordinatesG.getX(),coordinatesG.getY(),coordinatesG.getRoom());
                   return true;
               }
               else return false;

    case SHOOT: LinkedList<WeaponCard> hand = player.getHand();
                WeaponCard weapon = chooseWeaponCard(hand);

        if (weapon.getReload() == false) {
            if (!canPayCard(weapon, player,paymentOption))
               return false;
            
        }
        if(!canPayCard(weapon,player,paymentOption))
            return false;

        List<EffectAndNumber> payEff = paidEffect(weapon, player,paymentOption);
            if(payEff==null)
            {return false;}

            CoordinatesWithRoom cChoosen;
            if(order==PlayerOrder.FIRST)
                cChoosen=chooseCell(proposeCellsRunBeforeShootFrenzy(c,g));
            else
                cChoosen=chooseCell(proposeCellsRunBeforeShootFrenzy(c,g,order));
            player.setPlayerPosition(cChoosen.getX(),cChoosen.getY(),cChoosen.getRoom());

            shoot(weapon, cChoosen, player, payEff, m,g);

            weapon.setNotReload();// i've lost base effect payment

            return true;

        }

        return false;
    }



    public LinkedList<CoordinatesWithRoom> proposeCellsRunFrenzy(CoordinatesWithRoom c, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(g, 1));
        list.addAll(c.xTilesDistant(g, 2));
        list.addAll(c.xTilesDistant(g, 3));
        list.addAll(c.xTilesDistant(g,4));
        return list;
    }


    public LinkedList<CoordinatesWithRoom> proposeCellsGrabFrenzy(CoordinatesWithRoom c, GameBoard g, Player player){
            LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(g, 1));
            list.addAll(c.xTilesDistant(g,2));
            list.add(c);
            return list;

    }
    public LinkedList<CoordinatesWithRoom> proposeCellsGrabFrenzy(CoordinatesWithRoom c, GameBoard g, Player player,PlayerOrder order){
        LinkedList<CoordinatesWithRoom> list = proposeCellsGrabFrenzy(c,g,player);
        list.addAll(c.xTilesDistant(g,3));
        list.add(c);
        return list;

    }

    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShootFrenzy(CoordinatesWithRoom c,GameBoard g){
        LinkedList<CoordinatesWithRoom>list=new LinkedList<>(c.xTilesDistant(g,1));
        list.add(c);
        return list;
    }
    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShootFrenzy(CoordinatesWithRoom c,GameBoard g,PlayerOrder order){
        LinkedList<CoordinatesWithRoom>list=proposeCellsRunBeforeShootFrenzy(c,g);
        list.addAll(c.xTilesDistant(g,2));
        return list;
    }
}
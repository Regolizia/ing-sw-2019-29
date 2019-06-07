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


/*FIRST
* move up to 4 squares
* move uo to 2 squares and grab
* move up to 1 squares reload and shoot*/
        /*AFTER
         * move up to 3 squares and grab
         * move up to 2 squares reload and shoot*/

/**
 * proposeCellsRunFrenzy
 * this is proposeCellsRun frenzy variant
 * @param c :player's coordinates
 *          CONV: this action can be used by the players which order is before first player or by first player
 */
    public LinkedList<CoordinatesWithRoom> proposeCellsRunFrenzy(CoordinatesWithRoom c) {

        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 1));
        list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 2));
        list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 3));
        list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),4));
        return list;
    }

    /**
     * proposeCellsGrabFrenzy
     * this is proposeCellsGrab frenzy variant
     * @param c :player's coordinates
     * @param order :player's order, depends on first player
     *          CONV: this action can be used by all the players but change its effect, based on player's order
     */

    public LinkedList<CoordinatesWithRoom> proposeCellsGrabFrenzy(CoordinatesWithRoom c,PlayerOrder order){
            LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 1));
            list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),2));
            list.add(c);
            if(order.equals(PlayerOrder.AFTER))
                list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),3));
            return list;

    }

    /**
     * proposeCellsRunBeforeShootFrenzy
     * this is proposeCellsRunBeforeShoot frenzy variant
     * @param c :player's coordinates
     * @param order :player's order, depends on first player
     *          CONV: this action can be used by all the players but change its effect, based on player's order
     */
    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShootFrenzy(CoordinatesWithRoom c,PlayerOrder order){
        LinkedList<CoordinatesWithRoom>list=new LinkedList<>(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),1));
        list.add(c);
        if(order.equals(PlayerOrder.AFTER))
            list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),2));
        return list;
    }


}
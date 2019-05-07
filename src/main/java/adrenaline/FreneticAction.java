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

    public FreneticAction(){
        super();
    }


    public boolean selectFrenzyActionBeforFirstPlayer(ActionType actionSelected,Player player,CoordinatesWithRoom c,GameBoard g,GameModel m,PayOption paymentOption){


        return true;
    }

    public boolean selectFrenzyActionAfterFirstPlayer(ActionType actionSelected,Player player,CoordinatesWithRoom c, GameBoard g,GameModel m,PayOption paymentOption){
        //dopo è peggio



        return true;
    }
}
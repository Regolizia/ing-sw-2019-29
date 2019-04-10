package adrenaline;

import static adrenaline.GameModel.Bot.*;// it imports gameplay's bot choice
import static adrenaline.Action.ActionType.*;

public class Turn {
private boolean firstAction;
private boolean secondAction;
private GameModel model;
private Player pGoal;
private Spawnpoint sGoal;

Action action;
WeaponCard weapon;


    public Turn(){

    }

    public Turn(Player player){
        firstAction=true;
        secondAction=false;
    }

/*
    public void playerTurn(Player player, Action.ActionType actiontype, GameModel.Bot bot) {
        if (firstAction) {
            getAction(player, actiontype, bot);
            firstAction = false;
            secondAction = true;
        }
        if (secondAction) {
            getAction(player, actiontype, bot);
            secondAction = false;
        }

        System.out.println("recharge weapon?");
            action.reload(player);
        endTurn(player);

    }*/


    public void getAction(Player player, Action.ActionType actiontype, GameModel.Bot bot){
        if (firstAction && bot.equals(NOBOT))
        {
            switch(actiontype){


                case RUN: int x=0,y=0; //adding a method to choose the coordinates
                    //action.run(player,x,y);
                    break;

                case SHOOT: //weapon=player.getWeapon(player);
                    action.shoot(player, weapon);
                    break;

                case GRAB:
                    //action.grabHere(player);
                    //          action.grabThere( player);


                default: }
        }
        if (firstAction && bot.equals(BOT))
        {
            switch(actiontype){
                case RUN:
                    break;

                case SHOOT: //weapon=player.getWeapon(player);
                    action.shoot(player, weapon);
                    break;
                default: }
        }
    };








  public void endTurn(Player player){
       // player.nextPlayer(player); todo method to get next player
        replaceAmmoTiles();
        replaceWeapons();
    };

    public void replaceAmmoTiles(){}
    public void replaceWeapons(){}


    public void chooseCard(){}
    public void keepCard(){}//ex showCard()
    public void trowCard(){}

    public void giveMark(){}
    public void multipleKill(){}
    public void death(){}
    //public void kill(Player player,Player pGoal){}*/
}

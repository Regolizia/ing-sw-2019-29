package adrenaline;

import static adrenaline.GameModel.Bot.*;

public class Turn {
private boolean firstAction;
private boolean secondAction;
private GameModel model;
private Player pGoal;
private Spawnpoint sGoal;

Action action=new Action(){};
WeaponCard weapon=new WeaponCard(); //

    /**
     * Default constructor
     */
    public Turn() {
    }

    public Turn(Player player){
        firstAction=true;
        secondAction=false;
    }

        // it imports gameplay's bot choice
    public void playerTurn(Player player, Action action, GameModel.Bot bot) {
        if (firstAction) {
            getAction(player, action);
            firstAction = false;
            secondAction = true;
        }
        if (secondAction) {
            getAction(player, action);
            secondAction = false;
        }
        if (!secondAction && !firstAction) {
            if (bot.equals(NOBOT)) {
            }

            //endTurn(player);

            if (bot.equals(BOT)) {
            }

            //terminator's actions

            //endTurn(player);
        }

    }


    public void getAction(Player player, Action action){
        if (firstAction)
        {
            switch(action.getName()){

                case "reload": action.reload(player);
                                break;

                case "attack": //weapon=player.getWeapon(player);
                        action.shoot(player,weapon);
                                break;

                case "grab": action.grabHere(player);
                                action.grabThere( player);


                case"adrenaline.Run": action.run(player);
                                break;
                default: }
        }
    };
    public void getBotLastAction(){}







/*
    public void endTurn(Player player){
        action.
        //player=model.nextPlayer(player);
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
    public void kill(Player player,Player pGoal){}*/
}

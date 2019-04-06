package adrenaline;

public class Turn {
private boolean firstAction;
private boolean secondAction;
private GameModel model;
private Player pGoal;
private Spawnpoint sGoal;

Action action=new Action(){};
WeaponCard weapon=new WeaponCard();

    /**
     * Default constructor
     */
    public Turn() {
    }

    public Turn(Player player){
        firstAction=true;
        secondAction=false;
    }

    public void playerTurn(Player player, Action action, boolean terminator){
        if (firstAction==true)
        {
            getAction(player,action);
            firstAction=false; secondAction=true;
        }
        if (secondAction==true){
            getAction(player,action);
            secondAction=false;
        }
        if(secondAction==false&&firstAction==false&&!terminator)
            //endTurn(player);
        if(secondAction==false&&firstAction==false&&terminator)
        {
            //terminator's actions

            //endTurn(player);
        }

    }


    public void getAction(Player player, Action action){
        if (firstAction==true)
        {
            switch(action.getName()){

                case "reload": action.reload(player);
                                break;

                case "attack": //weapon=player.getWeapon(player);
                        action.shoot(player,weapon);
                                break;

                case "grab": action.grabHere(player);
                                action.grabThere( player);


                case"adrenaline.Run":action.run(player);
                                break;
                default: }
        }
    };
    public void getUltimaAzioneTerminator(){}







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

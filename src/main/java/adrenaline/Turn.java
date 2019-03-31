package adrenaline;

public class Turn {
private boolean firstAction;
private boolean secondAction;
private GameModel model;
private Player pGoal;
private Spawnpoint sGoal;


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
            endTurn(player);
        if(secondAction==false&&firstAction==false&&terminator)
        {
            //terminator's actions

            endTurn(player);
        }

    }


    public void getAction(Player player, Action action){
        if (firstAction==true)
        {
            switch(action.getName()){

                case "reload": reload(player);
                                break;

                case "attack": attack(player);
                                break;

                case "pick up weapon":pickUpWeapon();
                                break;

                case "pick up power-up":pickUpPower();
                                break;

                case"run":run(player);
                                break;
                default: }
        }
    };
    public void getUltimaAzioneTerminator(){}
    public void reload(Player player){}
    //human target
    public void attack(Player player){
        pGoal=getTarget(player);
        if(pGoal.getLife(pGoal)==0)
            kill(player,pGoal);
    }
    //adrenaline.Spawnpoint target
    public void attackSpawn(Player player){
        sGoal=getTargetSpawn(player);
        if(sGoal.getLife(sGoal)==0)
            sGoal.conquer(player,sGoal);
    }


    public Player getTarget(Player player){

        //todo check on distance
        //todo multiple target , override???
        Player target=new Player();

        return target;
    }


    public Spawnpoint getTargetSpawn(Player player){

        //todo check on distance
        //todo multiple target
        Spawnpoint target=new Spawnpoint();

        return target;
    }


    public void endTurn(Player player){
        reload(player);
        player=model.nextPlayer(player);
        replaceAmmoTiles();
        replaceWeapons();
    };
    public void replaceAmmoTiles(){}
    public void replaceWeapons(){}
    public void pickUpPower(){ }
    public void pickUpWeapon(){}
    public void chooseCard(){}
    public void keepCard(){}//ex showCard()
    public void trowCard(){}
    public void run(Player player){}
    public void giveMark(){}
    public void multipleKill(){}
    public void death(){}
    public void kill(Player player,Player pGoal){}
}

public class Turn {
protected boolean firstAction;
    protected boolean secondAction;
    Player pGoal;
    Spawnpoint sGoal;



    public Turn(Player player){
        firstAction=true;
        secondAction=false;
    }


    public void getAction(Player player, Action action){
        if (firstAction==true)
        {
            switch(action.getName()){

                case "reload": reload(player);
                                break;

                case "attack": attack(player);
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
    //Spawnpoint target
    public void attackSpawn(Player player){
        sGoal=getTargetSpawn(player);
        if(sGoal.getLife(sGoal)==0)
            sGoal.conquer(player,sGoal);
    }


    public Player getTarget(Player player){

        //todo check on distance
        //todo multiple target
        Player target=new Player();

        return target;
    }


    public Spawnpoint getTargetSpawn(Player player){

        //todo check on distance
        //todo multiple target
        Spawnpoint target=new Spawnpoint();

        return target;
    }



    public void replaceAmmoTiles(){}
    public void replaceWeapons(){}
    public void pickUpPowerUp(){}
    public void chooseCard(){}
    public void keepCard(){}//ex showCard()
    public void trowCard(){}
    public void recharge(){}
    public void giveMark(){}
    public void multipleKill(){}
    public void death(){}
    public void kill(Player player,Player pGoal){}
}

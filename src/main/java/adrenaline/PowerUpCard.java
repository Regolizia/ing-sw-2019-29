package adrenaline;

public class PowerUpCard extends Card {

    private AmmoCube.CubeColor color;
    private boolean canBeUsedOnBot;
    public PowerUpCard(){

    }

    public PowerUpCard(AmmoCube.CubeColor c){

        this.color = c;
    }


    public AmmoCube.CubeColor getPowerUpColor(){
                return this.color;
    }
    public void setPowerUpColor(AmmoCube.CubeColor c){
        this.color = c;
    }
    /**
     *getCanBeUsedOnBot
     * @return if a PowerUp can be used on bot
     **/
    public boolean getCanBeUsedOnBot() {
        return canBeUsedOnBot;
    }
    /**
     *setCanBeUsedOnBot
     * @param canBeUsedOnBot
     **/
    public void setCanBeUsedOnBot(boolean canBeUsedOnBot) {
        this.canBeUsedOnBot = canBeUsedOnBot;
    }

    public void plusOneDamage(Player player,Object target){
        if(target instanceof Player)
            ((Player) target).damageByShooter(player);

    }
    //useless
    //public void setColor(PowerUpCard card, Color c){
      //  this.color = c ;}


}

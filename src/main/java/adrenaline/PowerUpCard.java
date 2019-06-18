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

    public boolean getCanBeUsedOnBot() {
        return canBeUsedOnBot;
    }

    public void setCanBeUsedOnBot(boolean canBeUsedOnBot) {
        this.canBeUsedOnBot = canBeUsedOnBot;
    }


    //useless
    //public void setColor(PowerUpCard card, Color c){
      //  this.color = c ;}


}

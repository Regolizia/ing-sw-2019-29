package adrenaline;

public class PowerUpCard extends Card {

    private AmmoCube.CubeColor color;

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

    //useless
    //public void setColor(PowerUpCard card, Color c){
      //  this.color = c ;}


}

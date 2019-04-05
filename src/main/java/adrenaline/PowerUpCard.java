package adrenaline;

public class PowerUpCard extends Card {

    public AmmoCube.CubeColor color;

    public PowerUpCard(){

    }

    public PowerUpCard(AmmoCube.CubeColor c){
        this.color = c;
    }


    public AmmoCube.CubeColor getColor(){
        return this.color;
}

    //useless
    //public void setColor(PowerUpCard card, Color c){
      //  this.color = c ;}


}

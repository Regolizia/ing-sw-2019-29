package adrenaline;

public class PowerUpCard {

    public static enum CubeColor {
        RED, YELLOW, BLUE;
    }
    public CubeColor color;


    public PowerUpCard(){

    }

    public PowerUpCard(CubeColor c){
        this.color = c;
    }


    public CubeColor getColor(){
        return this.color;
}

    //useless
    //public void setColor(PowerUpCard card, Color c){
      //  this.color = c ;}


}

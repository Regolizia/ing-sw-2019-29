package adrenaline;

public class PowerUpCard {
    public enum PColor {
        RED, YELLOW, GREEN
    }
    public PColor color;


    public PowerUpCard(){

    }
    public PowerUpCard(Color c){
        this.color = c;
    }


    public Color getColor(){
        return this.color;
}

    //useless
    //public void setColor(PowerUpCard card, Color c){
      //  this.color = c ;}


}

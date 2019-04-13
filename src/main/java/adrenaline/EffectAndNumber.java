package adrenaline;

// USED TO KNOW HOW MANY TARGETS FOR EVERY EFFECT
public class EffectAndNumber {

    private AmmoCube.Effect effect;
    private int number;

    public EffectAndNumber(AmmoCube.Effect e, int i){
        this.effect = e;
        this.number = i;
    }

    public AmmoCube.Effect getEffect(){
        return  effect;
    }
    public int getNumber(){
        return number;
    }

    public void setEffect(AmmoCube.Effect e){
        this.effect = e;
    }
    public void setNumber(int x){
        this.number = x;
    }
}


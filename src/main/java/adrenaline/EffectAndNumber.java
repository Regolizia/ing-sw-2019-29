package adrenaline;

// USED TO KNOW HOW MANY TARGETS FOR EVERY EFFECT
public class EffectAndNumber {

    private AmmoCube.Effect effect;
    private int targetsNumber;

    public EffectAndNumber(AmmoCube.Effect e, int i){
        this.effect = e;
        this.targetsNumber = i;
    }

    public AmmoCube.Effect getEffect(){
        return  effect;
    }
    public int getTargetsNumber(){
        return targetsNumber;
    }

    public void setEffect(AmmoCube.Effect e){
        this.effect = e;
    }
    public void setTargetsNumber(int x){
        this.targetsNumber = x;
    }
}

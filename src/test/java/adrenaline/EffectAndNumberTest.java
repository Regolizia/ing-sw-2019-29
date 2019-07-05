package adrenaline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectAndNumberTest {
    private EffectAndNumber effectAndNumber;

 /*   @BeforeEach
    void setUp() {
        effectAndNumber = new EffectAndNumber(AmmoCube.Effect.BASE, 4);
    }

    @Test
    void setEffect() {
        effectAndNumber.setEffect(AmmoCube.Effect.ALT);
        assertEquals(AmmoCube.Effect.ALT,effectAndNumber.getEffect());
    }*/
 /*test creation of EffectAndNumber, Adding AmmoCube.Effect, setEffect and addEffect plus setNumber*/
 @Test
 void constructor(){
     effectAndNumber=new EffectAndNumber(AmmoCube.Effect.BASE,1);
     effectAndNumber.setEffect(AmmoCube.Effect.BASE);
     effectAndNumber.setNumber(1);
     effectAndNumber.getEffect();
     effectAndNumber.getNumber();

 }
}
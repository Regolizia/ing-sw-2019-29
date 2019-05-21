package adrenaline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectAndNumberTest {
    private EffectAndNumber effectAndNumber;

    @BeforeEach
    void setUp() {
        effectAndNumber = new EffectAndNumber(AmmoCube.Effect.BASE, 4);
    }

    @Test
    void setEffect() {
        effectAndNumber.setEffect(AmmoCube.Effect.ALT);
        assertEquals(AmmoCube.Effect.ALT,effectAndNumber.getEffect());
    }
}
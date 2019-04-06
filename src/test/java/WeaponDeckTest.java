import adrenaline.*;
import adrenaline.weapons.*;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeaponDeckTest {


    @Test
    public void testConstructor() {

        WeaponDeck deckTest = new WeaponDeck();

        int sum =0;

        int Cyb = 0;
        int Ele = 0;
        int Fla = 0;
        int Fur = 0;
        int Gre = 0;
        int Hea = 0;
        int Hel = 0;
        int Loc = 0;
        int Mac = 0;
        int Pla = 0;
        int Pow = 0;
        int Rai = 0;
        int Roc = 0;
        int Shoc = 0;
        int Shot = 0;
        int Sle = 0;
        int Tho = 0;
        int Tra = 0;
        int Vor = 0;
        int Whi = 0;
        int Zx = 0;


        for(int i =0;i<=20;i++){
            if(deckTest.deck.get(i) instanceof Cyberblade)
                Cyb++;
            if(deckTest.deck.get(i) instanceof Electroscythe)
                Ele++;
            if(deckTest.deck.get(i) instanceof Flamethrower)
                Fla++;
            if(deckTest.deck.get(i) instanceof Furnace)
                Fur++;
            if(deckTest.deck.get(i) instanceof GrenadeLaucher)
                Gre++;
            if(deckTest.deck.get(i) instanceof Heatseeker)
                Hea++;
            if(deckTest.deck.get(i) instanceof Hellion)
                Hel++;
            if(deckTest.deck.get(i) instanceof LockRifle)
                Loc++;
            if(deckTest.deck.get(i) instanceof MachineGun)
                Mac++;
            if(deckTest.deck.get(i) instanceof PlasmaGun)
                Pla++;
            if(deckTest.deck.get(i) instanceof PowerGlove)
                Pow++;
            if(deckTest.deck.get(i) instanceof Railgun)
                Rai++;
            if(deckTest.deck.get(i) instanceof RocketLaucher)
                Roc++;
            if(deckTest.deck.get(i) instanceof Shockwave)
                Shoc++;
            if(deckTest.deck.get(i) instanceof Shotgun)
                Shot++;
            if(deckTest.deck.get(i) instanceof Sledgehammer)
                Sle++;
            if(deckTest.deck.get(i) instanceof Thor)
                Tho++;
            if(deckTest.deck.get(i) instanceof TractorBeam)
                Tra++;
            if(deckTest.deck.get(i) instanceof VortexCannon)
                Vor++;
            if(deckTest.deck.get(i) instanceof Whisper)
                Whi++;
            if(deckTest.deck.get(i) instanceof Zx_2)
                Zx++;
        }

            assertTrue(1 == Cyb);
            assertTrue(1 == Ele);
            assertTrue(1 == Fla);
            assertTrue(1 == Fur);
            assertTrue(1 == Gre);
            assertTrue(1 == Hea);
            assertTrue(1 == Hel);
            assertTrue(1 == Loc);
            assertTrue(1 == Mac);
            assertTrue(1 == Pla);
            assertTrue(1 == Pow);
            assertTrue(1 == Rai);
            assertTrue(1 == Roc);
            assertTrue(1 == Shoc);
            assertTrue(1 == Shot);
            assertTrue(1 == Sle);
            assertTrue(1 == Tho);
            assertTrue(1 == Tra);
            assertTrue(1 == Vor);
            assertTrue(1 == Whi);
            assertTrue(1 == Zx);
    }
    }


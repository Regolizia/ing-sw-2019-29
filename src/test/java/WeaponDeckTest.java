import adrenaline.*;
import adrenaline.weapons.*;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeaponDeckTest {


    @Test
    public void testConstructor() {

        Deck deckClass = new Deck();
        WeaponDeck deckTest = new WeaponDeck();
        deckClass.shuffleCards(deckTest.getList());

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
            if(deckTest.getList().get(i) instanceof Cyberblade)
                Cyb++;
            if(deckTest.getList().get(i) instanceof Electroscythe)
                Ele++;
            if(deckTest.getList().get(i) instanceof Flamethrower)
                Fla++;
            if(deckTest.getList().get(i) instanceof Furnace)
                Fur++;
            if(deckTest.getList().get(i) instanceof GrenadeLaucher)
                Gre++;
            if(deckTest.getList().get(i) instanceof Heatseeker)
                Hea++;
            if(deckTest.getList().get(i) instanceof Hellion)
                Hel++;
            if(deckTest.getList().get(i) instanceof LockRifle)
                Loc++;
            if(deckTest.getList().get(i) instanceof MachineGun)
                Mac++;
            if(deckTest.getList().get(i) instanceof PlasmaGun)
                Pla++;
            if(deckTest.getList().get(i) instanceof PowerGlove)
                Pow++;
            if(deckTest.getList().get(i) instanceof Railgun)
                Rai++;
            if(deckTest.getList().get(i) instanceof RocketLaucher)
                Roc++;
            if(deckTest.getList().get(i) instanceof Shockwave)
                Shoc++;
            if(deckTest.getList().get(i) instanceof Shotgun)
                Shot++;
            if(deckTest.getList().get(i) instanceof Sledgehammer)
                Sle++;
            if(deckTest.getList().get(i) instanceof Thor)
                Tho++;
            if(deckTest.getList().get(i) instanceof TractorBeam)
                Tra++;
            if(deckTest.getList().get(i) instanceof VortexCannon)
                Vor++;
            if(deckTest.getList().get(i) instanceof Whisper)
                Whi++;
            if(deckTest.getList().get(i) instanceof Zx_2)
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


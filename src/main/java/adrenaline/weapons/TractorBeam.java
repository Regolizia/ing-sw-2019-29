package adrenaline.weapons;

import adrenaline.AmmoCube;
import adrenaline.EffectAndNumber;
import adrenaline.Player;
import adrenaline.WeaponCard;

import java.util.LinkedList;

/**
 * 
 */
public class TractorBeam extends WeaponCard {

    /**
     * Default constructor
     */
    public TractorBeam() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.ALT,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.ALT,false));

    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {
            case BASE:
                break;

            case OP1:
                break;

            case OP2:
                break;

            case ALT:
                break;


        }

    }

}
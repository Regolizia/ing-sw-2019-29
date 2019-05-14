package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class Sledgehammer extends WeaponCard {

    /**
     * Default constructor
     */
    public Sledgehammer() {
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.ALT,false));
    }

    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        list.add(c);
        return list;
    }

    @Override
    public List<Object> fromCellsToTargets(List<CoordinatesWithRoom> list, CoordinatesWithRoom c, GameBoard g, Player p, GameModel m, EffectAndNumber en) {
        List<Object> targets = super.fromCellsToTargets(list, c, g, p, m, en);
        // ASK WHICH TARGET TO DAMAGE, REMOVE OTHERS
        //IF ALT MODE MOVE THE TARGET
        return targets;
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

      /*  switch (e.getEffect()) {
            case BASE:  // 2 DAMAGE, 1 TARGET, (todo YOUR SQUARE control in possible target)
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                }

            case ALT:   // 3 DAMAGE, 1 TARGET, YOUR SQUARE, THEN TODO MOVE TARGET 0-1-2
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    if(e.getEffect()== AmmoCube.Effect.ALT) // intendi sei in base finchè
                        // non hai pagato il prezzo alternativa perchè se è cosi li mergiamo
                        i++;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                }
*/
         if(targetList.get(0) instanceof Player&&(e.getEffect()==AmmoCube.Effect.ALT)||(e.getEffect()==AmmoCube.Effect.BASE)) {
            int i =((Player) targetList.get(0)).marksByShooter(p);
            i=i+2;
            if(e.getEffect()== AmmoCube.Effect.ALT)
            {i++; /* move target*/}
            ((Player) targetList.get(0)).addDamageToTrack(p,i);
        }
                else {
                    // DAMAGE SPAWNPOINT
                }
               // break;

      //  }
    }
}

//possibile merge :
/*
*
*  if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+2;
                    if(e.getEffect()== AmmoCube.Effect.ALT)
                    {i++ e poi move } a mio avviso non necessitiamo di due case le due opzioni sono molto simili
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);
                }

                else {
                    // DAMAGE SPAWNPOINT
                }
*
*
*
* */
package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class Furnace extends WeaponCard {

    /**
     * Default constructor
     */
    public Furnace() {
        price.add(new AmmoCube(AmmoCube.CubeColor.RED, AmmoCube.Effect.BASE, true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE, false));
    }

    public boolean canShootBase(){
        return true;
    }
    public boolean canShootAlt(){
        return true;
    }

    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        List<CoordinatesWithRoom> list = new LinkedList<>();

        if(en.getEffect()== AmmoCube.Effect.BASE) {

            int x = c.getRoom().getRoomSizeX();
            int y = c.getRoom().getRoomSizeY();

            for (int i = 1; i <= x; i++) {
                for (int j = 1; j <= y; j++) {
                    list.add(new CoordinatesWithRoom(i, j, c.getRoom()));
                }
            }
            return list;
        }
        else{
            list = c.oneTileDistant(g, false);

            return list;
        }
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

        switch (e.getEffect()) {

            case BASE:  // 1 DAMAGE, EVERY PLAYER
            case ALT:   // 1 DAMAGE, 1 MARK, EVERY PLAYER
                for (Object o : targetList) {
                    if (o instanceof Player) {
                        int i = ((Player)o).marksByShooter(p);
                        i++;

                        ((Player)o).addDamageToTrack(p, i);

                        if (e.getEffect() == AmmoCube.Effect.ALT) {
                            ((Player)o).addMarks(p, 1);

                        }
                    } else {
                        // DAMAGE SPAWNPOINT
                    }

                }
                break;

        }
    }
    @Override
    public String toString() {
        return "Furnace";
    }
}
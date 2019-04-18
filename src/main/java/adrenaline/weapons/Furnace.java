package adrenaline.weapons;

import adrenaline.*;

import java.util.LinkedList;

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
        price.add(new AmmoCube(AmmoCube.CubeColor.FREE, AmmoCube.Effect.ALT, true));
        // DON'T CARE IF ALT IS PAID OR NOT BECAUSE IT'S FREE (IF YOU PAY BASE YOU ALREADY HAVE IT
        // BUT IT'S DIFFERENT FROM OP BECAUSE YOU CAN HAVE BASE OR ALT NOT BOTH)
    }

    @Override
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        if(en.getEffect()== AmmoCube.Effect.BASE) {

            LinkedList<Room> possibleRooms = new LinkedList<>();

            int x = c.getRoom().getRoomSizeX();
            int y = c.getRoom().getRoomSizeY();

            for (int k = 0; k < g.getDoors().size(); k++) {
                if (c.getX() == g.getDoors().get(k).getCoordinates1().getX() &&
                        c.getY() == g.getDoors().get(k).getCoordinates1().getY() &&
                        c.getRoom().getToken() == g.getDoors().get(k).getCoordinates1().getRoom().getToken()) {

                    possibleRooms.add(g.getDoors().get(k).getCoordinates2().getRoom());
                }

                if (c.getX() == g.getDoors().get(k).getCoordinates2().getX() &&
                        c.getY() == g.getDoors().get(k).getCoordinates2().getY() &&
                        c.getRoom().getToken() == g.getDoors().get(k).getCoordinates2().getRoom().getToken()) {

                    possibleRooms.add(g.getDoors().get(k).getCoordinates1().getRoom());

                }
            }

            ///// ASK PLAYER FOR A CELL!!!!!!
            // SEND PLAYER LIST OF ROOMS possibleRooms
            /// get index or something about that room

            int u = 0 //= possibleRooms.get(i).getRoomSizeX()
                    ;
            int q = 0 //= possibleRooms.get(i).getRoomSizeY()
                    ;

            for (int i = 1; i <= u; i++) {
                for (int j = 1; j <= q; j++) {
                    list.add(new CoordinatesWithRoom(i, j, possibleRooms.get(i)));
                }
            }
            return list;
        }
        else{
            list = c.oneTileDistant(g, false);

            //// ASK PLAYER WHICH TILE, GET ONE BACK IN THAT LIST

            return list;
        }
    }

    @Override
    public void applyDamage(LinkedList<Object> targetList, Player p, EffectAndNumber e) {

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
}
package adrenaline.weapons;

import adrenaline.*;
import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.Furnace;
import adrenaline.weapons.LockRifle;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.LinkedList;
import java.util.List;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class FurnaceTest {
private Furnace furnace;
        // NO PAYMENT, NO SPAWNPOINTS, NO CELLS METHOD (REQUIRES CLI)

        @Test
        public void testConstructor() {
            furnace=new Furnace();
            GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1,false);
            CoordinatesWithRoom c1 = new CoordinatesWithRoom(2, 1, m.getMapUsed().getGameBoard().getRoom(0));
            CoordinatesWithRoom c2 = new CoordinatesWithRoom(2, 1, m.getMapUsed().getGameBoard().getRoom(1));
            CoordinatesWithRoom c3 = new CoordinatesWithRoom(3, 1, m.getMapUsed().getGameBoard().getRoom(1));
            CoordinatesWithRoom c4 = new CoordinatesWithRoom(2, 1, m.getMapUsed().getGameBoard().getRoom(0));

            CoordinatesWithRoom c5 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));


            m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
            m.addPlayer(new Player(c2, Figure.PlayerColor.BLUE));
            m.addPlayer(new Player(c3, Figure.PlayerColor.PURPLE));
            m.addPlayer(new Player(c4, Figure.PlayerColor.GRAY));

            m.addPlayer(new Player(c5, Figure.PlayerColor.YELLOW)); // SHOOTER

            m.getPlayers().get(4).getHand().add(new Furnace());
            EffectAndNumber enBase = new EffectAndNumber(AmmoCube.Effect.BASE, 0);
            EffectAndNumber enAlt = new EffectAndNumber(AmmoCube.Effect.ALT, 0);
/*
            LinkedList<CoordinatesWithRoom> list = m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5, enBase, m.getMapUsed().getGameBoard());
            for (CoordinatesWithRoom c : list) {
                System.out.println(c);
            }
            */


            // FAKE CELL METHOD, I CHOSE ROOM 1
            LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
            int u = m.getMapUsed().getGameBoard().getRoom(1).getRoomSizeX();
            int q = m.getMapUsed().getGameBoard().getRoom(1).getRoomSizeY();

            for (int i = 1; i <= u; i++) {
                for (int j = 1; j <= q; j++) {
                    list.add(new CoordinatesWithRoom(i, j, m.getMapUsed().getGameBoard().getRoom(1)));
                }
            }
            //

            List<Object> targets = m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list, c5, m.getMapUsed().getGameBoard(), m.getPlayers().get(4), m, enBase);
            for (Object o : targets) {
                System.out.println(o);
            }

            // BASE EFFECT

            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(targets, m.getPlayers().get(4), enBase);
            System.out.printf("\n\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4))==1);
            assertTrue(m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4))==1);

          // ALT EFFECT
            List<CoordinatesWithRoom> list2 = m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5, enAlt, m.getMapUsed().getGameBoard());

            list2.remove(1);

            List<Object> targets2 = m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list2, c5, m.getMapUsed().getGameBoard(), m.getPlayers().get(4), m, enAlt);
            for (Object o : targets2) {
                System.out.println(o);
            }

            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(3).marksByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(targets2, m.getPlayers().get(4), enAlt);
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(3).marksByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4))==1);
            assertTrue(m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4))==1);
            furnace.canShootAlt();
            furnace.canShootBase();
            furnace.toString();
            furnace.getPossibleTargetCells(c1,new EffectAndNumber(AmmoCube.Effect.BASE,0),m.getMapUsed().getGameBoard());
        }

    }

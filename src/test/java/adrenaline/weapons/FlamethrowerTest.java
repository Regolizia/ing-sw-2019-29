package adrenaline.weapons;

import adrenaline.*;
    import adrenaline.weapons.Flamethrower;
    import adrenaline.weapons.LockRifle;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.LinkedList;
    import java.util.List;

    import static adrenaline.GameModel.Mode.DEATHMATCH;

    public class FlamethrowerTest {


        // NO PAYMENT, NO SPAWNPOINTS
        private Flamethrower flamethrower;
        @Test
        public void testConstructor() {
            flamethrower=new Flamethrower();
            GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 3,false);
            CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(4));
            CoordinatesWithRoom c2 = new CoordinatesWithRoom(1, 2, m.getMapUsed().getGameBoard().getRoom(3));
            CoordinatesWithRoom c3 = new CoordinatesWithRoom(1, 2, m.getMapUsed().getGameBoard().getRoom(3));
            CoordinatesWithRoom c4 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(4));

            CoordinatesWithRoom c5 = new CoordinatesWithRoom(2, 2, m.getMapUsed().getGameBoard().getRoom(3));
            //CoordinatesWithRoom c5 = new CoordinatesWithRoom(3, 1, m.getMapUsed().getGameBoard().getRoom(0));


            m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
            m.addPlayer(new Player(c2, Figure.PlayerColor.BLUE));
            m.addPlayer(new Player(c3, Figure.PlayerColor.PURPLE));
            m.addPlayer(new Player(c4, Figure.PlayerColor.GRAY));

            m.addPlayer(new Player(c5, Figure.PlayerColor.YELLOW)); // SHOOTER

            m.getPlayers().get(4).getHand().add(new Flamethrower());
            EffectAndNumber enBase = new EffectAndNumber(AmmoCube.Effect.BASE, 0);
            EffectAndNumber enAlt = new EffectAndNumber(AmmoCube.Effect.ALT, 0);

            List<CoordinatesWithRoom> list = m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5, enBase, m.getMapUsed().getGameBoard());
            for (CoordinatesWithRoom c : list) {
                System.out.println(c);
            }

            System.out.print(list.size());
            List<Object> targets = m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list, c5, m.getMapUsed().getGameBoard(), m.getPlayers().get(4), m, enBase);
            for (Object o : targets) {
                System.out.println(o);
            }


            // I CHOOSE BLUE TARGET AND GREEN TARGET, BASE EFFECT
            LinkedList<Object> temp = new LinkedList<>();
            temp.add(targets.get(0));
            temp.add(targets.get(1));

            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(1).marksByShooter(m.getPlayers().get(4)));

            m.getPlayers().get(4).getHand().get(0).applyDamage(temp, m.getPlayers().get(4), enBase);

            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)) == 1);
            assertTrue(m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)) == 0);
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(1).marksByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)) == 1);
            assertTrue(m.getPlayers().get(1).marksByShooter(m.getPlayers().get(4)) == 0);


            // IF I USE ALT ON 1,2 R3 AND 1,1 R4
            List<CoordinatesWithRoom> listTwo = m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5, enBase, m.getMapUsed().getGameBoard());
            listTwo.remove(1);
            listTwo.remove(1);
            for (CoordinatesWithRoom c : listTwo) {
                System.out.println(c);
            }

            List<Object> targetsTwo = m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(listTwo, c5, m.getMapUsed().getGameBoard(), m.getPlayers().get(4), m, enAlt);
            for (Object o : targetsTwo) {
                System.out.println(o);
            }


            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));

            m.getPlayers().get(4).getHand().get(0).applyDamage(targetsTwo, m.getPlayers().get(4), enAlt);

            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)) == 3);  // FIRST SQUARE
            //assertTrue(m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)) == 2);  // SECOND SQUARE
            flamethrower.canShootAlt();
            flamethrower.canShootBase();
            flamethrower.toString();
        }


        }




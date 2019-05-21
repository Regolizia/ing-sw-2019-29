package adrenaline.weapons;

import adrenaline.*;
import adrenaline.weapons.Electroscythe;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.LinkedList;
import java.util.List;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class ElectroscytheTest {

    // NO PAYMENT, NO SPAWNPOINTS

    @Test
    public void testConstructor() {

        GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1);
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));
        CoordinatesWithRoom c2 = new CoordinatesWithRoom(2, 1, m.getMapUsed().getGameBoard().getRoom(1));
        CoordinatesWithRoom c3 = new CoordinatesWithRoom(2, 1, m.getMapUsed().getGameBoard().getRoom(2));
        CoordinatesWithRoom c4 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));

        CoordinatesWithRoom c5 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));


        m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
        m.addPlayer(new Player(c2, Figure.PlayerColor.BLUE));
        m.addPlayer(new Player(c3, Figure.PlayerColor.PURPLE));
        m.addPlayer(new Player(c4, Figure.PlayerColor.GRAY));

        m.addPlayer(new Player(c5, Figure.PlayerColor.YELLOW)); // SHOOTER

        m.getPlayers().get(4).getHand().add(new Electroscythe());
        EffectAndNumber enBase = new EffectAndNumber(AmmoCube.Effect.BASE, 0);
        EffectAndNumber enAlt = new EffectAndNumber(AmmoCube.Effect.ALT, 0);

        List<CoordinatesWithRoom> list = m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5, enBase, m.getMapUsed().getGameBoard());
        for (CoordinatesWithRoom c : list) {
            System.out.println(c);
        }
        List<Object> targets = m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list, c5, m.getMapUsed().getGameBoard(), m.getPlayers().get(4), m, enBase);
        for (Object o : targets) {
            System.out.println(o);
        }

            // BASE EFFECT

            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(3).marksByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(targets, m.getPlayers().get(4), enBase);
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4))==1);
            assertTrue(m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4))==1);

            // ALT EFFECT
        List<Object> targetsTwo = m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list, c5, m.getMapUsed().getGameBoard(), m.getPlayers().get(4), m, enBase);


            System.out.printf("\n\nDamage by shooter: " + m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: " + m.getPlayers().get(3).marksByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(targetsTwo, m.getPlayers().get(4), enAlt);
            System.out.printf("\n\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: " + m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4))==3);
            assertTrue(m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4))==3);

    }
}
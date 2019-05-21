package adrenaline.weapons;

import adrenaline.*;
import adrenaline.weapons.Hellion;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.LinkedList;
import java.util.List;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class HellionTest {

        // NO PAYMENT, NO SPAWNPOINTS, NO PLAYER'S CHOICE INSIDE CELLSTOTARGETS

        @Test
        public void testConstructor() {

            GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,1);
            CoordinatesWithRoom c1 = new CoordinatesWithRoom(2,1,m.getMapUsed().getGameBoard().getRoom(1));
            CoordinatesWithRoom c2 = new CoordinatesWithRoom(2,1,m.getMapUsed().getGameBoard().getRoom(1));
            CoordinatesWithRoom c3 = new CoordinatesWithRoom(3,1,m.getMapUsed().getGameBoard().getRoom(1));
            CoordinatesWithRoom c4 = new CoordinatesWithRoom(3,1,m.getMapUsed().getGameBoard().getRoom(1));

            CoordinatesWithRoom c5 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(0));


            m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
            m.addPlayer(new Player(c2, Figure.PlayerColor.BLUE));
            m.addPlayer(new Player(c3, Figure.PlayerColor.PURPLE));
            m.addPlayer(new Player(c4, Figure.PlayerColor.GRAY));

            m.addPlayer(new Player(c5, Figure.PlayerColor.YELLOW)); // SHOOTER

            m.getPlayers().get(4).getHand().add(new Hellion());
            EffectAndNumber enBase = new EffectAndNumber(AmmoCube.Effect.BASE,0);
            EffectAndNumber enAlt = new EffectAndNumber(AmmoCube.Effect.ALT,0);


            List<CoordinatesWithRoom> list= m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5,enBase,m.getMapUsed().getGameBoard());
            for(CoordinatesWithRoom c : list){
                System.out.println(c);
            }
            List<Object> targets =m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list,c5,m.getMapUsed().getGameBoard(),m.getPlayers().get(4),m,enBase);

            // I CHOOSE BLUE TARGET, BASE EFFECT
            LinkedList<Object> temp = new LinkedList<>();
            temp.add(targets.get(1));
            // I MANUALLY ADD GREEN TARGET
            temp.add(m.getPlayers().get(0));
            for(Object o : temp){
                System.out.println(o);
            }

            // BLUE IS PLAYER1, GREEN IS PLAYER0
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: "+m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: "+m.getPlayers().get(1).marksByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(temp,m.getPlayers().get(4),enBase);
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: "+m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: "+m.getPlayers().get(1).marksByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4))==1);
            assertTrue(m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4))==1);



            // I CHOOSE PURPLE TARGET, THEN ADD GRAY
            LinkedList<Object> temp2 = new LinkedList<>();
            temp2.add(m.getPlayers().get(2));
            // I MANUALLY ADD GREEN TARGET
            temp2.add(m.getPlayers().get(3));
            for(Object o : temp2){
                System.out.println(o);
            }

            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: "+m.getPlayers().get(2).marksByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: "+m.getPlayers().get(3).marksByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(temp2,m.getPlayers().get(4),enAlt);
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: "+m.getPlayers().get(2).marksByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nMarks by shooter: "+m.getPlayers().get(3).marksByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4))==1);
            assertTrue(m.getPlayers().get(3).marksByShooter(m.getPlayers().get(4))==2);



        }

    }


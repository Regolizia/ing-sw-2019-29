package adrenaline.weapons;

import adrenaline.*;
import adrenaline.weapons.Thor;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.LinkedList;
import java.util.List;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class ThorTest {

    private Thor thor;

        // NO PAYMENT, NO SPAWNPOINTS, NO BASE MOVE

        @Test
        public void testConstructor() {
            thor=new Thor();
            GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,1);
            CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(0));
            CoordinatesWithRoom c2 = new CoordinatesWithRoom(3,1,m.getMapUsed().getGameBoard().getRoom(1));
            CoordinatesWithRoom c3 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(3));
            CoordinatesWithRoom c4 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(0));

            CoordinatesWithRoom c5 = new CoordinatesWithRoom(2,1,m.getMapUsed().getGameBoard().getRoom(0));


            m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
            m.addPlayer(new Player(c2, Figure.PlayerColor.BLUE));
            m.addPlayer(new Player(c3, Figure.PlayerColor.PURPLE));
            m.addPlayer(new Player(c4, Figure.PlayerColor.GRAY));

            m.addPlayer(new Player(c5, Figure.PlayerColor.YELLOW)); // SHOOTER

            m.getPlayers().get(4).getHand().add(new Thor());
            EffectAndNumber enBase = new EffectAndNumber(AmmoCube.Effect.BASE,0);
            EffectAndNumber enOp1 = new EffectAndNumber(AmmoCube.Effect.OP1,0);
            EffectAndNumber enOp2 = new EffectAndNumber(AmmoCube.Effect.OP2,0);

            List<CoordinatesWithRoom> list= m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5,enBase,m.getMapUsed().getGameBoard());
            for(CoordinatesWithRoom c : list){
                System.out.println(c);
            }
            List<Object> targets =m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list,c5,m.getMapUsed().getGameBoard(),m.getPlayers().get(4),m,enBase);
            for(Object o : targets){
                System.out.println(o);
            }

            // I CHOOSE GREEN TARGET, BASE EFFECT
            LinkedList<Object> temp = new LinkedList<>();
            temp.add(targets.get(0));

            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(temp,m.getPlayers().get(4),enBase);
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4))==2);

           // OP1 EFFECT, BLUE TARGET (PASSED THE POSITION OF GREEN)
            List<CoordinatesWithRoom> list2= m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c1,enOp1,m.getMapUsed().getGameBoard());
            for(CoordinatesWithRoom c : list2){
                System.out.println(c);
            }
            List<Object> targets2 =m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list2,c5,m.getMapUsed().getGameBoard(),m.getPlayers().get(4),m,enOp1);
            for(Object o : targets2){
                System.out.println(o);
            }

            LinkedList<Object> temp2 = new LinkedList<>();
            temp2.add(targets2.get(1));

            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(temp2,m.getPlayers().get(4),enOp1);
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4))==1);

            // OP2 EFFECT, PURPLE TARGET (PASSED THE POSITION OF BLUE)
            List<CoordinatesWithRoom> list3= m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c2,enOp2,m.getMapUsed().getGameBoard());
            for(CoordinatesWithRoom c : list3){
                System.out.println(c);
            }
            List<Object> targets3 =m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list3,c5,m.getMapUsed().getGameBoard(),m.getPlayers().get(4),m,enOp2);
            for(Object o : targets3){
                System.out.println(o);
            }

            LinkedList<Object> temp3 = new LinkedList<>();
            temp3.add(targets3.get(2));

            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(temp3,m.getPlayers().get(4),enOp2);
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4))==2);
            thor.canShootBase();
            thor.canShootOp1();
            thor.canShootOp2();
            thor.toString();

        }

    }



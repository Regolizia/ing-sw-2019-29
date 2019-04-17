import adrenaline.*;
import adrenaline.weapons.LockRifle;
import adrenaline.weapons.MachineGun;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.LinkedList;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class MachineGunTest {


        // NO PAYMENT, NO SPAWNPOINTS, NO PLAYER'S CHOICE (NO CHECK DIFFERENT TARGETS)

        @Test
        public void testConstructor() {

            GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,3);
            CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(1));
            CoordinatesWithRoom c2 = new CoordinatesWithRoom(2,1,m.getMapUsed().getGameBoard().getRoom(0));
            CoordinatesWithRoom c3 = new CoordinatesWithRoom(2,2,m.getMapUsed().getGameBoard().getRoom(3));
            CoordinatesWithRoom c4 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(0));

            CoordinatesWithRoom c5 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(3));


            m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
            m.addPlayer(new Player(c2, Figure.PlayerColor.BLUE));
            m.addPlayer(new Player(c3, Figure.PlayerColor.PURPLE));
            m.addPlayer(new Player(c4, Figure.PlayerColor.GRAY));

            m.addPlayer(new Player(c5, Figure.PlayerColor.YELLOW)); // SHOOTER

            m.getPlayers().get(4).getHand().add(new MachineGun());
            EffectAndNumber enBase = new EffectAndNumber(AmmoCube.Effect.BASE,0);
            EffectAndNumber enOp1 = new EffectAndNumber(AmmoCube.Effect.OP1,0);
            EffectAndNumber enOp2 = new EffectAndNumber(AmmoCube.Effect.OP2,0);

            LinkedList<CoordinatesWithRoom> list= m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5,enBase,m.getMapUsed().getGameBoard());
            for(CoordinatesWithRoom c : list){
                System.out.println(c);
            }
            LinkedList<Object> targets =m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list,c5,m.getMapUsed().getGameBoard(),m.getPlayers().get(4),m,enBase);
            for(Object o : targets){
                System.out.println(o);
            }


            // I CHOOSE BLUE TARGET AND GRAY TARGET, BASE EFFECT
            LinkedList<Object> temp = new LinkedList<>();
            temp.add(targets.get(0));
            temp.add(targets.get(2));

            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(temp,m.getPlayers().get(4),enBase);
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4))==1);
            assertTrue(m.getPlayers().get(3).damageByShooter(m.getPlayers().get(4))==1);


            // IF I USE OP1 ON BLUE TARGET
            temp.remove(1);

            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(temp,m.getPlayers().get(4),enOp1);
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4))==2);

            // IF I USE OP2 ON PURPLE TARGET
            temp.clear();
            temp.add(targets.get(1));

            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(temp,m.getPlayers().get(4),enOp2);
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4))==1);




        }

    }


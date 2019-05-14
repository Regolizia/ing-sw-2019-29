
import adrenaline.*;
import adrenaline.weapons.VortexCannon;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.LinkedList;
import java.util.List;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class VortexCannonTest {


        // NO PAYMENT, NO SPAWNPOINTS, NO BASE MOVE

        @Test
        public void testConstructor() {

            GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,1);
            CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(1));
            CoordinatesWithRoom c2 = new CoordinatesWithRoom(2,1,m.getMapUsed().getGameBoard().getRoom(1));
            CoordinatesWithRoom c3 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(2));
            CoordinatesWithRoom c4 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(0));

            CoordinatesWithRoom c5 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(0));


            m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
            m.addPlayer(new Player(c2, Figure.PlayerColor.BLUE));
            m.addPlayer(new Player(c3, Figure.PlayerColor.PURPLE));
            m.addPlayer(new Player(c4, Figure.PlayerColor.GRAY));

            m.addPlayer(new Player(c5, Figure.PlayerColor.YELLOW)); // SHOOTER

            m.getPlayers().get(4).getHand().add(new VortexCannon());
            EffectAndNumber enBase = new EffectAndNumber(AmmoCube.Effect.BASE,0);
            EffectAndNumber enOp1 = new EffectAndNumber(AmmoCube.Effect.OP1,0);

            List<CoordinatesWithRoom> list= m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5,enBase,m.getMapUsed().getGameBoard());

            // DONE MANUALLY, THE REQUEST SHOULD BE INSIDE THE WEAPON
            LinkedList<CoordinatesWithRoom> listwithvortex = new LinkedList<>();
            listwithvortex.add(list.get(4));
            listwithvortex.addAll(list.get(4).oneTileDistant(m.getMapUsed().getGameBoard(), false));

            for(CoordinatesWithRoom c : listwithvortex){
                System.out.println(c);
            }

            List<Object> targets =m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(listwithvortex,c5,m.getMapUsed().getGameBoard(),m.getPlayers().get(4),m,enBase);
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

            // OP1 EFFECT, BLUE TARGET AND PURPLE TARGET
            LinkedList<Object> temp2 = new LinkedList<>();
            temp2.add(targets.get(1));
            temp2.add(targets.get(2));

            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            m.getPlayers().get(4).getHand().get(0).applyDamage(temp2,m.getPlayers().get(4),enOp1);
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
            System.out.printf("\nDamage by shooter: "+m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4)));
            assertTrue(m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4))==1);
            assertTrue(m.getPlayers().get(2).damageByShooter(m.getPlayers().get(4))==1);


        }

    }



import adrenaline.*;
import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.LockRifle;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class LockRifleTest {


    // NO PAYMENT, NO SPAWNPOINTS

    @Test
    public void testConstructor() {

        GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,1);
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(1));
        CoordinatesWithRoom c2 = new CoordinatesWithRoom(2,1,m.getMapUsed().getGameBoard().getRoom(1));
        CoordinatesWithRoom c3 = new CoordinatesWithRoom(2,1,m.getMapUsed().getGameBoard().getRoom(2));
        CoordinatesWithRoom c4 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(0));

        CoordinatesWithRoom c5 = new CoordinatesWithRoom(1,1,m.getMapUsed().getGameBoard().getRoom(0));


        m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
        m.addPlayer(new Player(c2, Figure.PlayerColor.BLUE));
        m.addPlayer(new Player(c3, Figure.PlayerColor.PURPLE));
        m.addPlayer(new Player(c4, Figure.PlayerColor.GRAY));

        m.addPlayer(new Player(c5, Figure.PlayerColor.YELLOW));

        m.getPlayers().get(4).getHand().add(new LockRifle());
        EffectAndNumber enBase = new EffectAndNumber(AmmoCube.Effect.BASE,0);
        EffectAndNumber enOp1 = new EffectAndNumber(AmmoCube.Effect.OP1,0);

       LinkedList<CoordinatesWithRoom> list= m.getPlayers().get(4).getHand().get(0).getPossibleTargetCells(c5,enBase,m.getMapUsed().getGameBoard());
       for(CoordinatesWithRoom c : list){
           System.out.println(c);
       }
       LinkedList<Object> targets =m.getPlayers().get(4).getHand().get(0).fromCellsToTargets(list,c5,m.getMapUsed().getGameBoard(),m.getPlayers().get(4),m,enBase);
        for(Object o : targets){
            System.out.println(o);
        }

        // I CHOOSE BLUE TARGET
        LinkedList<Object> temp = new LinkedList<>();
        temp.add(targets.get(1));

        System.out.printf("Damage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
        System.out.printf("\nMarks by shooter: "+m.getPlayers().get(1).marksByShooter(m.getPlayers().get(4)));
        m.getPlayers().get(4).getHand().get(0).applyDamage(temp,m.getPlayers().get(4),enBase);
        System.out.printf("\nDamage by shooter: "+m.getPlayers().get(1).damageByShooter(m.getPlayers().get(4)));
        System.out.printf("\nMarks by shooter: "+m.getPlayers().get(1).marksByShooter(m.getPlayers().get(4)));


        // IF I USE OP1 ON GREEN TARGET
        temp.clear();
        temp.add(targets.get(0));

        System.out.printf("\nDamage by shooter: "+m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
        System.out.printf("\nMarks by shooter: "+m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));
        m.getPlayers().get(4).getHand().get(0).applyDamage(temp,m.getPlayers().get(4),enOp1);
        System.out.printf("\nDamage by shooter: "+m.getPlayers().get(0).damageByShooter(m.getPlayers().get(4)));
        System.out.printf("\nMarks by shooter: "+m.getPlayers().get(0).marksByShooter(m.getPlayers().get(4)));



    }

}

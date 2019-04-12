import adrenaline.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class WeaponCardTest {

    @Test
    public void testConstructor() {
        GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 4);
        Map map = new MapFour(DEATHMATCH);
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player p = new Player(c1, Figure.PlayerColor.GRAY);
        m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
        m.addPlayer(new Player(c1, Figure.PlayerColor.GREEN));
        m.getPlayers().get(0).setPlayerPosition(1,1,map.getGameBoard().getRoom(1));
        m.getPlayers().get(0).setPlayerPosition(1,1,map.getGameBoard().getRoom(1));
        LinkedList<EffectAndNumber> list = new LinkedList<>();
        list.add(new EffectAndNumber(AmmoCube.Effect.BASE,1));
        list.add(new EffectAndNumber(AmmoCube.Effect.OP1,1));
        WeaponCard w = new WeaponCard();
        LinkedList<Object> targets = new LinkedList<>();
        LinkedList<CoordinatesWithRoom> l = w.getPossibleTargetCells(c1, AmmoCube.Effect.ALT,map.getGameBoard());
        w.weaponShoot(targets,c1,p,list,m);
        w.proposeTargets(c1,m.getMapUsed().getGameBoard(),p,m, AmmoCube.Effect.ALT);
        p.getTrack()[2]= Figure.PlayerColor.BLUE;
        w.proposeTargets(c1,m.getMapUsed().getGameBoard(),p,m, AmmoCube.Effect.ALT);
        p.getTrack()[5]= Figure.PlayerColor.BLUE;
        w.proposeTargets(c1,m.getMapUsed().getGameBoard(),p,m, AmmoCube.Effect.ALT);
        LinkedList<Object> ma = new LinkedList<>();
        w.applyDamage(ma,p,list.get(0));


    }
}

package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapFour;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class WeaponCard2Test {

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
        EffectAndNumber en = new EffectAndNumber(AmmoCube.Effect.ALT,1);
        List<CoordinatesWithRoom> l = w.getPossibleTargetCells(c1, en, map.getGameBoard());
        w.weaponShoot(targets,c1,p,list,m);
        EffectAndNumber en1 = new EffectAndNumber(AmmoCube.Effect.ALT,1);
        w.fromCellsToTargets(l,c1,m.getMapUsed().getGameBoard(),p,m, en1);
        p.getTrack()[2]= Figure.PlayerColor.BLUE;
        w.fromCellsToTargets(l,c1,m.getMapUsed().getGameBoard(),p,m, en1);
        p.getTrack()[5]= Figure.PlayerColor.BLUE;
        w.fromCellsToTargets(l,c1,m.getMapUsed().getGameBoard(),p,m, en1);
        LinkedList<Object> ma = new LinkedList<>();
        w.applyDamage(ma,p,list.get(0));


    }
}

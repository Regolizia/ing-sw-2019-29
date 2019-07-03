package adrenaline;

import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.Thor;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static adrenaline.GameModel.Mode.DEATHMATCH;
import static org.junit.jupiter.api.Assertions.*;

class FreneticActionTest {
    private FreneticAction actionF;
    @Test
    void constructor(){
        actionF=new FreneticAction(new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1,false));
        WeaponCard w=new Thor();
        WeaponCard wyellow=new Cyberblade();
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1,false).getMapUsed().getGameBoard().getRoom(0));
        Player player=new Player(c1, Figure.PlayerColor.GREEN);
        Player pl=new Player(c1, Figure.PlayerColor.BLUE);
        Player victim=new Player(c1, Figure.PlayerColor.YELLOW);
        actionF.proposeCellsRunFrenzy(c1);
        actionF.proposeCellsGrabFrenzy(c1, FreneticAction.PlayerOrder.FIRST);
        actionF.proposeCellsGrabFrenzy(c1, FreneticAction.PlayerOrder.AFTER);
        actionF.proposeCellsRunBeforeShootFrenzy(c1, FreneticAction.PlayerOrder.AFTER);
        actionF.proposeCellsRunBeforeShootFrenzy(c1, FreneticAction.PlayerOrder.FIRST);
        victim.newLife();
        LinkedList<Object>victims=new LinkedList<>();
        victims.add(victim);
        EffectAndNumber effectAndNumber=new EffectAndNumber(AmmoCube.Effect.BASE,0);
        w.applyDamage(victims,player,effectAndNumber);
        w.applyDamage(victims,player,effectAndNumber);
        w.applyDamage(victims,pl,effectAndNumber);
        w.applyDamage(victims,player,effectAndNumber);
        victim.damageByShooter(player);
        victim.damageByShooter(pl);
        LinkedList<Player>players=new LinkedList<>();
        victim.damageByShooter(pl);
        victim.damageByShooter(pl);
        players.add(player);
        players.add(pl);
        actionF.whoHasDoneMoreDamage(players,victim);
        players.clear();
        players.add(player);
        players.add(pl);
        actionF.whoHasDoneMoreDamage(players,victim);
        actionF.chooseOne(pl,player,victim);
        System.out.print( victim.getFirstPositionOnTrack(pl));
        actionF.chooseOne(player,pl,victim);
        actionF.frenzyGivePoints(players,victim);
        w.applyDamage(victims,pl,effectAndNumber);
        w.applyDamage(victims,player,effectAndNumber);
        actionF.frenzyGivePoints(players,victim);
        w.applyDamage(victims,pl,effectAndNumber);
        w.applyDamage(victims,pl,effectAndNumber);
        w.applyDamage(victims,pl,effectAndNumber);
        w.applyDamage(victims,pl,effectAndNumber);
        actionF.whoHasDoneMoreDamage(players,victim);
        List<Player>victimss=new LinkedList<>();
        victimss.add(victim);
        actionF.canGetPoints(victimss,players);



    }
}
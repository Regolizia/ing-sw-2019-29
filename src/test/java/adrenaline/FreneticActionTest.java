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
    /*testing frenetic action*/
    void constructor(){

   //____________________________________PROPOSE CELLS__________________________________________________________________________//
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
    //___________________________DAMAGE AND ORDER __to do points methods______________________________________________//
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

        Player aldo=new Player(c1, Figure.PlayerColor.BLUE);
        Player giovanni=new Player(c1, Figure.PlayerColor.YELLOW);
        Player giacomo=new Player(c1, Figure.PlayerColor.GREEN);
        LinkedList<Object> victimsso=new LinkedList<>();
        players.clear();
        players.add(aldo);
        players.add(giacomo);
        victimss.clear();
        victimss.add(giovanni);
        victimsso.add(giovanni);
        w.applyDamage(victimsso,aldo,effectAndNumber);
        w.applyDamage(victimsso,giacomo,effectAndNumber);
        w.applyDamage(victimsso,giacomo,effectAndNumber);
        actionF.canGetPoints(victimss,players);
        for (Player p:players
             ) {
            System.out.print(p+":"+p.getPoints());
        }


    }
}
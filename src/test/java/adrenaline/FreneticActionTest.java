package adrenaline;

import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.Thor;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;
import static org.junit.jupiter.api.Assertions.*;

class FreneticActionTest {
    private FreneticAction actionF;
    @Test
    void constructor(){
        actionF=new FreneticAction(new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1));
        WeaponCard w=new Thor();
        WeaponCard wyellow=new Cyberblade();
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1).getMapUsed().getGameBoard().getRoom(0));
        Player player=new Player(c1, Figure.PlayerColor.GREEN);

        actionF.proposeCellsRunFrenzy(c1);
        actionF.proposeCellsGrabFrenzy(c1, FreneticAction.PlayerOrder.FIRST);
        actionF.proposeCellsGrabFrenzy(c1, FreneticAction.PlayerOrder.AFTER);
        actionF.proposeCellsRunBeforeShootFrenzy(c1, FreneticAction.PlayerOrder.AFTER);
        actionF.proposeCellsRunBeforeShootFrenzy(c1, FreneticAction.PlayerOrder.FIRST);
    }
}
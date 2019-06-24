package adrenaline;

import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;
import static org.junit.jupiter.api.Assertions.*;

class BotActionTest {
    private BotAction botAction;

    @Test
    void constructor(){
        GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1);
        botAction=new BotAction(m);
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));
        Player player = new Player(c1, Figure.PlayerColor.GREEN);
        Bot bot=new Bot(Figure.PlayerColor.NONE);



        botAction.proposeCellsRun(c1);

        botAction.botShoot(player,bot);
        botAction.botShoot(player,bot);
        botAction.botShoot(player,bot);

        botAction.botShoot(player,bot);
        botAction.botShoot(player,bot);
        botAction.botShoot(player,bot);

        botAction.botShoot(player,bot);
        botAction.botShoot(player,bot);
        botAction.botShoot(player,bot);

        botAction.botShoot(player,bot);
        botAction.botShoot(player,bot);
        botAction.botShoot(player,bot);

       player.getMarks();


    }

}
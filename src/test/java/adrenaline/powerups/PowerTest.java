package adrenaline.powerups;

import adrenaline.*;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;
import static org.junit.jupiter.api.Assertions.*;

class PowerTest {
private Newton newton;
private TagbackGrenade tagbackGrenade;
private TargetingScope targetingScope;
private Teleporter teleporter;

@Test
void constructor(){
    newton=new Newton(AmmoCube.CubeColor.BLUE);
    tagbackGrenade=new TagbackGrenade(AmmoCube.CubeColor.BLUE);
    targetingScope=new TargetingScope(AmmoCube.CubeColor.BLUE);
    teleporter=new Teleporter(AmmoCube.CubeColor.BLUE);
    GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1);
    CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));
    Player player = new Player(c1, Figure.PlayerColor.GREEN);
    newton.toString();
    tagbackGrenade.toString();
    tagbackGrenade.giveMark(player,player);
    targetingScope.toString();
    targetingScope.plusOneDamage(player,player);
    teleporter.getPossibleCells(m,player);

}
}
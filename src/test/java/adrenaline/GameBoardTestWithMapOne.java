package adrenaline;

import adrenaline.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class GameBoardTestWithMapOne {

    @Test
    public void testConstructor() {
        GameModel g = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,1,false);
        GameModel g2 = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,2,false);
        GameModel g3 = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,3,false);
        GameModel g4 = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,4,false);

        GameModel g5 = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT,5,false);

        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,g.getMapUsed().getGameBoard().getRoom(0));
        g.addPlayer(new Player(c1, Figure.PlayerColor.BLUE));
        g.addPlayer(new Player());
        Figure.PlayerColor c = g.getPlayers().get(0).getColor();
        int x = g.getPlayers().get(0).checkDamage();
        g.getPlayers().get(0).getTrack()[2] = Figure.PlayerColor.BLUE;
        int y = g.getPlayers().get(0).checkDamage();
        g.getPlayers().get(0).getTrack()[5] = Figure.PlayerColor.BLUE;
        int z = g.getPlayers().get(0).checkDamage();
        g.getPlayers().get(0).canGrabPowerUp();
        g.getPlayers().get(0).canGrabWeapon();

        g.getPlayers().get(0).setPlayerPosition(1,2);
        g.getPlayers().get(0).getPlayerPositionX();
        g.getPlayers().get(0).getPlayerPositionY();
        g.getPlayers().get(0).setPlayerPosition(1,1,g.getMapUsed().getGameBoard().getRoom(1));
        g.getPlayers().get(0).getTrack()[11] = Figure.PlayerColor.BLUE;
        g.getPlayers().get(0).isDead();
        //g.getPlayers().get(0).hasDied();
        System.out.printf(Arrays.toString(g.getPlayers().get(0).getPointsArray()));
        //g.getPlayers().get(0).hasDied();
        System.out.printf(Arrays.toString(g.getPlayers().get(0).getPointsArray()));

    }

}

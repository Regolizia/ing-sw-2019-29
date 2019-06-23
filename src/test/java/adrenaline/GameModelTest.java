package adrenaline;

import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    @Test
    void constructor() {

        GameModel model;
        model=new GameModel(GameModel.Mode.DEATHMATCH, GameModel.Bot.NOBOT,1);
        Spawnpoint s=new Spawnpoint(1,2);
        System.out.println(model.getMapUsed().getGameBoard().getRoom(2).getSpawnpoints().size());
        model.getMapUsed().getGameBoard().getRoom(2).addSpawnpoint(s);

        System.out.println(model.getMapUsed().getGameBoard().getRoom(2).getSpawnpoints().size());

        System.out.println(model.getMapUsed().getGameBoard().getRoom(2).getSpawnpoints().indexOf(s));

        model.getMapUsed().getGameBoard().getRoom(1).addAmmoTile(new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED),1,1);
        model.populateMap();
    }
}
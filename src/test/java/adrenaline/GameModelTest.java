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
        model.startingMap();
        model.getMapUsed().getGameBoard().getRoom(0).getTiles();
        Coordinates c1 = new Coordinates(1,1);
        model.getMapUsed().getGameBoard().getRoom(0).getAmmoTile(c1);
        int yspawn=model.getMapUsed().getGameBoard().getRoom(0).getSpawnpoints().get(0).getSpawnpointY();
        int xspawn=model.getMapUsed().getGameBoard().getRoom(0).getSpawnpoints().get(0).getSpawnpointX();
        AmmoTile ammoTile=new AmmoTile(AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE);
        model.getMapUsed().getGameBoard().getRoom(0).addAmmoTile(ammoTile,c1.getX(),c1.getY());
        model.getMapUsed().getGameBoard().getRoom(0).getAmmoTile(c1);
        model.getMapUsed().getGameBoard().getRoom(0).getAmmoTile(new Coordinates(0,0));
        Coordinates cspawn=new Coordinates(xspawn,yspawn);
        model.getMapUsed().getGameBoard().getRoom(0).getSpawnpoint(cspawn.getX(),cspawn.getY());
        model.getMapUsed().getGameBoard().getRoom(0).getSpawnpoint(0,0);
    }
}
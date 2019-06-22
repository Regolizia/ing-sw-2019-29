package adrenaline;

import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.CyberbladeTest;
import org.junit.jupiter.api.Test;

import javax.management.modelmbean.ModelMBean;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
GameModel model;
    @Test
    void constructor() {
        model=new GameModel(GameModel.Mode.DEATHMATCH, GameModel.Bot.NOBOT,1);
        Spawnpoint s=new Spawnpoint(1,2);
        System.out.println(model.getMapUsed().getGameBoard().getRoom(2).getSpawnpoints().size());
        model.getMapUsed().getGameBoard().getRoom(2).addSpawnpoint(s);
        
        System.out.println(model.getMapUsed().getGameBoard().getRoom(2).getSpawnpoints().size());

        System.out.println(model.getMapUsed().getGameBoard().getRoom(2).getSpawnpoints().indexOf(s));

        model.getMapUsed().getGameBoard().getRoom(1).addAmmoTile(new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED));
        model.populateMap();
    }
}
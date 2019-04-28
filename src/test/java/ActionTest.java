import adrenaline.*;
import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.Electroscythe;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import java.util.LinkedList;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class ActionTest {


    @Test
    public void testConstructor() {

    }

    //input ActionType chosen,
    @Test
    public void doAction(){
        boolean notEnd=true;
        Map map = new MapFour(DEATHMATCH);
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player p = new Player(c1, Figure.PlayerColor.GRAY);
        GameBoard g=map.getGameBoard();
        GameModel m=new GameModel(DEATHMATCH,null,1);
        Action.PayOption optionNONE= Action.PayOption.NONE;
        Action.ActionType move= Action.ActionType.RUN;

        }
     @Test
    public void proposeCellsRun() {
        Map map = new MapFour(DEATHMATCH);
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        GameBoard g=map.getGameBoard();
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.XTilesDistant(g, 1));
    }
@Test
    public void run() {
    Map map = new MapFour(DEATHMATCH);
    CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
    Player p = new Player(c1, Figure.PlayerColor.GRAY);
    p.setPlayerPosition(c1.getX(),c1.getY(),c1.getRoom());
    }
    @Test
    public void proposeCellsGrab() {
        Map map = new MapFour(DEATHMATCH);
        GameBoard g=map.getGameBoard();
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.XTilesDistant(g, 1));
        Player p = new Player(c, Figure.PlayerColor.GRAY);
        list.add(c);

        // IF ADRENALINE GRAB IS POSSIBLE
        if (p.checkDamage() == 1) {
            list.addAll(c.XTilesDistant(g, 2));
        }

    }
@Test
    public void grab( )
{
    Action.PayOption option= Action.PayOption.AMMO;
    Map map = new MapFour(DEATHMATCH);
    CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
    Player p = new Player(c, Figure.PlayerColor.GRAY);
    p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
    p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
    Spawnpoint s=new Spawnpoint(c.getX(),c.getY());
    c.getRoom().addSpawnpoint(s);
    LinkedList<Spawnpoint>spawnpoints=c.getRoom().getSpawnpoints();
        // IF THERE IS A SPAWNPOINT HERE
    for (int i=0;i<spawnpoints.size();i++) {
        if (c.getRoom() == p.getPlayerRoom() && c.getX() == p.getPlayerPositionX() && c.getY() == p.getPlayerPositionY()
                && c.getRoom().getSpawnpoints() != null && c.getRoom().getSpawnpoints().get(i).getSpawnpointX() == c.getX() &&
                c.getRoom().getSpawnpoints().get(i).getSpawnpointY() == c.getY()) ;
    }


}
@Test
    public void grabCard(){
        Map map = new MapFour(DEATHMATCH);
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player player = new Player(c, Figure.PlayerColor.GRAY);
        Action.PayOption option= Action.PayOption.AMMO;

        LinkedList<WeaponCard> hand= new LinkedList<>();
        LinkedList <WeaponCard> canBeGrabbedWeapon=new LinkedList<>();
        WeaponCard w=new Electroscythe();
        canBeGrabbedWeapon.addFirst(w);

        hand.add(canBeGrabbedWeapon.get(0));
        canBeGrabbedWeapon.get(0).setReload(); //when i grab a weapon i've already paid its base effect

    }
    @Test
    public void dropWeaponCard(){
        Player player=new Player(null, Figure.PlayerColor.GRAY);

        player.getHand().addFirst(new Cyberblade());
        player.getHand().add(new Electroscythe());
        System.out.println(player.getHand());
        player.getHand().removeFirst();

        System.out.println(player.getHand());
    }




}


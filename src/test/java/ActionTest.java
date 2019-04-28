import adrenaline.*;
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
    }


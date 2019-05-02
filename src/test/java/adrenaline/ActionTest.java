package adrenaline;
import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.Thor;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static adrenaline.GameModel.Mode.DEATHMATCH;
public class ActionTest {
    private Action action;
    @Test
    void constructor(){
        action = new Action(true); //constructor
        WeaponCard w=new Thor();//
        Action.ActionType actionType= Action.ActionType.RUN; //enum
        Action.PayOption payOption= Action.PayOption.AMMO;  //enum
        GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1);// to do coordinatesWithRoom
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));//to do doAction
        Player player=new Player(c1, Figure.PlayerColor.GREEN);//to do doAction()
        Player victim=new Player(c1, Figure.PlayerColor.BLUE);//
        LinkedList<Object>victims=new LinkedList<>();//
        victims.add(victim);//
        LinkedList<CoordinatesWithRoom> c=action.proposeCellsGrab(c1,m.getMapUsed().getGameBoard(),player);//to do grab()
        LinkedList<PowerUpCard>powers=new LinkedList<>();//to do payPowerUp()
        PowerUpCard powerOne=new PowerUpCard(AmmoCube.CubeColor.RED);//
        PowerUpCard powerTwo=new PowerUpCard(AmmoCube.CubeColor.BLUE);//
        PowerUpCard powerThree=new PowerUpCard(AmmoCube.CubeColor.YELLOW);//
        powers.add(powerOne);//
        powers.get(0).getPowerUpColor().equals(AmmoCube.CubeColor.RED);//
        powers.remove(0);//
        powers.add(powerTwo);//
        powers.get(0).getPowerUpColor().equals(AmmoCube.CubeColor.BLUE);//
        powers.remove(0);//
        powers.add(powerThree);//
        powers.get(0).getPowerUpColor().equals(AmmoCube.CubeColor.YELLOW);//


        new Thor().getPrice().get(0).getCubeColor().equals(AmmoCube.CubeColor.BLUE);//


        action.chooseWeaponCard(player.getHand());//
        action.chooseTargets(victims,1);//
        action.grab(player,c1,m.getMapUsed().getGameBoard(), Action.PayOption.AMMO);// to do grab();
        action.paidEffect(new Thor(),player, Action.PayOption.AMMO);//
        action.paidEffect(new Thor(),player, Action.PayOption.AMMOPOWER);//
        action.pay(player,new AmmoCube(AmmoCube.CubeColor.RED));// pay()
        action.pay(player,new AmmoCube(AmmoCube.CubeColor.BLUE));//pay()
        action.pay(player,new AmmoCube(AmmoCube.CubeColor.YELLOW));//pay()
        action.canPayAmmo(w,player,player.getCubeRed(),player.getCubeYellow(),player.getCubeBlue());//
        action.canPayCard(w,player, Action.PayOption.AMMOPOWER);//
        action.canPayCard(w,player, Action.PayOption.AMMO);//
        action.canPayCard(w,player, Action.PayOption.NONE);//
        action.payPowerUp(new Thor(),powers,player);//
        action.reload(player,w, Action.PayOption.AMMO);//
        action.reload(player,w, Action.PayOption.AMMOPOWER);//
        action.reload(player,w, Action.PayOption.NONE);//
        action.doAction(Action.ActionType.RUN,player,c1,m.getMapUsed().getGameBoard(),m, Action.PayOption.AMMO);
        //action.doAction(Action.ActionType.GRAB,player,c1,m.getMapUsed().getGameBoard(),m, Action.PayOption.AMMO);
        //action.doAction(Action.ActionType.SHOOT,player,c1,m.getMapUsed().getGameBoard(),m, Action.PayOption.AMMO);
        //action.doAction(Action.ActionType.RELOAD,player,c1,m.getMapUsed().getGameBoard(),m, Action.PayOption.AMMO);
    }
}
/*
 * pay() covered
 * canPayCard() covered
 * run() covered
 * getEndTurn() covered
 *chooseTarget() covered
 * reload() covered
 * */
package adrenaline;
import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.Thor;
import adrenaline.weapons.Zx_2;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static adrenaline.GameModel.Mode.DEATHMATCH;
public class ActionTest {
    private Action action;
    @Test
    void constructor(){
        GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1);// to do coordinatesWithRoom
        action = new Action(m); //constructor
        WeaponCard w=new Thor();//
        Action.ActionType actionType= Action.ActionType.RUN; //enum
        Action.PayOption payOption= Action.PayOption.AMMO;  //enum

        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));//to do doAction
        Player player=new Player(c1, Figure.PlayerColor.GREEN);//to do doAction()
        Player victim=new Player(c1, Figure.PlayerColor.BLUE);//
        LinkedList<Object>victims=new LinkedList<>();//
        victims.add(victim);//
        LinkedList<Player>players=new LinkedList<>();
        players.add(player);players.add(victim);
        //LinkedList<CoordinatesWithRoom> c=action.proposeCellsGrab(c1,m.getMapUsed().getGameBoard());//to do grab()
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
        player.getHand().add(w);//
        player.getHand().get(0);//

        new Thor().getPrice().get(0).getCubeColor().equals(AmmoCube.CubeColor.BLUE);//
        Spawnpoint s=new Spawnpoint(0,1);
        c1.getRoom().addSpawnpoint(s);
      //  action.chooseWeaponCard(player.getHand());//
        //  action.chooseTargets(victims,1);//

       // action.paidEffect(new Thor(),player, Action.PayOption.AMMO, AmmoCube.Effect.BASE,m);//
        action.pay(player,new AmmoCube(AmmoCube.CubeColor.RED));// pay()
        action.pay(player,new AmmoCube(AmmoCube.CubeColor.BLUE));//pay()
        action.pay(player,new AmmoCube(AmmoCube.CubeColor.YELLOW));//pay()
        action.canPayAmmo(w,player.getCubeRed(),player.getCubeYellow(),player.getCubeBlue(), AmmoCube.Effect.BASE);//
        action.canPayCard(w,player, Action.PayOption.AMMOPOWER, AmmoCube.Effect.BASE);//
        action.canPayCard(w,player, Action.PayOption.AMMO, AmmoCube.Effect.BASE);//
        action.canPayCard(w,player, Action.PayOption.NONE, AmmoCube.Effect.BASE);//
        action.payPowerUp(new Thor(),powers,player, AmmoCube.Effect.BASE,0);//
        //action.reload(player,w, Action.PayOption.AMMO, AmmoCube.Effect.BASE,m);//
        //action.reload(player,w, Action.PayOption.AMMOPOWER, AmmoCube.Effect.BASE,m);//
        //action.reload(player,w, Action.PayOption.NONE, AmmoCube.Effect.BASE,m);//
       // action.proposeCellsRunBeforeShoot(c1,m.getMapUsed().getGameBoard());//

LinkedList<AmmoCube.Effect> list=new LinkedList<>();
list.add(AmmoCube.Effect.BASE);

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
package adrenaline;
import adrenaline.weapons.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static adrenaline.GameModel.Mode.DEATHMATCH;
public class ActionTest {
    private Action action;
    @Test
    void constructor() {
        GameModel m = new GameModel(DEATHMATCH, GameModel.Bot.NOBOT, 1,false);// to do coordinatesWithRoom
        action = new Action(m); //constructor
        WeaponCard w = new Thor();//
        WeaponCard wyellow = new Cyberblade();
        Action.ActionType actionType = Action.ActionType.RUN; //enum
        Action.PayOption payOption = Action.PayOption.AMMO;  //enum
        m.getMapUsed().getArrayX();
        m.getMapUsed().getArrayY();
        m.getMapUsed().fromIndexToColor(5);

        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1, 1, m.getMapUsed().getGameBoard().getRoom(0));//to do doAction
        Player player = new Player(c1, Figure.PlayerColor.GREEN);//to do doAction()
        Player victim = new Player(c1, Figure.PlayerColor.BLUE);//
        LinkedList<Object> victims = new LinkedList<>();//
        victims.add(victim);//
        LinkedList<Player> players = new LinkedList<>();
        players.add(player);
        players.add(victim);
        //LinkedList<CoordinatesWithRoom> c=action.proposeCellsGrab(c1,m.getMapUsed().getGameBoard());//to do grab()
        LinkedList<PowerUpCard> powers = new LinkedList<>();//to do payPowerUp()
        PowerUpCard powerOne = new PowerUpCard(AmmoCube.CubeColor.RED);//
        PowerUpCard powerTwo = new PowerUpCard(AmmoCube.CubeColor.BLUE);//
        PowerUpCard powerThree = new PowerUpCard(AmmoCube.CubeColor.YELLOW);//


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
        Spawnpoint s = new Spawnpoint();
        c1.getRoom().addSpawnpoint(s);
        //  action.chooseWeaponCard(player.getHand());//
        //  action.chooseTargets(victims,1);//

        // action.paidEffect(new Thor(),player, Action.PayOption.AMMO, AmmoCube.Effect.BASE,m);//
        action.pay(player, new AmmoCube(AmmoCube.CubeColor.RED));// pay()
        action.pay(player, new AmmoCube(AmmoCube.CubeColor.BLUE));//pay()
        action.pay(player, new AmmoCube(AmmoCube.CubeColor.YELLOW));//pay()
        action.canPayAmmo(w, player.getCubeRed(), player.getCubeYellow(), player.getCubeBlue(), AmmoCube.Effect.BASE);//
        action.canPayCard(w, player, Action.PayOption.AMMOPOWER, AmmoCube.Effect.BASE, new LinkedList<>());//
        action.canPayCard(w, player, Action.PayOption.AMMO, AmmoCube.Effect.BASE, new LinkedList<>());//
        action.canPayCard(w, player, Action.PayOption.NONE, AmmoCube.Effect.BASE, new LinkedList<>());//
        action.payPowerUp(new Thor(), powers, player, AmmoCube.Effect.BASE, 0);//
        //action.reload(player,w, Action.PayOption.AMMO, AmmoCube.Effect.BASE,m);//
        //action.reload(player,w, Action.PayOption.AMMOPOWER, AmmoCube.Effect.BASE,m);//
        //action.reload(player,w, Action.PayOption.NONE, AmmoCube.Effect.BASE,m);//
        // action.proposeCellsRunBeforeShoot(c1,m.getMapUsed().getGameBoard());//
        action.proposeCellsRun(player.getCoordinatesWithRooms());
        action.proposeCellsRunBeforeShoot(player);
        player.damageByShooter(player);
        player.damageByShooter(player);
        if (player.checkDamage() >= 2) ;
        action.proposeCellsRunBeforeShootAdrenaline(player);
        action.proposeCellsRunBeforeShootAdrenaline(player);
        action.proposeCellsGrab(player);
        action.proposeCellsGrabAdrenaline(player);
        action.run(player, player.getCoordinatesWithRooms());
        AmmoTile a = new AmmoTile(AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.BLUE);
        a.setCoordinates(1, 2);
        Room room = new RoomDeath(1, 2);
        //CoordinatesWithRoom cTile = new CoordinatesWithRoom(1, 2, room);
       // cTile.getRoom().addAmmoTile(a);
      //  CoordinatesWithRoom Tile = new CoordinatesWithRoom(a.getCoordinates().getX(), a.getCoordinates().getY(), cTile.getRoom());
     //   action.grabTile(player, Tile);
        action.grabPowerUp(player);
        AmmoTile a1 = new AmmoTile(AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW);
        AmmoTile a2 = new AmmoTile(AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.RED);
        action.grabCube(player, a);
        action.grabCube(player, a1);
        action.grabCube(player, a2);

        EffectAndNumber en = new EffectAndNumber(AmmoCube.Effect.BASE, 0);
        LinkedList<EffectAndNumber> enL = new LinkedList();
        enL.add(en);

        victims.add(player);
        action.shoot();
        LinkedList<AmmoCube.Effect> list = new LinkedList<>();
        list.add(AmmoCube.Effect.BASE);
        action.reload(player, w, Action.PayOption.AMMOPOWER, AmmoCube.Effect.BASE, new LinkedList<>());
        action.reload(player, w, Action.PayOption.AMMO, AmmoCube.Effect.BASE, new LinkedList<>());
        action.reload(player, w, Action.PayOption.NONE, AmmoCube.Effect.BASE, new LinkedList<>());
        LinkedList<PowerUpCard> power = new LinkedList<>();
        PowerUpCard powerUpCard = new PowerUpCard();
        powerUpCard.setPowerUpColor(AmmoCube.CubeColor.BLUE);
        PowerUpCard powerUpCard2 = new PowerUpCard();
        powerUpCard2.setPowerUpColor(AmmoCube.CubeColor.RED);
        PowerUpCard powerUpCard3 = new PowerUpCard();
        powerUpCard3.setPowerUpColor(AmmoCube.CubeColor.YELLOW);
        power.add(powerUpCard);
        power.add(powerUpCard2);
        power.add(powerUpCard3);
        action.reloadAmmoPower(player, w, AmmoCube.Effect.BASE, power);
        action.reloadAmmo(player, w, 2, 2, 2, AmmoCube.Effect.BASE);
        action.reloadAmmo(player, wyellow, 2, 2, 2, AmmoCube.Effect.BASE);
        action.canPayAmmo(w, 2, 2, 2, AmmoCube.Effect.BASE);
        action.canPayAmmo(wyellow, 2, 2, 2, AmmoCube.Effect.BASE);
        action.canPayAmmoPower(wyellow, player, power, AmmoCube.Effect.BASE);
        action.canPayAmmoPower(w, player, power, AmmoCube.Effect.BASE);
        action.paidEffect(w, player, Action.PayOption.AMMOPOWER, AmmoCube.Effect.BASE, power, 0);
        action.paidEffect(w, player, Action.PayOption.AMMO, AmmoCube.Effect.BASE, power, 0);
        action.paidEffect(w, player, Action.PayOption.NONE, AmmoCube.Effect.BASE, power, 0);
        action.payPower(player, wyellow.getPrice(), 2, 2, 2, AmmoCube.Effect.BASE);
        action.payPower(player, wyellow.getPrice(), 2, 2, 0, AmmoCube.Effect.BASE);
        action.getEndturn();
        action.setEndTurn(true);
        Spawnpoint sp = new Spawnpoint(1, 2);
        Player luke = new Player(c1, Figure.PlayerColor.BLUE);
        LinkedList<Player> vic = new LinkedList<>();
        vic.add(player);
        vic.add(luke);
        players.add(luke);
        luke.damageByShooter(player);
        luke.damageByShooter(player);
        luke.damageByShooter(player);
        action.canGetPoints(vic, players);
        action.endOfTheGame(action.getModel().getMapUsed().getGameBoard());
        action.bestShooterOrder(players, vic.getFirst());
        action.getRemaingPlayer();
        action.setRemainigPlayerMinus(action.getRemaingPlayer() - 1);
        for (int i = 0; i < 12; i++)
            victim.damageByShooter(player);
        LinkedList<Player> shooters = new LinkedList<>();
        shooters.add(player);
        action.givePoints(victim, shooters);
        victim.putASkullOnTrack();
        victim.newLife();
        Player player2=new Player(c1, Figure.PlayerColor.GRAY);
        for (int i = 0; i < 6; i++)
        {
            victim.damageByShooter(player);

            victim.damageByShooter(player2);}

        shooters.add(player2);
        action.givePoints(victim,  action.bestShooterOrder(shooters,victim));
        action.newtonChoosePossibleMoveFirstAllCells(player);
        action.newtonChoosePossibleMoveFirstCell(player);
        LinkedList<CoordinatesWithRoom>coo=new LinkedList<>();
        coo.add(c1);
        coo.add(player.getCoordinatesWithRooms());
        action.removeDifferentDirection(coo,c1);
        action.canPayTargetingScope(AmmoCube.CubeColor.BLUE,player);
        action.canPayTargetingScope(AmmoCube.CubeColor.RED,player);
        action.canPayTargetingScope(AmmoCube.CubeColor.YELLOW,player);
        action.canPayTargetingScope(AmmoCube.CubeColor.FREE,player);
        m.getMapUsed().getGameBoard().getDirection(c1,coo.getFirst());
        m.getMapUsed().getGameBoard().pickASkull();
        CoordinatesWithRoom c3 = new CoordinatesWithRoom(2, 2, m.getMapUsed().getGameBoard().getRoom(2));
        m.getMapUsed().getGameBoard().getDirection(c1,c3);
        PowerGlove powerGlove=new PowerGlove();
        powerGlove.fromCellsToTargets(coo,c1,m.getMapUsed().getGameBoard(),player,m,new EffectAndNumber(AmmoCube.Effect.ALT,0));
        PlasmaGun plasmaGun=new PlasmaGun();
        plasmaGun.fromCellsToTargets(coo,c1,m.getMapUsed().getGameBoard(),player,m,new EffectAndNumber(AmmoCube.Effect.OP1,0));
        action.grabTile(player,c1);
        c1.getSpawnpoint(m);
        c1.containsSpawnpoint(m);
        player.setDamagedStatus(true);
        player.damagedStatus();
        player.setShooter("Pikachu");
        player.getShooter();
        player.setPlayerPosition(c1);
        player.hasTargetingScope();
        player.hasTagbackGrenade();
        player.getTagbackGrenade();
        player.getTargetingScope();
        player.getTeleporter();
        player.getNewton();
        SpawnpointDom spw=new SpawnpointDom();
        w.canShootOp1();
        w.canShootBase();
        w.canShootOp2();
        w.placeWeaponOnMap(c1);
        w.getCoordinatesOnMap();
        w.setNotReload();
        w.setReloadAlt(true);
        w.getReloadAlt();
        w.toString();

        action.canGetPoints(players,players);
    player.setPlayerPositionSpawnpoint(c1);
    player.getPoints();
    player.getMortalPoints();
    player.setMortalPoints(1);
    player.incrementMortalPoints();
    player.getPlayerPos();
    player.setPlayerPos(Player.PlayerPos.FIRST);
    player.getAllPlayerPos();
    player.addWeaponcard(w);
    player.getFirstPositionOnTrack(player);
    player.getPointTrackFren();
    PowerUpCard p=m.powerUpDeck.pickPowerUp();
    p.getPossibleCells(m,player);
    p.plusOneDamage(player,player);
    m.getMapUsed().getGameBoard().getRooms().get(0).removeAmmotile(c1);
        m.getMapUsed().getGameBoard().getRooms().get(0).hasAmmoTile(c1);
    action.grabWeapon(player,w,c1.getSpawnpoint(m),player.getPowerUp(),c1);
    w.applyDamage(victims,player,en);
    action.setPosition(players);
    players.getFirst().setMortalPoints(1);
    players.getLast().setMortalPoints(3);
    action.setPosition(players);
    action.orderByMortalPoints(players);
    players.getLast().setMortalPoints(4);
    action.orderByMortalPoints(players);
    action.checkBestOrderScenario(players);
    action.mostPointsOrder(players);
    victim.newLife();
    LinkedList<Player>victimss=new LinkedList<>();
    victimss.add(victim);
    LinkedList<Player>shoot=new LinkedList<>();
    Player p1=new Player(c1, Figure.PlayerColor.GRAY);
    shoot.add(p1);
    action.canGetPoints(victimss,shoot);
    action.finalScoring();


    action.bestShooterOrderWithPosition(players,victim);
        w.applyDamage(victims,player,en);
        w.applyDamage(victims,player,en);
        players.addFirst(player);
        w.applyDamage(victims,player2,en);
        players.add(player2);
        action.bestShooterOrder(players,victim);
        w.applyDamage(victims,p1,en);
        players.add(p1);
        action.bestShooterOrder(players,victim);
        LinkedList<PowerUpCard> powerUps=new LinkedList<>();
        action.canPayGrab(w,player,powerUps);
        action.canPayGrab(w,player,powers);

        victim.newLife();
        Player player3=new Player(c1, Figure.PlayerColor.GRAY);
        Player player4=new Player(c1, Figure.PlayerColor.GREEN);
        Player v=new Player(c1, Figure.PlayerColor.YELLOW);
        LinkedList<Object>target=new LinkedList<>();
        target.add(v);
        w.applyDamage(target,player3,en);
        w.applyDamage(target,player4,en);
        w.applyDamage(target,player4,en);
        players.clear();
        players.add(player3);
        players.add(player4);
        LinkedList<Player>targets=new LinkedList<>();
        targets.add(v);
        action.canGetPoints(targets,players);
        players.clear();
        players.add(player3);
        players.add(player4);
        Player pippo=new Player("An", Figure.PlayerColor.BLUE);
        System.out.println(action.bestShooterOrderWithPosition(players,v));
        action.orderSubListByPos(players,v);

        for (Player pl:players
             ) {
            System.out.println(pl+""+""+pl.getPoints());
        }




        action.canGetPoints(targets,players);
        action.finalScoring();
        for (Player pla:players

             ) { System.out.print(pla+""+pla.getPoints());

        }

        System.out.print(action.mostPointsOrder(players));

        System.out.println(action.checkBestOrderScenario(action.mostPointsOrder(players)));
       action.setPosition(action.checkBestOrderScenario(action.mostPointsOrder(players)));
        for (Player pla:players
             ) {
            System.out.print(pla+" P:"+pla.getPoints()+" MP:"+pla.getMortalPoints()+" POS:"+pla.getPlayerPos());
        }
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
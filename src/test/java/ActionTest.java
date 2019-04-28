import adrenaline.*;
import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.Electroscythe;
import adrenaline.weapons.Thor;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import java.util.LinkedList;

import static adrenaline.AmmoCube.Effect.ALT;
import static adrenaline.AmmoCube.Effect.BASE;
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
@Test
    public void grabTile(){
    Map map = new MapFour(DEATHMATCH);
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player player=new Player(c,Figure.PlayerColor.GRAY);
        AmmoTile toBeGrabbedTile=new AmmoTile(c, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.RED, AmmoCube.CubeColor.YELLOW);
        // grab ammo or powerUp

        //TODO a way to convert propose to grab cells in AmmoTile
        PowerUpCard toBeGrabbedPowerUp=null;
        if(toBeGrabbedTile.getAmmoTile().get(0)!=null&&toBeGrabbedTile.getAmmoTile().get(1)==null&&toBeGrabbedTile.getAmmoTile().get(2)==null)
            //is a powerUp
           ;

        if (toBeGrabbedTile.getAmmoTile().get(1) != null || toBeGrabbedTile.getAmmoTile().get(2) != null )
           ;



    }
    @Test
    public void grabCube( ){
        Map map = new MapFour(DEATHMATCH);
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player player=new Player(c,Figure.PlayerColor.GRAY);
        AmmoTile a=new AmmoTile(c, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW, AmmoCube.CubeColor.YELLOW);
      //  player.setCube(2,1,0);

        for(int i=0;i<3;i++)
        {
            if(player.getAmmoBox()[i]>=3)
                ;
            System.out.println(player.getAmmoBox()[i]);
        }
        for(int i=0;i<3;i++)
        {
            switch (a.getAmmoTile().get(i).getCubeColor())


                {
                    case YELLOW:if(player.getCubeYellow()<3)
                        player.setCube(0,0,1);break;
                    case BLUE:if(player.getCubeBlue()<3)
                        player.setCube(0,1,0);break;

                    case RED:
                        player.setCube(1,0,0);break;


            }

        }
//BLUE RED YELLOW
        System.out.println(player.getAmmoBox()[0]+"\n"+player.getAmmoBox()[1]+"\n"+player.getAmmoBox()[2]);
    }
    @Test
    public void grabPowerUp(){
        Map map = new MapFour(DEATHMATCH);
        PowerUpCard a=new PowerUpCard();
        a.setPowerUpColor(AmmoCube.CubeColor.BLUE);
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player p=new Player(c,Figure.PlayerColor.GRAY);
        if(!p.canGrabPowerUp()) return;

        System.out.println(p.getPowerUp());
        p.getPowerUp().add(a);
        System.out.println(p.getPowerUp());
    }
    @Test
    public void shoot( ) {
        WeaponCard w=new Cyberblade();
        LinkedList<EffectAndNumber> effectsList=new LinkedList<>();
        EffectAndNumber effectAndNumber=new EffectAndNumber(BASE,0);
        effectsList.add(0,effectAndNumber);
        effectsList.get(0).setEffect(BASE);
        Map map = new MapFour(DEATHMATCH);
        GameModel m=new GameModel(DEATHMATCH,null,1);
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player p=new Player(c,Figure.PlayerColor.GRAY);
        Player victim=new Player(c,Figure.PlayerColor.BLUE);
        GameBoard g=map.getGameBoard();
        p.setPlayerPosition(c.getX(),c.getY(),c.getRoom());
        EffectAndNumber effectNumber=null;
        for(int index=0;index<effectsList.size();index++) {
            ///todo choose effect order
            switch (effectsList.get(index).getEffect()) {
                case BASE: effectNumber = new EffectAndNumber(AmmoCube.Effect.BASE, 0);
                case ALT: effectNumber = new EffectAndNumber(ALT, 0);
                case OP1: effectNumber = new EffectAndNumber(AmmoCube.Effect.OP1, 0);
                case OP2: effectNumber = new EffectAndNumber(AmmoCube.Effect.OP2, 0);
            }
          //  LinkedList<CoordinatesWithRoom> target= w.getPossibleTargetCells(c, effectNumber, g);
          //  w.fromCellsToTargets(target,c,g,p,m,effectNumber);
            //here controller gives back choosen opponents
            LinkedList<Object>effectiveTarget=new LinkedList<>();

            effectiveTarget.add(0,victim);
            w.weaponShoot(effectiveTarget,c,p,effectsList,m);
        }}
@Test
    public void reload() {
        Map map = new MapFour(DEATHMATCH);
        WeaponCard w=new Thor();
        Action.PayOption option=Action.PayOption.AMMO;
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player p=new Player(c,Figure.PlayerColor.GRAY);

        switch(option){
            case AMMO:break;

            case AMMOPOWER:break;

        }

    }
    @Test
    public void reloadAmmoPower(){
        Map map = new MapFour(DEATHMATCH);
        WeaponCard weapon=new Thor();
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player player=new Player(c,Figure.PlayerColor.GRAY);

        System.out.println(player.getAmmoBox());

        LinkedList<PowerUpCard>wallet=new LinkedList<>();
        PowerUpCard powerUp=new PowerUpCard();
        powerUp.setPowerUpColor(AmmoCube.CubeColor.BLUE);
        wallet.addFirst(powerUp);

        System.out.println(wallet);

        int redCube=player.getCubeRed();
        int blueCube=player.getCubeBlue();
        int yellowCube=player.getCubeYellow();
        System.out.println(player.getCubeBlue());
        for(int i=0;i<wallet.size();i++){
            switch (wallet.get(i).getPowerUpColor()){
                case RED: redCube++;break;
                case YELLOW:yellowCube++;break;
                case BLUE:blueCube++;break;
            }
        }
        System.out.println(blueCube);


        for(int i=0;i<wallet.size();i++)
            player.getPowerUp().remove(wallet.get(i));
        System.out.println(wallet);
    }


}


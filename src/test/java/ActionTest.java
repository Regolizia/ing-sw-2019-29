import adrenaline.*;
import adrenaline.weapons.Cyberblade;
import adrenaline.weapons.Electroscythe;
import adrenaline.weapons.Thor;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import java.util.LinkedList;

import static adrenaline.AmmoCube.CubeColor.*;
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
        AmmoTile toBeGrabbedTile=new AmmoTile(c, AmmoCube.CubeColor.BLUE, AmmoCube.CubeColor.RED, YELLOW);
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
        AmmoTile a=new AmmoTile(c, YELLOW, YELLOW, YELLOW);
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
@Test
    public void reloadAmmo(){
        Map map = new MapFour(DEATHMATCH);
        WeaponCard w=new Thor();
        LinkedList<AmmoCube> price;
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player p=new Player(c,Figure.PlayerColor.GRAY);
        int red=3;
        int blue=3;
        int yellow=3;
        price=w.getPrice();
        System.out.println(price.get(0)+""+price.get(1)+""+price.get(2));
        int blueToPay=0;
        int yellowToPay=0;
        int redToPay=0;
        for (int i = 0; i < price.size(); i++) {
            if (price.get(i).getEffect() == AmmoCube.Effect.BASE) {
                if (price.get(i).getCubeColor() == AmmoCube.CubeColor.BLUE)
                    blueToPay++;
                if (price.get(i).getCubeColor() == AmmoCube.CubeColor.RED)
                    redToPay++;
                if (price.get(i).getCubeColor() == AmmoCube.CubeColor.YELLOW)
                    yellowToPay++;
            }
        }
        if(blue-blueToPay<0||red-redToPay<0||yellow-yellowToPay<0) {
            w.setNotReload();


        }
        for (int i = 0; i < price.size(); i++) {
            if (price.get(i).getEffect() == AmmoCube.Effect.BASE) {
                price.get(i).setPaid(true);
            }

            p.getAmmoBox()[0] = blue-blueToPay;
            p.getAmmoBox()[1] = red-redToPay;
            p.getAmmoBox()[2] = yellow-yellowToPay;
            w.setReload();

        }
        }
@Test
    public void chooseWeaponCard() {
        int j;
    Map map = new MapFour(DEATHMATCH);
    WeaponCard w=new Thor();
    CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
    Player p=new Player(c,Figure.PlayerColor.GRAY);
    LinkedList<WeaponCard>hand=new LinkedList<>();
    hand.addFirst(w);
    }
@Test
    public void canPayCard() {

        //todo ask pay option: only ammo or ammo+power-up
        Map map = new MapFour(DEATHMATCH);
        WeaponCard weapon=new Thor();
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player player=new Player(c,Figure.PlayerColor.GRAY);
        Action.PayOption option= Action.PayOption.AMMO;
        switch(option) {

            case AMMOPOWER: {

            }

            case AMMO: {

            }
        }
    }
@Test
    public void canPayAmmo() {
        Map map = new MapFour(DEATHMATCH);
        WeaponCard weapon=new Thor();
        CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player player=new Player(c,Figure.PlayerColor.GRAY);
        LinkedList<AmmoCube> cost = weapon.getPrice();
        int red=3;int yellow=2;int blue=2;
        int i;
        int redToPay=0;
        int blueToPay=0;
        int yellowToPay=0;
        boolean no = false;
        if(weapon.getReload())
        //can you pay base effect or you can pay alt
        for (i = 0; i < cost.size(); i++) {
            if (((cost.get(i).getEffect().equals(AmmoCube.Effect.BASE) || cost.get(i).getEffect().equals(AmmoCube.Effect.ALT)) && !weapon.getReload()
            )) {

                for (int j = 0; j < 2; j++) {

                    if (cost.get(i).getEffect().equals(cost.get(j).getEffect())) {


                        switch (cost.get(i).getCubeColor()) {
                            case RED:
                                redToPay++;
                                break;

                            case BLUE:

                                blueToPay++;
                                break;

                            case YELLOW:

                                yellowToPay++;
                                break;
                        }


                    }

                }

            }
            if(yellow-yellowToPay<0||red-redToPay<0||blue-blueToPay<0);

        }
        }

        @Test
        public void canPayAmmoPower(){
            Map map = new MapFour(DEATHMATCH);
            WeaponCard weapon=new Thor();
            CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
            Player player=new Player(c,Figure.PlayerColor.GRAY);
            LinkedList<PowerUpCard>powerUpCards=new LinkedList<>();
            PowerUpCard power=new PowerUpCard();
            power.setPowerUpColor(BLUE );
            powerUpCards.addFirst(power);
            int redPower=0;
            int bluePower=0;
            int yellowPower=0;
            for(int i=0;i<powerUpCards.size();i++)
            {
                switch(powerUpCards.get(i).getPowerUpColor())
                {
                    case RED: redPower++;
                        break;
                    case BLUE: bluePower++;
                        break;
                    case YELLOW: yellowPower++;
                        break;
                }
            }
            int redCube=player.getCubeRed()+redPower;
            int blueCube=player.getCubeBlue()+bluePower;
            int yellowCube=player.getCubeYellow()+yellowPower;


        }
        @Test
    public void choosePowerUp(){
        int j=0;
            Map map = new MapFour(DEATHMATCH);

            CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
            Player player=new Player(c,Figure.PlayerColor.GRAY);
        LinkedList<PowerUpCard>power=player.getPowerUp();
        LinkedList<PowerUpCard>powerChoosen=new LinkedList<>();
            PowerUpCard e=new PowerUpCard();
            e.setPowerUpColor(RED);
        powerChoosen.addFirst(e);
        }
        @Test

    public void paidEffect() {
            Map map = new MapFour(DEATHMATCH);
            WeaponCard weapon=new Thor();
            CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
            Player player=new Player(c,Figure.PlayerColor.GRAY);
            Action.PayOption option= Action.PayOption.AMMO;
            switch (option) {
                case AMMO:

                case AMMOPOWER:

            }
        }


@Test
    public void payAmmoPlusPowerUp(){
    Map map = new MapFour(DEATHMATCH);
    WeaponCard weapon=new Thor();
    CoordinatesWithRoom c = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
    Player player=new Player(c,Figure.PlayerColor.GRAY);
    Action.PayOption option= Action.PayOption.AMMO;
    LinkedList<AmmoCube>price=weapon.getPrice();
    LinkedList<EffectAndNumber> paid=new LinkedList<>();
    EffectAndNumber effectAndNumber=new EffectAndNumber(BASE,2);
    paid.addFirst(effectAndNumber);
    EffectAndNumber effectAndNumberToAdd;

        if(!weapon.getReload()){
            for (int i = 0; i < 1; i++) {        //missing choose your effect base/alt here you source option position
                for (int j = 0; j < 2; j++) {
                    if (price.get(i).getEffect().equals(price.get(j).getEffect())&&((price.get(i).getEffect().equals(AmmoCube.Effect.BASE))||
                            (price.get(i).getEffect().equals(AmmoCube.Effect.ALT)))&&!weapon.getReload()) {

                    }
                }
                paid.get(0).setEffect(price.get(i).getEffect()); break;  // i can pay only one
            }}
        if(weapon.getReload()){
            paid.get(0).setEffect(AmmoCube.Effect.BASE);
        }

        //now pay options

        for (int i = 0,k=1; i < weapon.getPrice().size(); i++,k++) {
            for (int j = 0; j < 2; j++) {
                if (price.get(i).getEffect().equals(price.get(j).getEffect()) && (price.get(j).getEffect() == AmmoCube.Effect.OP1 ||
                        price.get(j).getEffect() == AmmoCube.Effect.OP2)) {

                }


            }
            effectAndNumberToAdd=new EffectAndNumber(price.get(i).getEffect(),0);
        paid.add(k,effectAndNumber);
        }




    }
    }




package adrenaline;

import adrenaline.gameboard.Map;
import adrenaline.gameboard.MapFour;
import adrenaline.weapons.Flamethrower;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class Player2Test {

/*player creation plus some of its methods*/
    @Test
    public void testConstructor() {
        WeaponCard weapon=null;
        Map map = new MapFour(DEATHMATCH);
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player p = new Player(c1, Figure.PlayerColor.GRAY);
        Room a = p.getPlayerRoom();
        p.getHand().add(new Flamethrower());
       // boolean b = weapon.getReload();


        for(int i=0;i<p.getHand().get(0).getPrice().size();i++) {
            if(p.getHand().get(0).getPrice().get(i).getEffect()== AmmoCube.Effect.BASE){
                p.getHand().get(0).getPrice().get(i).setPaid(false);
            }
        }

       // boolean r = weapon.getReload();
        p.getAmmoBox();
        p.setPoints(2);
        p.getPointTrack();
        p.getTrackPointSize();
        p.getTrackSize();
        p.getMaxPointAssignableCounter();
        p.setMaxPointAssignableCounter(1);
        p.putASkullOnTrack();
        p.isFirstTurn();
        p.returnPoints();
        //testing respawn
        p.damageByShooter(p);
        p.damageByShooter(p);
        p.damageByShooter(p);
        p.damageByShooter(p);
        p.damageByShooter(p);
        p.damageByShooter(p);
        p.damageByShooter(p);
        p.damageByShooter(p);
        p.numberOfDeaths();
        p.setName("PLUTO");
        p.getName();
        p.getRespawnCoordinates();
        p.newLife();
        p.getMarks();

    }
}

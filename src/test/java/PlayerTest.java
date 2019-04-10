import adrenaline.*;
import adrenaline.weapons.Flamethrower;
import org.junit.jupiter.api.Test;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class PlayerTest {

    @Test
    public void testConstructor() {

        Map map = new MapFour(DEATHMATCH);
        CoordinatesWithRoom c1 = new CoordinatesWithRoom(1,1,map.getGameBoard().getRoom(0));
        Player p = new Player(c1, Figure.PlayerColor.GRAY);
        Room a = p.getPlayerRoom();
        p.getHand().add(new Flamethrower());
        boolean b = p.isLoaded(p.getHand().get(0));


        for(int i=0;i<p.getHand().get(0).getPrice().size();i++) {
            if(p.getHand().get(0).getPrice().get(i).getEffect()== AmmoCube.Effect.BASE){
                p.getHand().get(0).getPrice().get(i).setPaid(false);
            }
        }

        boolean r = p.isLoaded(p.getHand().get(0));
        p.getAmmoBox();

    }
}

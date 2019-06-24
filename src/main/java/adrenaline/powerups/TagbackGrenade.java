package adrenaline.powerups;

import adrenaline.AmmoCube;
import adrenaline.Player;
import adrenaline.PowerUpCard;

public class TagbackGrenade extends PowerUpCard {

    /**
     * Default constructor
     */
    public TagbackGrenade(AmmoCube.CubeColor color) {
        setPowerUpColor(color);
        setCanBeUsedOnBot(true);
    }

    @Override
    public String toString() {
        return "TagbackGrenade, "+getPowerUpColor();
    }

    /**
     *giveMark
     * @param player
     * @param victim
     * method to give a mark
     */
    public void giveMark(Player player, Player victim){
        victim.addMarks(player,1);
    }
}
package adrenaline.powerups;

import adrenaline.AmmoCube;
import adrenaline.Player;
import adrenaline.PowerUpCard;
import adrenaline.Spawnpoint;

import java.util.LinkedList;

public class TargetingScope  extends PowerUpCard {

    /**
     * Default constructor
     */
    public TargetingScope(){
        setCanBeUsedOnBot(false);
    }
    public TargetingScope(AmmoCube.CubeColor color) {
        setPowerUpColor(color);
        setCanBeUsedOnBot(false);
    }

    @Override
    public String toString() {
        return "TargetingScope, "+getPowerUpColor();
    }

    /**
     *plusOneDamage
     * @param player
     * @param target
     * a method to give one additional damage when used Targeting scope
     */
    public void plusOneDamage(Player player,Object target){
        if(target instanceof Player)
            ((Player) target).damageByShooter(player);

    }
}
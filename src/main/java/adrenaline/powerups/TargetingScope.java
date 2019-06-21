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
    }

    @Override
    public String toString() {
        return "TargetingScope, "+getPowerUpColor();
    }

    public void plusOneDamage(Player player,Object target){
        if(target instanceof Player)
            ((Player) target).damageByShooter(player);

    }
}
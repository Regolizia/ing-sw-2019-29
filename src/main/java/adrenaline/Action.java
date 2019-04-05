package adrenaline;

public class Action {

WeaponCard weapon;

public String getName(){ return "";}



public void shoot(Player player, WeaponCard weapon)
{
    // /*/ref options list*/ checkPayment(Player player, WeaponCard weapon);  checkPayment is in WeaponCard
    // /*/ref possible target list*/ canAim(Player player, // ref options list );
    // /*/ref target list/ aimTarget(ref possible target);
    // check if target=spawnpoint or target=player

    //damage(ref target list, WeaponCard weapon, ref options list) //todo check on target in damage

}

public void run(Player player /*,finalPosition*/){
    player.getLocation(player);
    // CHECK IF CAN RUN
    //DO RUN
    player.setPos(player);
}

public void grabHere(Player player){
    player.getLocation(player);
    // do move
    player.setPos(player);
}

public void grabThere(Player player /*,finalPosition*/){
        player.getLocation(player);
        // CHECK IF CAN MOVE
        //DO MOVE
        //SET POS
        player.setPos(player);
        //DO MOVE
        player.getCard(player);
    }
    public void reload(Player player)
    {
        /*weapon=*/player.getWeapon(player);


    }

    }


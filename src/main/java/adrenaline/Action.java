package adrenaline;

public class Action {



public String getName(){ return "";}

public void Shoot(Player player, WeaponCard weapon)
{
    // /*/ref options list*/ checkPayment(Player player, WeaponCard weapon);  checkPayment is in WeaponCard
    // /*/ref possible target list*/ canAim(Player player, // ref options list );
    // /*/ref target list/ aimTarget(ref possible target);
    // check if target=spawnpoint or target=player

    //damage(ref target list, WeaponCard weapon, ref options list) //todo check on target in damage

}

public void Run(Player player /*,finalPosition*/){
    player.getLocation(player);
    // CHECK IF CAN RUN
    //DO RUN
    // RETURN NEW POS
}
}

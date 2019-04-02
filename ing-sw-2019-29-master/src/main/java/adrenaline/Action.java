package adrenaline;

public class Action {



public String getName(){ return "";}


public void shoot(Player player, WeaponCard weapon ){

    //todo checkPayment()
   //public "option" checkPayment(Player player, WeaponCard weapon);

                    //options: base effect, base and optional effects, another option.

    //todo aimTarget
    //public "refTarget" aimTarget( Option option  or integer option?)

    //todo giveDamage
    //public void damage(refTarget)


    // you can only give a mark if an options tells you that
    // if you shoot to a target with 11 bloodTokens, you will get a mark.
}

public void shoot(Player player, Spawnpoint spawnpoint){
    // only in domination mode
    // just a damage at time
    // spawnpoint does respond
    // spawnpoint doesn't attack its owner
}


public boolean canGrab(Player player /*initial Pos, final Pos*/)
// final POS = spawpoint pos? grab without move if initialPos=finalPos
{
    //if canGrab is valid
    return true;
    //if canGrab isn't valid
    //return false;
}

public void grabAmmo(Player player /*ammoTilePos*/)
{

}
public void grabWeapon(Player player, Spawnpoint pos)
{
    // can I pay?
    // How many card? > trow a card
    // pick up choosen card and put throwed card instead of it
    // Choosen weapon is a bit charged
}


}

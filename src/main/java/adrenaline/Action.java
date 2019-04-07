package adrenaline;

public class Action {

//WeaponCard weapon;

    public static enum ActionType {
        GRAB, RUN, SHOOT, ADRENALINEGRAB, ADRENALINESHOOT;
    }

    private ActionType actionSelected;


public void shoot(Player player, WeaponCard weapon)
    {
    if (player.checkDamage(player)==2)
    {shootAdrenaline(player, weapon);
            return;}

    // /*/ref options list*/ checkPayment(Player player, WeaponCard weapon);  checkPayment is in WeaponCard
    // /*/ref possible target list*/ canAim(Player player, // ref options list );
    // /*/ref target list/ aimTarget(ref possible target);
    // check if target=spawnpoint or target=player
    //damage(ref target list, WeaponCard weapon, ref options list) //todo check on target in damage
    }

public void shootAdrenaline(Player player, WeaponCard weapon)
    {
        // /*/ref options list*/ checkPayment(Player player, WeaponCard weapon);  checkPayment is in WeaponCard
        // /*/ref possible target list*/ canAim(Player player, // ref options list );
        // /*/ref target list/ aimTarget(ref possible target);
        // check if target=spawnpoint or target=player
        //shoot+adrenaline option
        //damage(ref target list, WeaponCard weapon, ref options list) //todo check on target in damage

    }

public void grab(Player player /*final position*/)
    {
        player.getLocation(player);
        if (player.checkDamage(player)==2||player.checkDamage(player)==1) {
            grabAdrenaline(player /*location*/);
            return;
            }
        // CHECK IF CAN MOVE
        //DO MOVE
        //SET POS
        player.setPos(player);
        //DO MOVE
        player.getCard(player);
    }
public void grabAdrenaline(Player player /*final position*/)
    {
    // CHECK IF CAN MOVE
    //DO MOVE
    //SET POS
    player.setPos(player);
    //DO MOVE
    player.getCard(player);
    }

public void run(Player player /*,finalPosition*/)
    {
    player.getLocation(player);
    // CHECK IF CAN RUN
    //DO RUN
    player.setPos(player);
    }
public void reload(Player player)
    {
        /*weapon=*/player.getWeapon(player);

    }
    /*todo frenzyShoot frenzyRun frenzyGrab*/


    public ActionType getActionSelected() {
        return actionSelected;
    }
    public void setActionSelected(ActionType a){
        this.actionSelected = a;
    }



    }


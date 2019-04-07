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

public void grab(Player player , int xF , int yF)
    {


        // CHECK IF CAN MOVE: move only a square up/down or a square left/right
        if((((player.getPosX(player)-xF==1)||(player.getPosX(player)-xF==-1))&& player.getPosY(player)-yF==0)||((player.getPosX(player)-xF==0)&&(player.getPosY(player)-yF==0)))
        {//check if there is a wall, if not
            player.setPos(xF,yF,player);
            //method to pickup the card
            return;
            }
        else if (player.checkDamage(player)==2||player.checkDamage(player)==1) {
        grabAdrenaline(player,xF,yF);

    }
    else  System.out.println("movimento non valido");
    }
public void grabAdrenaline(Player player, int x, int y)
    {   //4 options: down+left down+right up+left up+ right
        // plus two up, two down, two left, two right
        if((player.getPosX(player)-x==2&& player.getPosY(player)-y==0)||(player.getPosX(player)-x==-2&& player.getPosY(player)-y==0)
        ||(player.getPosX(player)-x==0&& player.getPosY(player)-y==2)||(player.getPosX(player)-x==0&& player.getPosY(player)-y==-2))
        {//check if there is a wall, if not
            player.setPos(x,y,player);
            //method to pickup the card
        }
        if((player.getPosX(player)-x==1&& player.getPosY(player)-y==1)||(player.getPosX(player)-x==-1&& player.getPosY(player)-y==1)
                ||(player.getPosX(player)-x==1&& player.getPosY(player)-y==-1)||(player.getPosX(player)-x==-1&& player.getPosY(player)-y==-1))
        {//check if there is a wall, if not
            player.setPos(x,y,player);
            //method to pickup the card
        }
        else System.out.println("movimento non valido");
    }

public void run(Player player, int xF,int yF)
    {
        // can move up/down or left/right max 3 square
       if ((player.getPosX(player)-xF==2||player.getPosX(player)-xF==-2)&&(player.getPosY(player)-yF==1||player.getPosY(player)-yF==-1))
           player.setPos(xF,yF,player);
       else if((player.getPosY(player)-yF==2||player.getPosY(player)-yF==-2)&&(player.getPosX(player)-xF==1||player.getPosX(player)-xF==-1))
           player.setPos(xF,yF,player);
       else if(((player.getPosX(player)-xF==3||player.getPosX(player)-xF==-3)&&player.getPosY(player)-yF==0)||
               ((player.getPosY(player)-yF==3||player.getPosY(player)-yF==-3)&&player.getPosX(player)-xF==0))
           player.setPos(xF,yF,player);
    }
public void reload(Player player)
    {
        /*weapon=*/player.getWeaponCard(player);

    }
    /*todo frenzyShoot frenzyRun frenzyGrab*/


    public ActionType getActionSelected() {
        return actionSelected;
    }
    public void setActionSelected(ActionType a){
        this.actionSelected = a;
    }



    }


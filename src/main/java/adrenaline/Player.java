package adrenaline;

import java.util.LinkedList;

public class Player {

  public Figure.PlayerColor track[];
  public int ammoBox[];
  int x;
  int y;
  public WeaponCard hand[];
  public PowerUpCard powerups[];

    public Player(){

        ammoBox = new int[]{1, 1, 1}; //BLUE RED YELLOW
        track= new Figure.PlayerColor[]{null,null,null,null,null,null,null,null,null,null,null,null};
        // WE CAN ALSO CHOOSE TO PUT "NONE" VALUE IN ENUM AND SET IT HERE. WITH NULL WE CANNOT USE SWITCH CASE

        hand = new WeaponCard[]{null,null,null};
        powerups = new PowerUpCard[]{null,null,null};
    }

    // TODO IMPLEMENT PLAYER(COORDINATES) TO RESPAWNPLAYER ON SPAWNPOINT
    // MAYBE WE DON'T NEED PLAYER(), WE HAVE TO PUT IT SOMEWHERE

    // what does it do?
    public int getLife(Player player){

        return 0;
    }

    public int checkDamage(Player player){

        if(track[2]!=null && track[5]==null)
            return 1;       //better Grab
        if(track[5]!=null)
            return 2;       //better Shoot (and Grab)
        return 0;

    };


    
    public void setToken(Player player){}

    public String getToken(Player player){return "";} //adrenaline.Token

    public void /*locations*/getLocation(Player player){}

    public void  setPos(Player player){}

    public void /*pos*/ getPos(Player player){}
    public void /*card*/getCard(Player player){}
    public void /* WeaponCard*/ getWeapon(Player player){

        //return weapon;
    };


}


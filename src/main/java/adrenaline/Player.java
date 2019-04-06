package adrenaline;

public class Player {

   int track[];

 int ammoBox[];


    public Player(){

        ammoBox = new int[]{1, 1, 1};
        track=new int[]{0,0,0,0,0,0,0,0,0,0,0,0};

    }


    public int getLife(Player player){

        return 0;
    }

    public int checkDamage(Player player){

        if(track[2]==1&&track[5]==0)
            return 1;       //better Grab
        if(track[5]==0)
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


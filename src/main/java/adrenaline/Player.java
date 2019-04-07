package adrenaline;

import java.util.LinkedList;

public class Player {

  private Figure.PlayerColor track[];
  private int ammoBox[];
  private int x;
  private int y;
  private Figure.PlayerColor color;
  private int spwX,spwY;

  private LinkedList<WeaponCard> hand;
  private LinkedList<PowerUpCard> powerups;



    public Player(int spwX,int spwY, Figure.PlayerColor player){

        ammoBox = new int[]{1, 1, 1}; //BLUE RED YELLOW
        track= new Figure.PlayerColor[]{Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE
                ,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE
                ,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE};
        // PUTTING "NONE" VALUE SO WE CAN USE SWITCH CASE
        color=player;
        this.spwX=spwX;
        this.spwY=spwY;
        x=spwX;
        y=spwY;
        // they are lists because we need to add and remove easily
        hand = new LinkedList<WeaponCard>();
        powerups = new LinkedList<PowerUpCard>();
    }

    // TODO IMPLEMENT PLAYER(COORDINATES) TO RESPAWNPLAYER ON SPAWNPOINT


    public int checkDamage(Player player){

        if(track[2]!=Figure.PlayerColor.NONE && track[5]==Figure.PlayerColor.NONE)
            return 1;       //better Grab
        if(track[5]!=Figure.PlayerColor.NONE)
            return 2;       //better Shoot (and Grab)
        return 0;
    };
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean canGrabPowerUp(){
        return(powerups.get(2)==null);
    }

    public boolean canGrabWeapon(){
        return(hand.get(2)==null);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   // public void setToken(Player player){}

   // public String getToken(Player player){return "";} //adrenaline.Token

 //________________________to controll player's position____________________________________________________//
    public void setPos(int x, int y,Player player){
        this.x=x;
        this.y=y;
    }

    public int getPosX(Player player){
        return x;
    }

    public int getPosY(Player player){
        return y;
    }
//_________________________________________________________________________________________________________//



    public void pickWeaponCard(Player player, int x, int y){
        //metod to find the card in that position


    }

    public WeaponCard getWeaponCard(Player player){
        //metod to find the card in that position
        //shows a list of owned card
        //return only a card
        WeaponCard card=new WeaponCard();
        return card;
    }



}


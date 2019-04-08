package adrenaline;

import java.util.LinkedList;

public class Player {

  private Figure.PlayerColor track[];
  private int ammoBox[];
  private Position position;
  private Figure.PlayerColor color;
  private Position respawnPosition;

  private LinkedList<WeaponCard> hand;
  private LinkedList<PowerUpCard> powerups;

    public Player(){

    }
    // Position rsp is the spawnPosition chosen by the player
    public Player(Position rsp, Figure.PlayerColor playercolor){

        this.ammoBox = new int[]{1, 1, 1}; //BLUE RED YELLOW
        this.track= new Figure.PlayerColor[]{Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE
                ,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE
                ,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE,Figure.PlayerColor.NONE};
        // PUTTING "NONE" VALUE SO WE CAN USE SWITCH CASE
        this.color=playercolor;
        this.respawnPosition = rsp;
        this.position = rsp;
        // they are lists because we need to add and remove easily
        this.hand = new LinkedList<WeaponCard>();
        this.powerups = new LinkedList<PowerUpCard>();
    }

    // TODO IMPLEMENT PLAYER(COORDINATES) TO RESPAWNPLAYER ON SPAWNPOINT


    public int checkDamage(){

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

 //________________________to control player's position____________________________________________________//
    public void setPlayerPosition(int x, int y){
        this.position.setPosition(x,y);
    }

    public int getPlayerPositionX(){

        return position.getX();
    }

    public int getPlayerPositionY(){
        return position.getY();
    }
//_________________________________________________________________________________________________________//


    // I DON'T THINK IT SHOULD BE HERE
    public void pickWeaponCard(int x, int y){
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


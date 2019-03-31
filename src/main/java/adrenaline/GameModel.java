package adrenaline;

public class GameModel {


    Player next=new Player();
    protected int playerCount;
  //  protected String[] players = new String[5];;
    protected String mode;
    protected Map mapUsed=new Map();


    public GameModel(Map map,String m){
        mapUsed=map;
        mode=m;
    }




    public Player nextPlayer(Player player){


      //  return next.getToken(next);*/   //todo list
        return next;
    }

  public void setTurn(){};

    public void getTurn(){



    }












}

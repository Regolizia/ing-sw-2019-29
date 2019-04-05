package adrenaline;

public class GameModel {


    Player next=new Player();
    protected int playerCount;
  //  protected String[] players = new String[5];;

    public static enum Mode {
        DEATHMATCH, DOMINATION;
    }
    protected Mode mode;
    protected Map mapUsed;


    public GameModel(Mode m) {
        mapUsed = new Map(m);
        mode = m;
    }




    public Player nextPlayer(Player player){


      //  return next.getToken(next);*/   //todo list
        return next;
    }

  public void setTurn(){};

    public void getTurn(){



    }












}

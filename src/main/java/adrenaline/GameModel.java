package adrenaline;

public class GameModel {


    Player next=new Player();
    protected int playerCount;
  //  protected String[] players = new String[5];;

    public static enum Mode {
        DEATHMATCH, DOMINATION;
    }
    public static enum Bot {
        BOT, NOBOT;
    }
    protected Mode mode;
    protected Map mapUsed;
    protected Bot bot;


    public GameModel(Mode m, Map ma, Bot b) {
        //TODO CHANGE INTO MAP1 MAP2 ETC...
        mapUsed = new Map(m);
        mode = m;
        bot = b;
    }




    public Player nextPlayer(Player player){


      //  return next.getToken(next);*/   //todo list
        return next;
    }

  public void setTurn(){};

    public void getTurn(){



    }












}

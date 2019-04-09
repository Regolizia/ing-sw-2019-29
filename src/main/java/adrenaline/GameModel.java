package adrenaline;

import java.util.LinkedList;

public class GameModel {

    LinkedList<Player> players;
    Player next = new Player();

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
        //TODO CHANGE INTO MAP1 MAP2 ETC... SWITCHCASE MAYBE FOR MODES AND CHOESEN MAP
        mapUsed = new Map(m);
        mode = m;
        bot = b;
    }




    public Player nextPlayer(Player player){
      //  return next.getToken(next);
        return next;
    }


/*

  public void setTurn(){};

    public void getTurn(){
*/



    }





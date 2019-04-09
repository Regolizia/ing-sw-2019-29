package adrenaline;

import java.util.ArrayList;
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


    public GameModel(Mode m, Bot b) {
        //TODO CHANGE INTO MAP1 MAP2 ETC... SWITCHCASE MAYBE FOR MODES AND CHOESEN MAP
        mapUsed = new Map(m);
        mode = m;
        bot = b;
    }

    public void addPlayer(Player p){
        players.add(p);
    }


    public Player nextPlayer(Player player){
      //  return next.getToken(next);
        return next;
    }


    public Map getMapUsed(){
        return mapUsed;
    }
    public void setMapUsed(Map m){
        this.mapUsed = m;
    }
/*
    public void assignDeathPoints(Player dead){


        ArrayList<ColorAndScore> array = new ArrayList<>();
        for(int i=0;i<players.size();i++){
            array.add(new ColorAndScore(players.get(i).getColor(),--------score-associato----))
        }
    }*/

/*

  public void setTurn(){};

    public void getTurn(){
*/



    }





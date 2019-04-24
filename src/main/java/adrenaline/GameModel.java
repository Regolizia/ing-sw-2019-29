package adrenaline;

import java.util.LinkedList;

public class GameModel {

    private LinkedList<Player> players;
    //privatePlayer next = new Player();

    public static enum Mode {
        DEATHMATCH, DOMINATION
    }
    public static enum Bot {
        BOT, NOBOT
    }

    protected Mode mode;
    protected Map mapUsed;
    protected Bot bot;


    public GameModel(Mode m, Bot b, int chosenMap) {

        players = new LinkedList<>();

        switch (chosenMap) {
            case 1:
                mapUsed = new MapOne(m);
                break;
            case 2:
                mapUsed = new MapTwo(m);
                break;
            case 3:
                mapUsed = new MapThree(m);
                break;
            case 4:
                mapUsed = new MapFour(m);
                break;
            default:
                System.out.printf("Invalid input");
                break;
        }
        mode = m;
        bot = b;
    }

    public void addPlayer(Player p){
        getPlayers().add(p);
    }

/*
    public Player nextPlayer(Player player){
      //  return next.getToken(next);
        return next;
    }*/

    public Map getMapUsed(){
        return mapUsed;
    }
/*    public void setMapUsed(Map m){
        this.mapUsed = m;
    }*/

    public LinkedList<Player> getPlayers(){
        return players;
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





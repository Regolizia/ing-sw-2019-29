package adrenaline;

import java.util.LinkedList;
import java.util.List;

/**
 * Is the class that represents a Deck.
 * It has:
 * <ul>
 *     <li> a list of Players
 *     <li> a Game Mode
 *     <li> a Map
 * </ul>
 *
 * @author Eleonora Toscano
 * @version 1.0
 */
public class GameModel {

    private LinkedList<Player> players;

    public enum Mode {
        DEATHMATCH, DOMINATION
    }
    public enum Bot {
        BOT, NOBOT
    }
    public enum FrenzyMode{
        ON,OFF
    }
    protected Mode mode;
    private Map mapUsed;
    protected Bot bot;


    /**
     * Constructor with Mode, Bot and choice of map.
     *
     * @param m the mode
     * @param b the bot choice
     * @param chosenMap the map chosen
     */
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
            default:
                mapUsed = new MapFour(m);
                break;
        }
        mode = m;
        bot = b;
    }

    /**
     * Adds a Player to the list.
     *
     * @param p a player to add
     */
    public void addPlayer(Player p){
        getPlayers().add(p);
    }

    public Map getMapUsed(){
        return mapUsed;
    }

    public List<Player> getPlayers(){
        return players;
    }

    }





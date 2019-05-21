package adrenaline;

import adrenaline.gameboard.*;

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

    final private int numMaxWeaponSpawnpoin=3;
    private LinkedList<Player> players;
    protected boolean firstTurn;
    protected int currentPlayer;

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

    protected WeaponDeck weaponDeck;
    protected PowerUpDeck powerUpDeck;
    protected AmmoTileDeck ammoTileDeck;

    /**
     * Constructor with Mode, Bot and choice of map.
     *
     * @param m the mode
     * @param b the bot choice
     * @param chosenMap the map chosen
     */
    public GameModel(Mode m, Bot b,
                     int chosenMap) {

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

        firstTurn=false;
        currentPlayer=0;

        weaponDeck = new WeaponDeck();
        weaponDeck.shuffleCards();
        powerUpDeck = new PowerUpDeck();
        powerUpDeck.shuffleCards();
        ammoTileDeck = new AmmoTileDeck();
        ammoTileDeck.shuffleCards();
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

    public void populateMap(Map mapUsed){
        int indexOfRoom;
        int xCoordinate;
        int yCoordinate;
        Coordinates coordinates;
        for(indexOfRoom=0;indexOfRoom<mapUsed.getGameBoard().getNumberOfRooms();indexOfRoom++){
            //here check all the rooms one by one
            for(xCoordinate=0;xCoordinate<mapUsed.getArrayX()[indexOfRoom];xCoordinate++)
            {
                for(yCoordinate=0;yCoordinate<mapUsed.getArrayY()[indexOfRoom];yCoordinate++){
                   if(xCoordinate<mapUsed.getGameBoard().getRoom(indexOfRoom).getRoomSizeX()&&yCoordinate<mapUsed.getGameBoard().getRoom(indexOfRoom).getRoomSizeY()){
                       //now i check inside the room
                       for (Spawnpoint spawnpoint:mapUsed.getGameBoard().getRoom(indexOfRoom).getSpawnpoints()) {
                           if(spawnpoint.getSpawnpointY()==yCoordinate&&spawnpoint.getSpawnpointX()==xCoordinate&&spawnpoint.getWeaponCards().size()<numMaxWeaponSpawnpoin)
                               spawnpoint.addWeaponCard(weaponDeck.pickUpWeapon());

                       }
                       for (AmmoTile ammoTile:mapUsed.getGameBoard().getRoom(indexOfRoom).getTiles()) {
                           if(ammoTile.getCoordinates()==null)
                               mapUsed.getGameBoard().getRoom(indexOfRoom).addAmmoTile(ammoTileDeck.pickUpAmmoTile());
                       }
                   }

                }
            }

        }
    }

}





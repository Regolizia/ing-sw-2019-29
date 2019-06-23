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
    private boolean doesntExists;
    public WeaponDeck weaponDeck;
    public PowerUpDeck powerUpDeck;
    public AmmoTileDeck ammoTileDeck;

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


    public void startingMap(){
        for(Room room:getMapUsed().getGameBoard().getRooms()){

            for (AmmoTile a:room.getTiles()
                 ) {

                for (AmmoCube ac: a.getAmmoCubes()
                     ) {
                    ac.setCubeColor(AmmoCube.CubeColor.FREE);
                }
            }

            for (Spawnpoint spw:room.getSpawnpoints()
                 ) {
                spw.getWeaponCards().clear();
            }

        }
    }


//conv if an AmmoTile doesnt't exists all its colors are none
    public void populateMap() {
        populateAmmoTile();
        for (Room room : getMapUsed().getGameBoard().getRooms()) {
            //here check all the rooms one by one

            for (Spawnpoint s : room.getSpawnpoints()
            ) {
                while (s.getWeaponCards().size() < numMaxWeaponSpawnpoin) {
                    s.getWeaponCards().add(weaponDeck.pickUpWeapon());
                }
            }
        }

    }

    public void populateAmmoTile(){

     for (Room room : getMapUsed().getGameBoard().getRooms()) {
         for (int x = 1; x <= room.getRoomSizeX(); x++) {
             for (int y = 1; y <= room.getRoomSizeY(); y++) {
                 room.getTiles().add(ammoTileDeck.pickUpAmmoTile());
                 room.getTiles().getLast().setCoordinates(x, y);
             }
         }
     }

    }

}











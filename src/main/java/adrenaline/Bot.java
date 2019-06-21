package adrenaline;


import java.util.LinkedList;

public class Bot extends Player {

/**
 * this is bot's class
 *
 * @author Giulia Valcamonica
 * @version 1.0
 * */

/*bot doesn't have
*   ammoCube or actionTile
* bot has
*   playerBoard, a Spawnpoint  and a colour
*
* bot starts on second player turn
*
* if player has bot 2 action + 1 bot action
*
* if both adrenaline shoot bot (only 1 damage) , bot gives a mark
*
* Mirino doesn't work
*
* Teleport doesn't work*/

    private Player owner;

    public Bot(Figure.PlayerColor colorBot){
        super("BOT",colorBot);
    }


    /**
     * setOwner
     * this methods give bot to current player
     * @param player: player in turn
     * */
   public void setOwner(Player player){
        this.owner=player;
   }
    /**
     * getOwner
     * @return player who has bot
     * */

   public Player getOwner(){
        return owner;
   }




}

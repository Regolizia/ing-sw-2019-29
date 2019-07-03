package adrenaline;


import java.util.*;

/**
 * 
 */// only with frenzy mode and at the endOfTheGame
    //Players without damage turn over their board
    //every player gets a freneticTorn (2xFreneticAction)
    // look for the one with the first card
    // if a player plays before firstPlayer chooses an action
    // else chooses another action

/**
 * Is the class that describes players' frenzy actions
 * @author Giulia Valcamonica
 * @version 2.0
 **/

public class FreneticAction extends Action {

    public FreneticAction(GameModel m){
        super(m);
    }

    public static enum PlayerOrder {
        FIRST,AFTER      //reload is an  optional action //ADRENALINESHOOT
    }


/*FIRST
* move up to 4 squares
* move uo to 2 squares and grab
* move up to 1 squares reload and shoot*/
        /*AFTER
         * move up to 3 squares and grab
         * move up to 2 squares reload and shoot*/

/**
 * proposeCellsRunFrenzy
 * this is proposeCellsRun frenzy variant
 * @param c :player's coordinates
 *          CONV: this action can be used by the players which order is before first player or by first player
 */
    public LinkedList<CoordinatesWithRoom> proposeCellsRunFrenzy(CoordinatesWithRoom c) {

        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 1));
        list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 2));
        list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 3));
        list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),4));
        return list;
    }

    /**
     * proposeCellsGrabFrenzy
     * this is proposeCellsGrab frenzy variant
     * @param c :player's coordinates
     * @param order :player's order, depends on first player
     *          CONV: this action can be used by all the players but change its effect, based on player's order
     */

    public LinkedList<CoordinatesWithRoom> proposeCellsGrabFrenzy(CoordinatesWithRoom c,PlayerOrder order){
            LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 1));
            list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),2));
            list.add(c);
            if(order.equals(PlayerOrder.AFTER))
                list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),3));
            return list;

    }

    /**
     * proposeCellsRunBeforeShootFrenzy
     * this is proposeCellsRunBeforeShoot frenzy variant
     * @param c :player's coordinates
     * @param order :player's order, depends on first player
     *          CONV: this action can be used by all the players but change its effect, based on player's order
     */
    public LinkedList<CoordinatesWithRoom>proposeCellsRunBeforeShootFrenzy(CoordinatesWithRoom c,PlayerOrder order){
        LinkedList<CoordinatesWithRoom>list=new LinkedList<>(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),1));
        list.add(c);
        if(order.equals(PlayerOrder.AFTER))
            list.addAll(c.xTilesDistant(getModel().getMapUsed().getGameBoard(),2));
        return list;
    }

    //______________________________________freneticPoints____________________________________________________________//

    /**
     * canGetPoints()
     * @param allPlayers possible shooters
     * @param victims dead players
     *
     */


    @Override
    public void canGetPoints(List<Player> victims, List<Player> allPlayers) {
        List<Player>playersWhoHaveShoot=new LinkedList<>();


        for (Player victim: victims
        ) {
            for (Figure.PlayerColor color : victim.getTrack()
            ) {
                for (Player player : allPlayers
                ) {
                    if (player.getColor().equals(color))
                        playersWhoHaveShoot.add(player);

                }

            }
            if (playersWhoHaveShoot.size() == 0)
                break;

            frenzyGivePoints(playersWhoHaveShoot,victim);
        }
    }
    /**
     *frenzyGivePoints()
     * @param victim dead player
     * @param playersWhoHaveShoot victim's shooters
     */
    public void frenzyGivePoints(List<Player> playersWhoHaveShoot, Player victim) {
        whoHasDoneMoreDamage(playersWhoHaveShoot,victim).setPoints(victim.getPointTrackFren().length-1);
        playersWhoHaveShoot.remove(whoHasDoneMoreDamage(playersWhoHaveShoot,victim));
        for (Player shooter:playersWhoHaveShoot
             ) {
            shooter.setPoints(1);

        }
    }
    /**
     *whoHasDoneMoreDamage()
     * @param playersWhoHaveShoot victim's shooters
     * @param victim
     */
    public Player whoHasDoneMoreDamage(List<Player> playersWhoHaveShoot, Player victim) {
        int max=0;
        Player pMax=new Player();
        if(max==0) {max=victim.damageByShooter(playersWhoHaveShoot.get(0));
        pMax=playersWhoHaveShoot.get(0);
        playersWhoHaveShoot.remove(0);
        }
        for (Player player:playersWhoHaveShoot
             ) {
            if(victim.damageByShooter(player)>max)
            {
                max=victim.damageByShooter(player);
                pMax=player;
                playersWhoHaveShoot.remove(player);
            }
            else if(victim.damageByShooter(player)==max){
                pMax=chooseOne(player,pMax,victim);
                playersWhoHaveShoot.remove(player);
            }
        }
        return pMax;
    }
    /**
     *chooseOne()
     * @param victim
     * @param player
     * @param pMax
     * choose between shooters with same points, pick up the one who has damage first the victim
     */
    public Player chooseOne(Player player,Player pMax,Player victim){
        if(victim.getFirstPositionOnTrack(player)<victim.getFirstPositionOnTrack(pMax))
            return player;
        return pMax;
    }

}
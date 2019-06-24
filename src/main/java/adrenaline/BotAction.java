package adrenaline;

import adrenaline.gameboard.GameBoard;

import java.util.*;

/**
 * 
 */
public class BotAction extends Action {


    public BotAction(GameModel m)
            {
            super (m);
            }



     //---------------------------------TO RUN BOT-------------------------------------------------------------------//
@Override
    public LinkedList<CoordinatesWithRoom> proposeCellsRun(CoordinatesWithRoom c) {
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>(c.xTilesDistant(getModel().getMapUsed().getGameBoard(), 1));
        return list;
    }

     //-------------------------------TO SHOOT WHIT BOT---------------------------------------------------------------//

    /*bot can shoot only if it has valid opponents*/
     /*valid opponents: every player minus bot owner and bot (if dom can shoot to powerpoint)*/
        //TODO CHECK POSSIBLE TARGET


    public void botShoot(Object target,Bot bot){

            if(target instanceof Player&&((Player) target).getTrack()[11].equals(Figure.PlayerColor.NONE)){
                ((Player) target).addDamageToTrack(bot,1);
                if(((Player) target).isDead()&&((Player) target).getTrack()[11].equals(bot.getColor()))
                {
                    bot.addMarks((Player) target,1);
                }
                if(target instanceof Player&&bot.checkDamage()==2&&((Player) target).canAddMark(bot))
                {
                    ((Player) target).addMarks(bot,1);
                }
            }


    }

    }

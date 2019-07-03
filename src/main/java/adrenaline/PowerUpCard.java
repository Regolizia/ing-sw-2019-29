package adrenaline;

import java.util.LinkedList;
import java.util.List;

public class PowerUpCard extends Card {

    private AmmoCube.CubeColor color;
    private boolean canBeUsedOnBot;
    public PowerUpCard(){

    }

    public PowerUpCard(AmmoCube.CubeColor c){

        this.color = c;
    }


    public AmmoCube.CubeColor getPowerUpColor(){
                return this.color;
    }
    public void setPowerUpColor(AmmoCube.CubeColor c){
        this.color = c;
    }
    /**
     *getCanBeUsedOnBot
     * @return if a PowerUp can be used on bot
     **/
    public boolean getCanBeUsedOnBot() {
        return canBeUsedOnBot;
    }
    /**
     *setCanBeUsedOnBot
     * @param canBeUsedOnBot
     **/
    public void setCanBeUsedOnBot(boolean canBeUsedOnBot) {
        this.canBeUsedOnBot = canBeUsedOnBot;
    }

    /**
     *plusOneDamage()
     * @param player
     * @param target
     * add one damage to target
     */
    public void plusOneDamage(Player player,Object target){
        if(target instanceof Player)
            ((Player) target).damageByShooter(player);

    }
    /**
     *getPossibleCells()
     * @param p
     * @param m
     */
    public List<CoordinatesWithRoom> getPossibleCells(GameModel m, Player p){

        List<CoordinatesWithRoom>cWr=new LinkedList<CoordinatesWithRoom>();
        for (Room r:m.getMapUsed().getGameBoard().getRooms()
        ) {
            for (int x=1;x<=r.getRoomSizeX();x++) {

                for (int y=1;y<=r.getRoomSizeY();y++)
                {
                    cWr.add(new CoordinatesWithRoom(x,y,r));
                }

            }

        }

        return cWr;
    }

    //useless
    //public void setColor(PowerUpCard card, Color c){
      //  this.color = c ;}


}

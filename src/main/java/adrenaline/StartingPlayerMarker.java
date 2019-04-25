package adrenaline;

import java.util.*;

/**
 * 
 */
public class StartingPlayerMarker{

    private Player owner;
    /**
     * Default constructor
     */
    public StartingPlayerMarker() {
    }
    public StartingPlayerMarker(Player p) {
        this.owner = p;
    }

    public Player getOwner(){
        return owner;
    }
    // SAME AS CONSTRUCTOR
    public void setOwner(Player p){
        this.owner = p;
    }


}
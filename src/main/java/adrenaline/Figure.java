package adrenaline;

import java.util.*;

/**
 * 
 */
public class Figure {

    public enum PlayerColor {
        PURPLE, YELLOW, BLUE, GREEN, GRAY,NONE;     //NONE on the track when nobody has attacked the player
    }
    private PlayerColor color;



    /**
     * Default constructor
     */
    public Figure(PlayerColor c) {
        this.color = c;
    }

    /**
     * 
     */
    //public player choose;


    public PlayerColor getPlayerColor(){
        return color;
    }
    public void setPlayerColor(PlayerColor c){
        this.color = c;
    }


}
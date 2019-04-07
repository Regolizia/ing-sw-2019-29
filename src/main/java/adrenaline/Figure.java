package adrenaline;

import java.util.*;

/**
 * 
 */
public class Figure {

    public static enum PlayerColor {
        PURPLE, YELLOW, BLUE, GREEN, GRAY;
    }
    private PlayerColor color;



    /**
     * Default constructor
     */
    public Figure() {
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
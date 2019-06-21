package adrenaline.gameboard;

import adrenaline.AmmoCube;
import adrenaline.GameModel;

/**
 * Is the class that represents the Map of the Board.
 * It contains:
 * <ul>
 *     <li> The Game Board
 *     <li> Two arrays with the measures of the Rooms
 *     <li> Three arrays with the location of the Spawnpoints
 *     <li> Six arrays with the location of the Doors
 *     <li> Six arrays with the location of the Walls
 * </ul>
 *
 * @author Eleonora Toscano
 * @version 1.0
 */
public class Map {

    private GameBoard gameboard;

    int[] arrayX;
    int[] arrayY;
    int[] arraySpawnR;
    int[] arraySpawnX;
    int[] arraySpawnY;
    int[] doorR1;
    int[] doorX1;
    int[] doorY1;
    int[] doorR2;
    int[] doorX2;
    int[] doorY2;
    Door.Direction[] doorDir;

    int[] wallR1;
    int[] wallX1;
    int[] wallY1;
    int[] wallR2;
    int[] wallX2;
    int[] wallY2;
    Door.Direction[] wallDir;

    /**
     * Default constructor.
     */
    public Map() {

    }

    /**
     * Constructor with Game Mode.
     * Overridden.
     *
     * @param m the Game Mode
     */
    public Map(GameModel.Mode m) { }

    public GameBoard getGameBoard(){
        return gameboard;
    }
    public int[] getArrayX(){
        return  this.arrayX;
    }
    public int[] getArrayY(){
        return  this.arrayY;
    }

    void setGameBoard(GameBoard g){
        this.gameboard = g;
    }

    //0 -> BLUE
    //1 -> RED
    //2 -> YELLOW
    public AmmoCube.CubeColor fromIndexToColor(int i){
        if(i==0) return AmmoCube.CubeColor.BLUE;
        if(i==1) return AmmoCube.CubeColor.RED;
        if(i==2) return AmmoCube.CubeColor.YELLOW;
        return AmmoCube.CubeColor.BLUE;
    }

}

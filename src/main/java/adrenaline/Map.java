package adrenaline;

public class Map {

    private GameBoard gameboard;

    protected int[] arrayX;
    protected int[] arrayY;
    protected int[] arraySpawnR;
    protected int[] arraySpawnX;
    protected int[] arraySpawnY;
    protected int[] doorR1;
    protected int[] doorX1;
    protected int[] doorY1;
    protected int[] doorR2;
    protected int[] doorX2;
    protected int[] doorY2;
    protected Door.Direction[] doorDir;

    protected int[] wallR1;
    protected int[] wallX1;
    protected int[] wallY1;
    protected int[] wallR2;
    protected int[] wallX2;
    protected int[] wallY2;
    protected Door.Direction[] wallDir;

    public Map() {

    }

    public Map(GameModel.Mode m) {

    }

    public GameBoard getGameBoard(){
        return gameboard;
    }
    public void setGameBoard(GameBoard g){
        this.gameboard = g;
    }



}

package adrenaline;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class Map {

    private GameBoard gameboard;


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

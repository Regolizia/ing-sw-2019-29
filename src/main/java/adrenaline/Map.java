package adrenaline;

import static adrenaline.GameModel.Mode.DEATHMATCH;

public class Map {

    public GameBoard gameboard;


    public Map() {

    }

    public Map(GameModel.Mode m) {
        if (m.equals(DEATHMATCH)) {

            //DEATHMATCH MAP

        }
        else{

            //DOMINATION MAP

        };

    }

}

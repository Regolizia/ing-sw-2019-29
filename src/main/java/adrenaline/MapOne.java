package adrenaline;

import static adrenaline.GameModel.Mode.DEATHMATCH;

// MapOne is the first little map in the rules
public class MapOne extends Map {



    /**
     * Default constructor
     */
    public MapOne() {

    }

    public MapOne(GameModel.Mode m) {
        if (m.equals(DEATHMATCH)) {
        gameboard = new DeathmatchBoard();


        ((DeathmatchBoard) gameboard).addRoom(new Room(3,1));//BLUE
        ((DeathmatchBoard) gameboard).addRoom(new Room(3,1));//RED
        ((DeathmatchBoard) gameboard).addRoom(new Room(2,1));//WHITE
        ((DeathmatchBoard) gameboard).addRoom(new Room(1,2));//YELLOW

       // ((DeathmatchBoard) gameboard).addDoor(new Door(gameboard.getRoom(0), 1, 1, rooms.get(1), 1, 1));

        }
        else{

            //DOMINATION MAP

        };

    }


}

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


        ((DeathmatchBoard) gameboard).addRoom(new Room(3,1));//BLUE (getRoom(0))
        ((DeathmatchBoard) gameboard).addRoom(new Room(3,1));//RED  (getRoom(1))
        ((DeathmatchBoard) gameboard).addRoom(new Room(2,1));//WHITE  (getRoom(2))
        ((DeathmatchBoard) gameboard).addRoom(new Room(1,2));//YELLOW  (getRoom(3))

        ((DeathmatchBoard) gameboard).addDoor(new Door(gameboard.getRoom(0), 1, 1,gameboard.getRoom(1), 1, 1));
        ((DeathmatchBoard) gameboard).addDoor(new Door(gameboard.getRoom(0), 3, 1,gameboard.getRoom(1), 3, 1));
        ((DeathmatchBoard) gameboard).addDoor(new Door(gameboard.getRoom(2), 1, 1,gameboard.getRoom(1), 2, 1));
        ((DeathmatchBoard) gameboard).addDoor(new Door(gameboard.getRoom(1), 3, 1,gameboard.getRoom(3), 1, 1));
        ((DeathmatchBoard) gameboard).addDoor(new Door(gameboard.getRoom(2), 2, 1,gameboard.getRoom(3), 1, 2));

        }
        else{

            //DOMINATION MAP

        };

    }


}

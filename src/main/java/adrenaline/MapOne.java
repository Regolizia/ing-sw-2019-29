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

        (gameboard).addRoom(new RoomDeath(3,1));//BLUE (getRoom(0))
        (gameboard).addRoom(new RoomDeath(3,1));//RED  (getRoom(1))
        (gameboard).addRoom(new RoomDeath(2,1));//WHITE  (getRoom(2))
        (gameboard).addRoom(new RoomDeath(1,2));//YELLOW  (getRoom(3))
                }
        else{
        gameboard = new DominationBoard();

        (gameboard).addRoom(new RoomDom(3,1));//BLUE (getRoom(0))
        (gameboard).addRoom(new RoomDom(3,1));//RED  (getRoom(1))
        (gameboard).addRoom(new RoomDom(2,1));//WHITE  (getRoom(2))
        (gameboard).addRoom(new RoomDom(1,2));//YELLOW  (getRoom(3))
        };

        (gameboard).addDoor(new Door(gameboard.getRoom(0), 1, 1,gameboard.getRoom(1), 1, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(0), 3, 1,gameboard.getRoom(1), 3, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(2), 1, 1,gameboard.getRoom(1), 2, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(1), 3, 1,gameboard.getRoom(3), 1, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(2), 2, 1,gameboard.getRoom(3), 1, 2));

            (gameboard).getRoom(0).addSpawnpoint(new Spawnpoint(3, 1));
            (gameboard).getRoom(1).addSpawnpoint(new Spawnpoint(1, 1));
            (gameboard).getRoom(3).addSpawnpoint(new Spawnpoint(1, 2));


    }


}

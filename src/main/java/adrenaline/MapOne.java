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
            setGameBoard(new DeathmatchBoard());

            getGameBoard().addRoom(new RoomDeath(3,1));//BLUE (getRoom(0))
            getGameBoard().addRoom(new RoomDeath(3,1));//RED  (getRoom(1))
            getGameBoard().addRoom(new RoomDeath(2,1));//WHITE  (getRoom(2))
            getGameBoard().addRoom(new RoomDeath(1,2));//YELLOW  (getRoom(3))
        }
        else{

            setGameBoard(new DominationBoard());

            getGameBoard().addRoom(new RoomDom(3,1));//BLUE (getRoom(0))
            getGameBoard().addRoom(new RoomDom(3,1));//RED  (getRoom(1))
            getGameBoard().addRoom(new RoomDom(2,1));//WHITE  (getRoom(2))
            getGameBoard().addRoom(new RoomDom(1,2));//YELLOW  (getRoom(3))
        };

        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 1, 1,getGameBoard().getRoom(1), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 3, 1,getGameBoard().getRoom(1), 3, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(2), 1, 1,getGameBoard().getRoom(1), 2, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(1), 3, 1,getGameBoard().getRoom(3), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(2), 2, 1,getGameBoard().getRoom(3), 1, 2));

        getGameBoard().getRoom(0).addSpawnpoint(new Spawnpoint(3, 1)); //BLUE
        getGameBoard().getRoom(1).addSpawnpoint(new Spawnpoint(1, 1)); //RED
        getGameBoard().getRoom(3).addSpawnpoint(new Spawnpoint(1, 2)); //YELLOW


    }


}

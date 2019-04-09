package adrenaline;


import static adrenaline.GameModel.Mode.DEATHMATCH;

// MapTwo is the big map in the rules near the little maps
public class MapTwo extends Map{

    /**
     * Default constructor

    public MapTwo() {

    }
     */
    public MapTwo(GameModel.Mode m) {

        if (m.equals(DEATHMATCH)) {
            setGameBoard(new DeathmatchBoard());

            getGameBoard().addRoom(new RoomDeath(2,1));//BLUE (getRoom(0))
            getGameBoard().addRoom(new RoomDeath(1,2));//RED  (getRoom(1))
            getGameBoard().addRoom(new RoomDeath(2,1));//PURPLE  (getRoom(2))
            getGameBoard().addRoom(new RoomDeath(3,1));//WHITE  (getRoom(3))
            getGameBoard().addRoom(new RoomDeath(1,2));//YELLOW  (getRoom(4))

            getGameBoard().getRoom(0).addSpawnpoint(new Spawnpoint(2, 1)); //BLUE
            getGameBoard().getRoom(1).addSpawnpoint(new Spawnpoint(1, 2)); //RED
            getGameBoard().getRoom(4).addSpawnpoint(new Spawnpoint(1, 2)); //YELLOW
        }
        else{

            setGameBoard(new DominationBoard());

            getGameBoard().addRoom(new RoomDom(2,1));//BLUE (getRoom(0))
            getGameBoard().addRoom(new RoomDom(1,2));//RED  (getRoom(1))
            getGameBoard().addRoom(new RoomDom(2,1));//PURPLE  (getRoom(2))
            getGameBoard().addRoom(new RoomDom(3,1));//WHITE  (getRoom(3))
            getGameBoard().addRoom(new RoomDom(1,2));//YELLOW  (getRoom(4))

            getGameBoard().getRoom(0).addSpawnpoint(new SpawnpointDom(2, 1)); //BLUE
            getGameBoard().getRoom(1).addSpawnpoint(new SpawnpointDom(1, 2)); //RED
            getGameBoard().getRoom(4).addSpawnpoint(new SpawnpointDom(1, 2)); //YELLOW
        };

        getGameBoard().addDoor(new Door(getGameBoard().getRoom(1), 1, 1,getGameBoard().getRoom(0), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(1), 1, 2,getGameBoard().getRoom(3), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 1, 1,getGameBoard().getRoom(2), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 2, 1,getGameBoard().getRoom(2), 2, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(2), 1, 1,getGameBoard().getRoom(3), 2, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(2), 2, 1,getGameBoard().getRoom(4), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(4), 1, 2,getGameBoard().getRoom(3), 3, 1));



    }


}

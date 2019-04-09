package adrenaline;

import static adrenaline.GameModel.Mode.DEATHMATCH;

// MapThree is the second little map in the rules
public class MapThree extends Map{

    /**
     * Default constructor

    public MapThree() {

    }
     */
    public MapThree(GameModel.Mode m) {

        if (m.equals(DEATHMATCH)) {
            setGameBoard(new DeathmatchBoard());

            getGameBoard().addRoom(new RoomDeath(3,1));//BLUE (getRoom(0))
            getGameBoard().addRoom(new RoomDeath(2,1));//RED  (getRoom(1))
            getGameBoard().addRoom(new RoomDeath(1,1));//WHITE  (getRoom(2))
            getGameBoard().addRoom(new RoomDeath(2,2));//YELLOW  (getRoom(3))
            getGameBoard().addRoom(new RoomDeath(1,1));//GREEN  (getRoom(4))

            getGameBoard().getRoom(0).addSpawnpoint(new Spawnpoint(3, 1)); //BLUE
            getGameBoard().getRoom(1).addSpawnpoint(new Spawnpoint(1, 1)); //RED
            getGameBoard().getRoom(3).addSpawnpoint(new Spawnpoint(2, 2)); //YELLOW
        }
        else{

            setGameBoard(new DominationBoard());

            getGameBoard().addRoom(new RoomDom(3,1));//BLUE (getRoom(0))
            getGameBoard().addRoom(new RoomDom(2,1));//RED  (getRoom(1))
            getGameBoard().addRoom(new RoomDom(1,1));//WHITE  (getRoom(2))
            getGameBoard().addRoom(new RoomDom(2,2));//YELLOW  (getRoom(3))
            getGameBoard().addRoom(new RoomDom(1,1));//GREEN  (getRoom(4))

            getGameBoard().getRoom(0).addSpawnpoint(new SpawnpointDom(3, 1)); //BLUE
            getGameBoard().getRoom(1).addSpawnpoint(new SpawnpointDom(1, 1)); //RED
            getGameBoard().getRoom(3).addSpawnpoint(new SpawnpointDom(2, 2)); //YELLOW
        };

        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 1, 1,getGameBoard().getRoom(1), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 3, 1,getGameBoard().getRoom(4), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 3, 1,getGameBoard().getRoom(3), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(1), 2, 1,getGameBoard().getRoom(2), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(2), 1, 1,getGameBoard().getRoom(3), 1, 2));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(4), 1, 1,getGameBoard().getRoom(3), 2, 1));



    }


}

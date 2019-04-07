package adrenaline;

import static adrenaline.GameModel.Mode.DEATHMATCH;

// MapFour is the third little map in the rules
public class MapFour extends Map {



    /**
     * Default constructor
     */
    public MapFour() {

    }

    public MapFour(GameModel.Mode m) {

        if (m.equals(DEATHMATCH)) {
            setGameBoard(new DeathmatchBoard());

            getGameBoard().addRoom(new RoomDeath(2,1));//BLUE (getRoom(0))
            getGameBoard().addRoom(new RoomDeath(1,2));//RED  (getRoom(1))
            getGameBoard().addRoom(new RoomDeath(2,1));//WHITE  (getRoom(2))
            getGameBoard().addRoom(new RoomDeath(2,2));//YELLOW  (getRoom(3))
            getGameBoard().addRoom(new RoomDeath(1,1));//PURPLE  (getRoom(4))
            getGameBoard().addRoom(new RoomDeath(1,1));//GREEN  (getRoom(5))
        }
        else{

            setGameBoard(new DominationBoard());

            getGameBoard().addRoom(new RoomDom(2,1));//BLUE (getRoom(0))
            getGameBoard().addRoom(new RoomDom(1,2));//RED  (getRoom(1))
            getGameBoard().addRoom(new RoomDom(2,1));//WHITE  (getRoom(2))
            getGameBoard().addRoom(new RoomDom(2,2));//YELLOW  (getRoom(3))
            getGameBoard().addRoom(new RoomDom(1,1));//PURPLE  (getRoom(4))
            getGameBoard().addRoom(new RoomDom(1,1));//GREEN  (getRoom(5))
        };

        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 1, 1,getGameBoard().getRoom(1), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 1, 1,getGameBoard().getRoom(4), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 2, 1,getGameBoard().getRoom(3), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(0), 2, 1,getGameBoard().getRoom(5), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(1), 1, 2,getGameBoard().getRoom(2), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(2), 2, 1,getGameBoard().getRoom(4), 1, 1));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(2), 2, 1,getGameBoard().getRoom(3), 1, 2));
        getGameBoard().addDoor(new Door(getGameBoard().getRoom(5), 1, 1,getGameBoard().getRoom(3), 2, 1));

        getGameBoard().getRoom(0).addSpawnpoint(new Spawnpoint(2, 1)); //BLUE
        getGameBoard().getRoom(1).addSpawnpoint(new Spawnpoint(1, 2)); //RED
        getGameBoard().getRoom(3).addSpawnpoint(new Spawnpoint(2, 2)); //YELLOW


    }


}

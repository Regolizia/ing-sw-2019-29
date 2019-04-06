package adrenaline;


import static adrenaline.GameModel.Mode.DEATHMATCH;

// MapTwo is the big map in the rules near the little maps
public class MapTwo extends Map{

    /**
     * Default constructor
     */
    public MapTwo() {

    }

    public MapTwo(GameModel.Mode m) {

        if (m.equals(DEATHMATCH)) {
            gameboard = new DeathmatchBoard();

            (gameboard).addRoom(new RoomDeath(2,1));//BLUE (getRoom(0))
            (gameboard).addRoom(new RoomDeath(1,2));//RED  (getRoom(1))
            (gameboard).addRoom(new RoomDeath(2,1));//PURPLE  (getRoom(2))
            (gameboard).addRoom(new RoomDeath(3,1));//WHITE  (getRoom(3))
            (gameboard).addRoom(new RoomDeath(1,2));//YELLOW  (getRoom(4))
        }
        else{
            gameboard = new DominationBoard();

            (gameboard).addRoom(new RoomDom(2,1));//BLUE (getRoom(0))
            (gameboard).addRoom(new RoomDom(1,2));//RED  (getRoom(1))
            (gameboard).addRoom(new RoomDom(2,1));//PURPLE  (getRoom(2))
            (gameboard).addRoom(new RoomDom(3,1));//WHITE  (getRoom(3))
            (gameboard).addRoom(new RoomDom(1,2));//YELLOW  (getRoom(4))
        };

        (gameboard).addDoor(new Door(gameboard.getRoom(1), 1, 1,gameboard.getRoom(0), 1, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(1), 1, 2,gameboard.getRoom(3), 1, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(0), 1, 1,gameboard.getRoom(2), 1, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(0), 2, 1,gameboard.getRoom(2), 2, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(2), 1, 1,gameboard.getRoom(3), 2, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(2), 2, 1,gameboard.getRoom(4), 1, 1));
        (gameboard).addDoor(new Door(gameboard.getRoom(4), 1, 2,gameboard.getRoom(3), 3, 1));

        (gameboard).getRoom(0).addSpawnpoint(new Spawnpoint(2, 1)); //BLUE
        (gameboard).getRoom(1).addSpawnpoint(new Spawnpoint(1, 2)); //RED
        (gameboard).getRoom(4).addSpawnpoint(new Spawnpoint(1, 2)); //YELLOW


    }


}

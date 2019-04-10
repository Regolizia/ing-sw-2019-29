package adrenaline;

import static adrenaline.GameModel.Mode.DEATHMATCH;

// MapThree is the second little map in the rules
public class MapThree extends Map {

    public MapThree(GameModel.Mode m) {

        arrayX = new int[]{3, 2, 1, 2, 1};
        arrayY = new int[]{1, 1, 1, 2, 1};
        arraySpawnR = new int[]{0, 1, 3};
        arraySpawnX = new int[]{3, 1, 2};
        arraySpawnY = new int[]{1, 1, 2};
        doorR1 = new int[]{0, 0, 0, 1, 2, 4};
        doorX1 = new int[]{1, 3, 3, 2, 1, 1};
        doorY1 = new int[]{1, 1, 1, 1, 1, 1};
        doorR2 = new int[]{1, 4, 3, 2, 3, 3};
        doorX2 = new int[]{1, 1, 1, 1, 1, 2};
        doorY2 = new int[]{1, 1, 1, 1, 2, 1};

        if (m.equals(DEATHMATCH)) {
            setGameBoard(new DeathmatchBoard());

            for (int i = 0; i < arrayX.length; i++) {

                //BLUE (getRoom(0))
                //RED  (getRoom(1))
                //WHITE  (getRoom(2))
                //YELLOW  (getRoom(3))
                //GREEN  (getRoom(4))
                getGameBoard().addRoom(new RoomDeath(arrayX[i], arrayY[i]));
            }
            for (int j = 0; j < arraySpawnR.length; j++) {
                //BLUE
                //RED
                //YELLOW
                getGameBoard().getRoom(arraySpawnR[j]).addSpawnpoint(new Spawnpoint(arraySpawnX[j], arraySpawnY[j]));
            }
        } else {

            setGameBoard(new DominationBoard());

            for (int i = 0; i < arrayX.length; i++) {

                //BLUE (getRoom(0))
                //RED  (getRoom(1))
                //WHITE  (getRoom(2))
                //YELLOW  (getRoom(3))
                //GREEN  (getRoom(4))
                getGameBoard().addRoom(new RoomDom(arrayX[i], arrayY[i]));
            }
            for (int j = 0; j < arraySpawnR.length; j++) {
                //BLUE
                //RED
                //YELLOW
                getGameBoard().getRoom(arraySpawnR[j]).addSpawnpoint(new SpawnpointDom(arraySpawnX[j], arraySpawnY[j]));
            }

        }
        for (int k = 0; k < doorR1.length; k++) {

            getGameBoard().addDoor(new Door(getGameBoard().getRoom(doorR1[k]), doorX1[k], doorY1[k], getGameBoard().getRoom(doorR2[k]), doorX2[k], doorY2[k]));

        }
    }
}

package adrenaline.gameboard;

import adrenaline.*;

import static adrenaline.gameboard.Door.Direction.*;
import static adrenaline.GameModel.Mode.DEATHMATCH;

/**
 * MapFour is the third little map in the rules.
 * It extends Map.
 *
 * @author Eleonora Toscano
 * @version 1.0
 * @see Map
 */
public class MapFour extends Map {

    /**
     * Constructor with parameter Mode.
     * Depending on the Mode RoomDeaths or RoomDoms are added
     * to the Map.
     * Also Walls, Doors and Spawnpoints are created.
     *
     * @param m the Game Mode
     * @see adrenaline.GameModel.Mode
     * @see RoomDeath
     * @see RoomDom
     */
    public MapFour(GameModel.Mode m) {

        arrayX = new int[]{2,1,2,2,1,1};
        arrayY = new int[]{1,2,1,2,1,1};
        arraySpawnR = new int[]{0,1,3};
        arraySpawnX = new int[]{2,1,2};
        arraySpawnY = new int[]{1,2,2};
        doorR1 = new int[]{0,0,0,0,1,2,2,5};
        doorX1 = new int[]{1,1,2,2,1,2,2,1};
        doorY1 = new int[]{1,1,1,1,2,1,1,1};
        doorR2 = new int[]{1,4,3,5,2,4,3,3};
        doorX2 = new int[]{1,1,1,1,1,1,1,2};
        doorY2 = new int[]{1,1,1,1,1,1,2,1};
        doorDir = new Door.Direction[]{EW, NS, NS, WE, NS, SN, WE, NS};
        wallR1 = new int[]{1,3};
        wallX1 = new int[]{1,1};
        wallY1 = new int[]{2,1};
        wallR2 = new int[]{4,4};
        wallX2 = new int[]{1,1};
        wallY2 = new int[]{1,1};
        wallDir = new Door.Direction[]{WE, EW};


        if (m.equals(DEATHMATCH)) {
            setGameBoard(new DeathmatchBoard());

            for(int i=0;i<arrayX.length;i++) {

                // BLUE (getRoom(0))
                // RED  (getRoom(1))
                // WHITE  (getRoom(2))
                // YELLOW  (getRoom(3))
                // PURPLE  (getRoom(4))
                // GREEN  (getRoom(5))
                getGameBoard().addRoom(new RoomDeath(arrayX[i], arrayY[i]));
            }
            for(int j=0;j<arraySpawnR.length;j++){
                //BLUE
                //RED
                //YELLOW
                getGameBoard().getRoom(arraySpawnR[j]).getSpawnpoints().add(new Spawnpoint(arraySpawnX[j], arraySpawnY[j]));
                getGameBoard().getRoom(arraySpawnR[j]).getSpawnpoints().get(0).setColor(fromIndexToColor(j));            }
        }
        else{

            setGameBoard(new DominationBoard());


            for(int i=0;i<arrayX.length;i++) {

                //BLUE (getRoom(0))
                // RED  (getRoom(1))
                // WHITE  (getRoom(2))
                // YELLOW  (getRoom(3))
                // PURPLE  (getRoom(4))
                // GREEN  (getRoom(5))
                getGameBoard().addRoom(new RoomDom(arrayX[i], arrayY[i]));
            }
            for(int j=0;j<arraySpawnR.length;j++){
                //BLUE
                //RED
                //YELLOW
                getGameBoard().getRoom(arraySpawnR[j]).addSpawnpoint(new SpawnpointDom(arraySpawnX[j], arraySpawnY[j]));
            }

        }
        for(int k=0;k<doorR1.length;k++) {

            getGameBoard().addDoor(new Door(getGameBoard().getRoom(doorR1[k]), doorX1[k], doorY1[k], getGameBoard().getRoom(doorR2[k]), doorX2[k], doorY2[k],doorDir[k]));

        }

        for(int n=0;n<wallR1.length;n++) {

            getGameBoard().addWall(new Wall(getGameBoard().getRoom(wallR1[n]), wallX1[n], wallY1[n], getGameBoard().getRoom(wallR2[n]), wallX2[n], wallY2[n], wallDir[n]));

        }


    }
}




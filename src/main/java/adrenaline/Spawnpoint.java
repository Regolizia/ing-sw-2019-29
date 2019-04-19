package adrenaline;

public class Spawnpoint {

    int x;
    int y;


    public Spawnpoint(){};

    public Spawnpoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getSpawnpointX( ) {
        return x;
    }

    public int getSpawnpointY( ) {
        return y;
    }
}

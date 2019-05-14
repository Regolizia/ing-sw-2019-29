package adrenaline;

/**
 * Is the class that represents the coordinates in the game.
 * It contains two integers.
 *
 * @author Eleonora Toscano
 * @version 1.0
 */
public class Coordinates {

    private int x;
    private int y;

    /**
     * Default constructor.
     */
    Coordinates(){
        this.x =0;
        this.y =0;
    }

    /**
     * Constructor with two integers
     *
     * @param x first integer
     * @param y second integer
     */
    public Coordinates(int x,int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

     public void setCoordinates(int x, int y){
        this.x = x;
        this.y = y;
     }

}

package adrenaline;

/**
 * 
 */
public class VirtualClientCLI {

    public void printPlayerDetails(String playerName, int score, String color){
        System.out.println("Player: ");
        System.out.println("Name: " + playerName);
        System.out.println("Color: " + color);
        System.out.println("Score: " + score);
    }

    // UPDATES DIRECTLY FROM THE MODEL WHAT'S GOING ON IN THE GAME
    // PLAYERS, SCORE, LIFE...
   /* public void update() {
        // TODO implement here
    }
*/
}
package adrenaline;


import java.util.LinkedList;
/**
 * player's class
 * @author Giulia Valcamonica
 * @author Eleonora Toscano
 * @version 1.0
 **/
public class Player {
    private final static int numMaxCube = 3;
    private final static int trackSize = 12;
    private final static  int trackPointSize=5;
    private Figure.PlayerColor[] track;
    private int[] pointTrack;
    private Figure.PlayerColor[] marks;
    private int[] ammoBox; //BLUE RED YELLOW
    private CoordinatesWithRoom coordinates;
    private Figure.PlayerColor color;
    private CoordinatesWithRoom respawnCoordinates;
    private LinkedList<WeaponCard> hand;
    private LinkedList<PowerUpCard> powerups;
    private int points;
    private int indexPointCounter; //to indicate max points assegnable
    private boolean[] pointsArray;// HOW MANY TIMES PLAYER DIED
    private boolean[] skullTrack;
    private String name;

    /**
     * Class constructor.
     *  * @version 1.0
     */
    public Player() {

    }

    /**
     * Class constructor with name.
     *  @version 1.0
     * @param playerColor color selected by the player
     * @param rsp spawnpoint selected by the player
     */
    // Coordinates rsp is the spawnPosition chosen by the player
    public Player(String name, Figure.PlayerColor playerColor) {

        this.ammoBox = new int[]{1, 1, 1}; //BLUE RED YELLOW
        this.track = new Figure.PlayerColor[]{Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE};
        this.marks = new Figure.PlayerColor[]{Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE};
        // PUTTING "NONE" VALUE SO WE CAN USE SWITCH CASE
        this.color = playerColor;
        this.respawnCoordinates = new CoordinatesWithRoom();
        this.coordinates = new CoordinatesWithRoom();
        // they are lists because we need to add and remove easily
        this.hand = new LinkedList<WeaponCard>();
        this.powerups = new LinkedList<PowerUpCard>();
        this.pointsArray = new boolean[]{true, true, true, true, true, true};
        this.points = 0;
        this.pointTrack = new int[]{8,6,4,2,1};
        this.skullTrack = new boolean[]{false, false, false, false, false, false};
        this.indexPointCounter=0; //it means tha i can give 8 points
        points=0;
        this.name=name;
    }

    /**
     * Class constructor.
     *  @version 1.0
     * @param playerColor color selected by the player
     * @param rsp spawnpoint selected by the player
     */
    // Coordinates rsp is the spawnPosition chosen by the player
    public Player(CoordinatesWithRoom rsp, Figure.PlayerColor playerColor) {

        this.ammoBox = new int[]{1, 1, 1}; //BLUE RED YELLOW
        this.track = new Figure.PlayerColor[]{Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE};
        this.marks = new Figure.PlayerColor[]{Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE};
        // PUTTING "NONE" VALUE SO WE CAN USE SWITCH CASE
        this.color = playerColor;
        this.respawnCoordinates = rsp;
        this.coordinates = rsp;
        // they are lists because we need to add and remove easily
        this.hand = new LinkedList<WeaponCard>();
        this.powerups = new LinkedList<PowerUpCard>();
         this.pointsArray = new boolean[]{true, true, true, true, true, true};
        this.points = 0;
        this.pointTrack = new int[]{8,6,4,2,1};
        this.skullTrack = new boolean[]{false, false, false, false, false, false};
        this.indexPointCounter=0; //it means tha i can give 8 points
        points=0;
        name="";
    }
    /**
     * toString
     * message to video
     */
    @Override
    public String toString() {
        return "My name is x and my color is " + this.color;
    }
    /**
     * getColor
     * returns player's color
     */
    public Figure.PlayerColor getColor() {
        return color;
    }
    /**
     * getTrack
     * returns player's track
     */
    public Figure.PlayerColor[] getTrack() {
        return track;
    }
    /**
     * getMarks
     * returns player's mark
     */
    public Figure.PlayerColor[] getMarks() {
        return marks;
    }

    /**
     * checkDamage
     * to check if a player can select adrenaline methods to do an action
     */
    public int checkDamage() {

        if (track[2] != Figure.PlayerColor.NONE && track[5] == Figure.PlayerColor.NONE)
            return 1;       //better Grab
        if (track[5] != Figure.PlayerColor.NONE)
            return 2;       //better Shoot (and Grab)
        return 0;
    }



    // public void setToken(Player player){}

    // public String getToken(Player player){return "";} //adrenaline.TokenCLI


    //________________________to control player's position____________________________________________________//
    /**
     * setPlayerPosition
     * @param x : x coordinate desired
     * @param y : y coordinate desired
     *          set player's position coordinates x,y but remaining in the same room
     */

    public void setPlayerPosition(int x, int y) {
        this.coordinates.setCoordinates(x, y);
    }
    /**
     * setPlayerPosition
     * @param x : x coordinate desired
     * @param y : y coordinate desired
     * @param r: room desired
     *         set player's position chancing room
     */
    public void setPlayerPosition(int x, int y, Room r) {
        this.coordinates.setCoordinates(x, y);
        this.coordinates.setRoom(r);
    }
    /**
     * setPlayerPosition
     * @param c: spawnpoint coordinates
     *         set player's position at the initial spawnpoint
     */
    public void setPlayerPositionSpawnpoint(CoordinatesWithRoom c) {
        this.respawnCoordinates = (c);

    }
    public void setPlayerPosition(CoordinatesWithRoom c) {
        this.coordinates = (c);

    }


    // public void list target in that cell (CoordinatesWithRoom c){ } // todo a way to get players name +figures

    /**
     * getPlayerRoom
     *
     * returns player's room
     */
    public Room getPlayerRoom() {
        return coordinates.getRoom();
    }
    /**
     *getPlayerPositionX
     *returns player's x coordinate
     */
    public int getPlayerPositionX() {
        return coordinates.getX();
    }
    /**
     *getPlayerPositionY
     *returns player's y coordinate
     */
    public int getPlayerPositionY() {
        return coordinates.getY();
    }
    /**
     *newLife()
     *respawn the player at his initial spawnpoint
     */
    public void newLife() {
        setPlayerPositionSpawnpoint(respawnCoordinates);
        this.track = new Figure.PlayerColor[]{Figure.PlayerColor.NONE};
        this.marks = new Figure.PlayerColor[]{Figure.PlayerColor.NONE};
        setMaxPointAssignableCounter(0);
        putASkullOnTrack();

    }
//_________________________________________________________________________________________________________//

/*
    // I DON'T THINK IT SHOULD BE HERE
    public void pickWeaponCard(int x, int y){
        //metod to find the card in that coordinates


    }

    public WeaponCard getWeaponCard(Player player){
        //method to find the card in that coordinates
        //shows a list of owned card
        //return only a card
        WeaponCard card=new WeaponCard();
        return card;
    }*/
    /**
     *canGrabPowerUp
     *check if player can pick up another power up
     */
    public boolean canGrabPowerUp() {
        return (powerups.size() <= 3);
    }
    /**
     *canGrabWeapon
     *check if player can grab another weapon
     */
    public boolean canGrabWeapon() {
        return (hand.size() <= 3);
    }

    //  REMOVE CELL 8,6... WHEN SOMEONE DIES
    /**
     *hasDied //todo delete
     *
     */
    public void hasDied() {
        // SETS FIRST 1 TO 0
        for (int i = 0; i < pointsArray.length; i++) {
            if (pointsArray[i]) {
                pointsArray[i] = false;
                break;
            }
        }
    }
    /**
     *getPointsArray
     *returns player's pointsArray
     */
    public boolean[] getPointsArray() {
        return pointsArray;
    }
    /**
     *isDead
     * @return true if player is dead
     */
    public boolean isDead() {
        return (getTrack()[10] != Figure.PlayerColor.NONE);
    }
    /**
     *getAmmoBox
     *@return player's ammoBox
     */
    public int[] getAmmoBox() {
        return ammoBox;
    }
    /**
     *getHand
     * @return player's weapons hand
     */
    public LinkedList<WeaponCard> getHand() {
        return hand;
    }

    // FINDS FIRST EMPTY CELL OF DAMAGETRACK
    /**
     *trackEmptyCell
     * @return first empty cell founded
     */
    public int trackEmptyCell() {
        int x = 0;
        for (int i = 0; i < getTrack().length; i++) {
            if (getTrack()[i] == Figure.PlayerColor.NONE) {
                x = i;
                break;
            }
        }
        return x;
    }

    public LinkedList<PowerUpCard> getPowerUp() {
        return powerups;
    }


    /**
     *addDamageToTrack
     *adds damage done by another player to player's track
     */
    public void addDamageToTrack(Player shooter, int i) {
        for (int x = this.trackEmptyCell(); x < track.length && i > 0; x++) {
            this.track[x] = shooter.getColor();
            i--;
        }

    }
    /**
     *damageByShooter
     *@return how many damage by shooter to target
     */
    public int damageByShooter(Player shooter) {
        int x = 0;
        for (int i = 0; i < track.length; i++) {
            if (track[i] == shooter.getColor()) {
                x++;
            }
        }
        return x;
    }

    /**
     *marksByShooter
     * @return how many marks given by shooter to player
     */

    public int marksByShooter(Player shooter) {
        int x = 0;
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] == shooter.getColor()) {
                x++;
            }
        }
        return x;
    }

    /**
     *canAddMarks
     *check if can add a mark
     */
    public boolean canAddMark(Player shooter) {
        return (marksByShooter(shooter) < 3);
    }

    /**
     * markEmptyCell
     * @return index of first empty cell where can add the mark
     */
    public int markEmptyCell() {
        int x = 0;
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] == Figure.PlayerColor.NONE) {
                x = i;
                break;
            }

        }
        return x;
    }

    /**
     *addMarks
     *adds i number of marks by shooter to player
     */
    public void addMarks(Player shooter, int i) {
        for (int x = this.markEmptyCell(); x < marks.length && i > 0; x++) {
            if (canAddMark(shooter)) { // IF SHOOTER HAS LESS THEN 3 MARKS
                this.marks[x] = shooter.getColor();
            } else {
                break;
            }
            i--;
        }
    }


    ///// FOR CHECKPAYMENT

    /**
     *setRedCube
     * @param redCube: number of player's redCube
     */
    public void setRedCube(int redCube) {
        this.ammoBox[1] = redCube;
    }
    /**
     *setBlueCube
     * @param blueCube: number of player's blueCube
     */
    public void setBlueCube(int blueCube) {
        this.ammoBox[0] = blueCube;
    }
    /**
     *setYellowCube
     * @param yellowCube: number of player's yellowCube
     */
    public void setYellowCube(int yellowCube) {
        this.ammoBox[2] = yellowCube;
    }
/**setCube
 * @param blue
 * @param yellow
 * @param red
 * add cube to player's cube*/
    public void setCube(int red, int blue, int yellow) {
        setBlueCube(getCubeBlue() + blue);
        setRedCube(getCubeRed() + red);
        setYellowCube(getCubeYellow() + yellow);

        for (int counter = 0; counter < 3; counter++) {
            if (this.ammoBox[counter] >= numMaxCube) ammoBox[counter] = numMaxCube;
        }

    }
/**getCubeRed
 * @return number of redCube
 * */
    public int getCubeRed() {
        return ammoBox[1];
    }
    /**getCubeYellow
     * @return number of yellowCube
     * */
    public int getCubeYellow() {
        return ammoBox[2];
    }
    /**getCubeBlue
     * @return number of blueCube
     * */
    public int getCubeBlue() {
        return ammoBox[0];
    }
    /**moveToThisSquare
     * @param c desired position
     *          moves a player to a specific cell
     * */
    public void moveToThisSquare(CoordinatesWithRoom c) {
        setPlayerPosition(c.getX(), c.getY(), c.getRoom());
    }

//______________________________________point ________________________________________________________________________//

    /**setPoints
     * @param points
     * add points to player
     * */
    public void setPoints(int points) {
        this.points = +points;
    }
    /**getPointTrack
     * @return player's pointTrack
     * */
    public int[] getPointTrack() {
        return this.pointTrack;
    }


    /**getTrackSize
     * @return size of player's track
     * */
    public int getTrackSize() {
        return this.trackSize;
    }
    /**getSkullTrack
     * @return skullTrack
     * */
    public boolean[] getSkullTrack(){
        return this.skullTrack;
    }
    /**getTrackPointSize
     * @return size of trackPoint
     * */
    public int getTrackPointSize(){return this.trackPointSize;}
    /**getMaxPointAssignableCounter
     * @return index of max point assignable
     * */
    public int getMaxPointAssignableCounter(){return this.indexPointCounter;}
    /**setMaxPointAssignableCounter
     * @param max set index of max point assignable
     * */
    public void setMaxPointAssignableCounter(int max){this.indexPointCounter=max;}
    //______________________________________________putASkullOnTrack__________________________________________________________________________//
    /**putASkullOnTrack
     * put a skull on first free position on skullTrack
     * */
    public void putASkullOnTrack() {
        for(int i=0;i<getTrackSize();i++){
            if(getSkullTrack()[i]==false) {
                getSkullTrack()[i]=true;
                break;

            }}
    }
//_________________________how Many Deaths____________________________________________________________//
    /**numberOfDeaths
     * @return number of skull on his skullTrack
     * */
    public int numberOfDeaths(){
        int death=0;

        for(int i=0; i<getSkullTrack().length;i++)
        {
            if (getSkullTrack()[i])
                death++;
        }
    return death;
    }
    /**isFirstTurn
     * @return if player is on his first turn
     * */
    public boolean isFirstTurn(){
        return (getPlayerPositionX()==0);
    }
    /**returnPoints
     * @retun number of player's points
     * */
    public int returnPoints(){return this.points;}
    /**getCoordinateWithRooms
     * @return player's cell on the board
     * */
    public CoordinatesWithRoom getCoordinatesWithRooms(){
        return coordinates;
    }

    public void setName(String newName){this.name=newName;}
    public String getName(){return this.name;}
    public CoordinatesWithRoom getRespawnCoordinates(){return this.respawnCoordinates;}
}
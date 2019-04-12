package adrenaline;

import java.util.ArrayList;
import java.util.LinkedList;

public class Player {

    private Figure.PlayerColor[] track;
    private Figure.PlayerColor[] marks;
    private int[] ammoBox; //BLUE RED YELLOW
    private CoordinatesWithRoom coordinates;
    private Figure.PlayerColor color;
    private CoordinatesWithRoom respawnCoordinates;

    private LinkedList<WeaponCard> hand;
    private LinkedList<PowerUpCard> powerups;

    private boolean[] pointsArray;// HOW MANY TIMES PLAYER DIED

    public Player() {

    }

    // Coordinates rsp is the spawnPosition chosen by the player
    public Player(CoordinatesWithRoom rsp, Figure.PlayerColor playercolor) {

        this.ammoBox = new int[]{1, 1, 1}; //BLUE RED YELLOW
        this.track = new Figure.PlayerColor[]{Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE};
        this.marks = new Figure.PlayerColor[]{Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE
                , Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE, Figure.PlayerColor.NONE};
        // PUTTING "NONE" VALUE SO WE CAN USE SWITCH CASE
        this.color = playercolor;
        this.respawnCoordinates = rsp;
        this.coordinates = rsp;
        // they are lists because we need to add and remove easily
        this.hand = new LinkedList<WeaponCard>();
        this.powerups = new LinkedList<PowerUpCard>();
        this.pointsArray = new boolean[]{true, true, true, true, true, true};
    }

    // TODO IMPLEMENT PLAYER(COORDINATES) TO RESPAWNPLAYER ON SPAWNPOINT

    public Figure.PlayerColor getColor() {
        return color;
    }

    public Figure.PlayerColor[] getTrack() {
        return track;
    }

    public int checkDamage() {

        if (track[2] != Figure.PlayerColor.NONE && track[5] == Figure.PlayerColor.NONE)
            return 1;       //better Grab
        if (track[5] != Figure.PlayerColor.NONE)
            return 2;       //better Shoot (and Grab)
        return 0;
    }

    ;

    // public void setToken(Player player){}

    // public String getToken(Player player){return "";} //adrenaline.TokenCLI

    //________________________to control player's position____________________________________________________//

    public void setPlayerPosition(int x, int y) {
        this.coordinates.setCoordinates(x, y);
    }

    public void setPlayerPosition(int x, int y, Room r) {
        this.coordinates.setCoordinates(x, y);
        this.coordinates.setRoom(r);
    }

    public Room getPlayerRoom() {
        return coordinates.getRoom();
    }

    public int getPlayerPositionX() {
        return coordinates.getX();
    }

    public int getPlayerPositionY() {
        return coordinates.getY();
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

    public boolean canGrabPowerUp() {
        return (powerups.size() <= 2);
    }

    public boolean canGrabWeapon() {
        return (hand.size() <= 2);
    }

    //  REMOVE CELL 8,6... WHEN SOMEONE DIES
    public void hasDied() {
        // SETS FIRST 1 TO 0
        for (int i = 0; i < pointsArray.length; i++) {
            if (pointsArray[i]) {
                pointsArray[i] = false;
                break;
            }
        }
    }

    public boolean[] getPointsArray() {
        return pointsArray;
    }

    // CAN SELECT WEAPON IF CHARGED (EVERY AMMOCUBE BASE MUST BE PAID)
    public boolean isLoaded(WeaponCard w) {
        for (int i = 0; i < w.price.size(); i++) {
            if (w.price.get(i).getEffect() == AmmoCube.Effect.BASE) {
                if (!w.price.get(i).getPaid()) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
    public void getPoints(){
    }
    */

    public boolean isDead() {
        return (track[11] != Figure.PlayerColor.NONE);
    }

    public int[] getAmmoBox() {
        return ammoBox;
    }

    public LinkedList<WeaponCard> getHand() {
        return hand;
    }

    // FINDS FIRST EMPTY CELL OF DAMAGETRACK
    public int trackEmptyCell() {
        int x=0;
        for (int i = 0; i < getTrack().length; i++) {
            if (getTrack()[i] == Figure.PlayerColor.NONE) {
                x=i;
                break;
            }
        }
        return x;
    }

    //  ADDS i NUMBER OF OLDMARKS AND DAMAGES BY SHOOTER TO PLAYER
    public void addDamageToTrack(Player shooter, int i){
        for(int x = this.trackEmptyCell(); x<track.length && i>0; x++){
            this.track[i] = shooter.getColor();
            i--;
        }

    }

    // COUNTS HOW MANY MARKS GIVEN BY SHOOTER TO PLAYER
    public int marksByShooter(Player shooter){
        int x=0;
        for(int i=0;i<marks.length;i++){
            if(marks[i]==shooter.getColor()){
                x++;
            }
        }
        return x;
    }

    // IF SHOOTER HAS LESS THEN 3 MARKS
    public boolean canAddMark(Player shooter){
        return (marksByShooter(shooter)<3);
    }

    // FINDS FIRST EMPTY CELL OF MARKS
    public int markEmptyCell() {
        int x=0;
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] == Figure.PlayerColor.NONE) {
                x=i;
                break;
            }
            else{
                x=42;
                break;
            }
        }
        return x;
    }

    // ADDS i NUMBER OF MARKS BY SHOOTER TO PLAYER
    public void addMarks(Player shooter, int i){
            for (int x=i; x> 0; x--) {
                if(canAddMark(shooter)){ // IF SHOOTER HAS LESS THEN 3 MARKS
                    this.marks[markEmptyCell()] = shooter.getColor();
                }
                else{
                    break;
                }
            }
    }


}


package adrenaline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Player {
    private final static int numMaxCube=3;
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
    }

    @Override
    public String toString() {
        return "My name is x and my color is " + this.color;
    }

    public Figure.PlayerColor getColor() {
        return color;
    }

    public Figure.PlayerColor[] getTrack() {
        return track;
    }
    public Figure.PlayerColor[] getMarks() {
        return marks;
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

    public void setPlayerPositionSpawnpoint(CoordinatesWithRoom c) {
        this.coordinates=(c);

    }


    // public void list target in that cell (CoordinatesWithRoom c){ } // todo a way to get players name +figures

    public Room getPlayerRoom() {
        return coordinates.getRoom();
    }

    public int getPlayerPositionX() {
        return coordinates.getX();
    }

    public int getPlayerPositionY() {
        return coordinates.getY();
    }

    public void newLife(){
        setPlayerPositionSpawnpoint(respawnCoordinates);
        this.track=new Figure.PlayerColor[]{Figure.PlayerColor.NONE};
        this.marks=new Figure.PlayerColor[]{Figure.PlayerColor.NONE};
        //must reset also ammoBox?
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
        return (powerups.size() <= 3);
    }

    public boolean canGrabWeapon() {
        return (hand.size() <= 3);
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

    public boolean isDead() {
        return (track[10] != Figure.PlayerColor.NONE);
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
    public LinkedList<PowerUpCard> getPowerUp(){return powerups;}
    //  ADDS i NUMBER OF OLDMARKS AND DAMAGES BY SHOOTER TO PLAYER
    public void addDamageToTrack(Player shooter, int i){
        for(int x = this.trackEmptyCell(); x<track.length && i>0; x++){
            this.track[x] = shooter.getColor();
            i--;
        }

    }

    // COUNTS HOW MANY DAMAGES BY SHOOTER TO TARGET
    public int damageByShooter(Player shooter){
        int x=0;
        for(int i=0;i<track.length;i++){
            if(track[i]==shooter.getColor()){
                x++;
            }
        }
        return x;
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

        }
        return x;
    }





    // ADDS i NUMBER OF MARKS BY SHOOTER TO PLAYER
    public void addMarks(Player shooter, int i){
            for (int x=this.markEmptyCell(); x<marks.length && i>0; x++) {
                if(canAddMark(shooter)){ // IF SHOOTER HAS LESS THEN 3 MARKS
                    this.marks[x] = shooter.getColor();
                }
                else{
                    break;
                }
                i--;
            }
    }




    ///// FOR CHECKPAYMENT

    public void setRedCube(int redCube) {
        this.ammoBox[1] = redCube;
    }
    public void setBlueCube(int blueCube){
        this.ammoBox[0]=blueCube;
    }
    public void setYellowCube(int yellowCube){
        this.ammoBox[2]=yellowCube;
    }

    public void setCube(int red,int blue,int yellow)
    {
        setBlueCube(getCubeBlue()+blue);
        setRedCube(getCubeRed()+red);
        setYellowCube(getCubeYellow()+yellow);

   for(int counter=0;counter<3;counter++)
    {
        if(this.ammoBox[counter]>=numMaxCube)ammoBox[counter]=numMaxCube;
    }

    }
    public int getCubeRed(){return ammoBox[1];}
    public int getCubeYellow(){return ammoBox[2];}
    public int getCubeBlue(){return ammoBox[0];}

    // MOVES PLAYER TO A CELL
    public void moveToThisSquare(CoordinatesWithRoom c){
        setPlayerPosition(c.getX(),c.getY(),c.getRoom());
    }




}


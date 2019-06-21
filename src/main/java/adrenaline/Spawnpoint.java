package adrenaline;

import java.util.LinkedList;

public class Spawnpoint {

    int x;
    int y;
    private LinkedList<WeaponCard> weaponCards;
    private AmmoCube.CubeColor color;

    public Spawnpoint(){
        weaponCards=new LinkedList<>();
    }

    public Spawnpoint(int x, int y){
        this.x = x;
        this.y = y;
        this.weaponCards=new LinkedList<>();
    }

    public int getSpawnpointX( ) {

        return x;
    }

    public int getSpawnpointY( ) {

        return y;
    }
    public void addWeaponCard(WeaponCard weaponCard){
        this.weaponCards.add(weaponCard);
    }
    public LinkedList<WeaponCard> getWeaponCards(){
        return this.weaponCards;
    }
    public void setColor(AmmoCube.CubeColor c){
        this.color = c;
    }
    public AmmoCube.CubeColor getColor(){ return color;}

}

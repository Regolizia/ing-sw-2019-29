package adrenaline.weapons;

import adrenaline.*;
import adrenaline.gameboard.GameBoard;

import java.util.List;

/**
 * 
 */
public class Whisper extends WeaponCard {

    /**
     * Default constructor
     */
    public Whisper() {
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,true));
        price.add(new AmmoCube(AmmoCube.CubeColor.BLUE, AmmoCube.Effect.BASE,false));
        price.add(new AmmoCube(AmmoCube.CubeColor.YELLOW, AmmoCube.Effect.BASE,false));
    }

    public boolean canShootBase(){
        return true;
    }

    @Override
    public List<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c, EffectAndNumber en, GameBoard g) {
        List<CoordinatesWithRoom> list = super.getPossibleTargetCells(c, en, g);
        List<CoordinatesWithRoom> listOne = c.oneTileDistant(g, false);
        listOne.add(c);

        for(int k=list.size()-1;k>=0;k--){
            for(CoordinatesWithRoom c2: listOne){
                if(list.get(k).getX()==c2.getX() &&
                        list.get(k).getY()==c2.getY() &&
                        list.get(k).getRoom().getToken()==c2.getRoom().getToken()){
                    list.remove(k);
                }
            }
        }
        return list;
    }

    @Override
    public void applyDamage(List<Object> targetList, Player p, EffectAndNumber e) {

        setDamaged(targetList,p);
        switch (e.getEffect()) {
            case BASE:  // 3 DAMAGE, 1 MARK, 1 TARGET
                if(targetList.get(0) instanceof Player) {
                    int i =((Player) targetList.get(0)).marksByShooter(p);
                    i=i+3;
                    ((Player) targetList.get(0)).addDamageToTrack(p,i);

                    ((Player) targetList.get(0)).addMarks(p,1);
                }

                else {
                    // DAMAGE SPAWNPOINT
                }
                break;


        }

    }
    @Override
    public String toString() {
        return "Whisper";
    }
    }
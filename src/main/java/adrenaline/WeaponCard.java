package adrenaline;

import java.sql.Struct;
import java.util.*;

public class WeaponCard extends Card{


    protected LinkedList<AmmoCube> price;

    /**
     * Default constructor
     */
    public WeaponCard() {
        price = new LinkedList<AmmoCube>();
    }

    // TO BE OVERRIDDEN
    public LinkedList<CoordinatesWithRoom> getPossibleTargetCells(CoordinatesWithRoom c){
        LinkedList<CoordinatesWithRoom> list = new LinkedList<>();
        return list;
    }



}

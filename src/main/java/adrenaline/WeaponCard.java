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
}

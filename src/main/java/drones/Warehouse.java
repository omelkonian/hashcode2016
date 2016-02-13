package drones;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Orestis Melkonian
 */
public class Warehouse {
    public int id;
    public static Point location;
    public Map<Integer, Integer> inventory = new HashMap<>();

    public Warehouse(int id, Point point) {
        this.id = id;
        location = point;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += location + "\n";
        for (Integer key : inventory.keySet())
            ret += key + " ~> " + inventory.get(key) + "\n";
        return ret;
    }
}

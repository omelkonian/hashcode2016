package drones;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Orestis Melkonian
 */
public class Data {
    public static int maxPayload;
    public int rows, cols, droneNo, turnNo, productNo, warehouseNo, orderNo;
    public Map<Integer, Integer> weights = new HashMap<>();
    public Set<Warehouse> warehouses = new HashSet<>();
    public Set<Order> orders = new HashSet<>();

    @Override
    public String toString() {
        String ret = rows + " " + cols + " " + droneNo + " " + turnNo + " " + maxPayload + " " + productNo + " " + warehouseNo + " " + orderNo + "\n";
        for (Integer key : weights.keySet())
            ret += key + " ~> " + weights.get(key) + "\n";
        return ret;
    }
}
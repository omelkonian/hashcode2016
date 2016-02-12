package drones;

import jsprit.core.problem.Location;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.solution.route.activity.TimeWindow;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Orestis Melkonian
 */
public class Warehouse {
    public static int id = 0;

    public static Coords location;
    public Map<Integer, Integer> inventory = new HashMap<>();

    public Warehouse(Coords coords) {
        location = coords;
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

package drones;

import jsprit.core.problem.Location;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.job.Shipment;
import jsprit.core.problem.solution.route.activity.TimeWindow;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Orestis Melkonian
 */
public class Order {
    public static int id = 0;

    public Coords location;
    public Map<Integer, Integer> products = new HashMap<>();

    public Order(Coords coords) {
        location = coords;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += location + "\n";
        for (Integer key : products.keySet())
            ret += key + " ~> " + products.get(key) + "\n";
        return ret;
    }

    public void set(VehicleRoutingProblem.Builder builder) {
        Shipment.Builder b = Shipment.Builder
                .newInstance("serviceId" + (id++))
                .setName("myShipment" + (id - 1))
                .setPickupLocation(Location.newInstance(Warehouse.location.x, Warehouse.location.y))
                .addSizeDimension(0, Data.maxPayload)
                .setDeliveryLocation(Location.newInstance(location.x, location.y));

        Shipment shipment = b.build();

        builder.addJob(shipment);
    }
}

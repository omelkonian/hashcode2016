package drones;

import jsprit.core.problem.Location;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.job.Shipment;
import jsprit.core.problem.solution.route.activity.TimeWindow;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Orestis Melkonian
 */
public class Order {
    public int id;
    public Point location;
    public Map<Integer, Integer> products = new HashMap<>();

    public Order(int id, Point point) {
        this.id = id;
        location = point;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += location + "\n";
        for (Integer key : products.keySet())
            ret += key + " ~> " + products.get(key) + "\n";
        return ret;
    }

    private static int id_gen = 0;
    public void set(VehicleRoutingProblem.Builder global) {
        int sum = products.values().stream().reduce((i1, i2) -> i1 + i2).get();
        List<Pair<Integer, Integer>> pairs = products.keySet()
                .stream()
                .map(k -> new Pair<>(k, products.get(k)))
                .filter(p -> p.getSecond() > 0)
                .collect(Collectors.toList());

        while (sum > 0) {
            Set<Integer> toCheck = products.keySet();
            Shipment.Builder b = Shipment.Builder
                    .newInstance("service_" + id + "_" + id_gen)
                    .setName("shipment_" + id + "_" + id_gen)
                    .setPickupLocation(Location.newInstance(Warehouse.location.x, Warehouse.location.y))
                    .setDeliveryTimeWindow(new TimeWindow(0.0, Data.turnNo))
                    .setPickupTimeWindow(new TimeWindow(0.0, Data.turnNo))
                    .setDeliveryLocation(Location.newInstance(location.x, location.y));

            int remaining = (sum > Data.maxPayload) ? Data.maxPayload : sum;
            while (remaining > 0 && !pairs.isEmpty()) {
                Pair<Integer, Integer> pair = pairs.get(0);
                int key = pair.getFirst(), value = pair.getSecond();
                if (value > remaining) {
                    pairs.set(0, new Pair<>(key, value - remaining));
                    b.addSizeDimension(key, remaining);
                    toCheck.remove(key);
                    remaining = 0;
                }
                else {
                    pairs.remove(0);
                    b.addSizeDimension(key, value);
                    toCheck.remove(key);
                    remaining -= value;
                }
            }

            for (Integer key : toCheck)
                b.addSizeDimension(key, 0);

            Shipment shipment = b.build();
            global.addJob(shipment);

            sum = sum - Data.maxPayload;
            id_gen++;
        }
    }
}

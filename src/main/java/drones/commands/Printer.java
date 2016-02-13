package drones.commands;

import jsprit.core.problem.Capacity;
import jsprit.core.problem.solution.route.activity.TourActivity;

/**
 * @author Orestis Melkonian
 */
public class Printer {
    public static void print(TourActivity... acts) {
        for (TourActivity act : acts)
            switch (act.getName()) {
                case "pickupShipment":
                    System.out.println("Pickup " + printCap(act));
                    break;
                case "deliverShipment":
                    System.out.println("Deliver" + printCap(act));
                    break;
                default:
                    System.out.println("--");
            }
    }

    public static String printCap(TourActivity act) {
        Capacity cap = act.getSize();
        String ret = "[";
        for (int i = 0; i < cap.getNuOfDimensions(); i++)
            ret += cap.get(i) + ",";
        ret += "]";
        return ret.replace(",]", "]");
    }
}

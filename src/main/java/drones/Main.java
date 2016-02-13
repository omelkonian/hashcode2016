package drones;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import drones.commands.Command;
import drones.commands.Deliver;
import drones.commands.Load;
import drones.commands.Printer;
import jsprit.analysis.toolbox.Plotter;
import jsprit.core.algorithm.*;
import jsprit.core.algorithm.box.Jsprit;
import jsprit.core.algorithm.state.StateManager;
import jsprit.core.problem.Capacity;
import jsprit.core.problem.Location;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.constraint.ConstraintManager;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.TourActivity;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.reporting.SolutionPrinter;
import jsprit.core.util.Solutions;

public class Main {
    public final static String filename =
                "mother_of_all_warehouses"
//                "busy_day"
//                "redudancy"
//                "easy"
//                "one_type"
//                "two_types"
//                "two_deliveries"
//            "two_pickups_and_types"
//                "two_pickups"
//                "products"
            ;

	public static void main(String [] args) throws FileNotFoundException, UnsupportedEncodingException {
		parse();
        Data.print();
        solve(init());
	}

    private static VehicleRoutingProblem init() {
        VehicleRoutingProblem.Builder vrpBuilder =  VehicleRoutingProblem.Builder.newInstance();

        VehicleTypeImpl.Builder vehicleBuilder =
                VehicleTypeImpl.Builder.newInstance("drone");
        for (int i = 0; i < Data.productNo; i++)
            vehicleBuilder.addCapacityDimension(i, Data.maxPayload);
        VehicleTypeImpl vehicleType = vehicleBuilder.build();

        // Vehicles
        for (int i = 0; i < Data.droneNo; i++) {
            VehicleImpl vehicle = VehicleImpl.Builder.newInstance("vehicle_" + i)
                    .setType(vehicleType)
                    .setReturnToDepot(false)
                    .setStartLocation(Location.newInstance(Warehouse.location.x, Warehouse.location.y))
                    .build();

            vrpBuilder.addVehicle(vehicle);
        }

        // Orders
        for (Order order : Data.orders.values())
            order.set(vrpBuilder);

        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);
        return vrpBuilder.build();
    }

    private static void solve(VehicleRoutingProblem problem) throws FileNotFoundException, UnsupportedEncodingException {
        // TODO modify cost
        StateManager stateManager = new StateManager(problem);
        ConstraintManager constraintManager = new ConstraintManager(problem, stateManager);
        constraintManager.addConstraint(new DeliverAfterPickupConstraint(stateManager), ConstraintManager.Priority.CRITICAL);
        VehicleRoutingAlgorithm algorithm = Jsprit.Builder.newInstance(problem)
                .setStateAndConstraintManager(stateManager, constraintManager).buildAlgorithm();
        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

        // Output
        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);
        new Plotter(problem,bestSolution).plot("output/solution.png", "solution");
        Queue<Command> commands = output(bestSolution);

        PrintWriter writer = new PrintWriter("output/" + filename + ".out");
        writer.println(commands.size());
        commands.forEach(writer::println);
        writer.close();
    }

    private static Queue<Command> output(VehicleRoutingProblemSolution solution) {
        Queue<Command> commands = new LinkedList<>();
        for (VehicleRoute route : solution.getRoutes()) {
            int vehicleID = numerify(route.getVehicle().getId());

            /*System.out.println("===========");
            System.out.println("\t\t"+ vehicleID);
            System.out.println("===========");*/

//            Printer.print(route.getActivities().toArray(new TourActivity[route.getActivities().size()]));

            for (TourActivity act : route.getActivities()) {

                if (act.getName().equals("pickupShipment")) {
                    Capacity capacity = ((TourActivity.JobActivity) act).getJob().getSize();
                    for (int productID = 0; productID < capacity.getNuOfDimensions(); productID++) {
                        int amount = capacity.get(productID);
                        if (amount > 0) {
                            Command load = new Load(vehicleID, 0, productID, amount);
                            commands.add(load);
                        }
                    }
                }

                if (act.getName().equals("deliverShipment")) {
                    int orderID = numerify(((TourActivity.JobActivity) act).getJob().getId());
                    Capacity capacity = ((TourActivity.JobActivity) act).getJob().getSize();
                    for (int productID = 0; productID < capacity.getNuOfDimensions(); productID++) {
                        int amount = capacity.get(productID);
                        if (amount > 0) {
                            Command deliver = new Deliver(vehicleID, orderID, productID, amount);
                            commands.add(deliver);
                        }
                    }
                }
            }
        }
        return commands;
    }

	private static void parse() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("input/" + filename + ".in"));

		Data.rows = scan.nextInt();
		Data.cols = scan.nextInt();
		Data.droneNo = scan.nextInt();
		Data.turnNo = scan.nextInt();
		Data.maxPayload = scan.nextInt();
		scan.nextLine();

		Data.productNo = scan.nextInt();
		scan.nextLine();

		for (int i = 0; i < Data.productNo; i++)
			Data.weights.put(i, scan.nextInt());
		scan.nextLine();

		Data.warehouseNo = scan.nextInt();
		scan.nextLine();

		for (int i = 0; i < Data.warehouseNo; i++) {
			Warehouse cur = new Warehouse(i, new Point(scan.nextInt(), scan.nextInt()));
			scan.nextLine();
			for (int j = 0; j < Data.productNo; j++)
				cur.inventory.put(j, scan.nextInt());
			scan.nextLine();
			Data.warehouses.put(i, cur);
		}

		Data.orderNo = scan.nextInt();
		scan.nextLine();

		for (int i = 0; i < Data.orderNo; i++) {
			Order cur = new Order(i, new Point(scan.nextInt(), scan.nextInt()));
			scan.nextLine();
			int itemNo = scan.nextInt();
			scan.nextLine();
			for (int j = 0; j < itemNo; j++) {
				int type = scan.nextInt();
				if (cur.products.containsKey(type))
					cur.products.put(type, cur.products.get(type) + 1);
				else
					cur.products.put(type, 1);
			}
			scan.nextLine();

			Data.orders.put(i, cur);
		}
	}

    private static int numerify(String str) {
        return Integer.valueOf(str.split("_")[1]);
    }
}


package drones;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Scanner;

import jsprit.core.algorithm.*;
import jsprit.core.algorithm.box.Jsprit;
import jsprit.core.problem.Location;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.reporting.SolutionPrinter;
import jsprit.core.util.Solutions;

public class Main {
	public static void main(String [] args) throws FileNotFoundException {
		Data data = parse();
        solve(init(data));
	}

    private static VehicleRoutingProblem init(Data data) {
        VehicleRoutingProblem.Builder vrpBuilder =  VehicleRoutingProblem.Builder.newInstance();

        VehicleTypeImpl vehicleType =
                VehicleTypeImpl.Builder.newInstance("drone")
                        .addCapacityDimension(0, Data.maxPayload)
                        .build();

        // VEHICLES
        for (int i = 0; i < data.droneNo; i++) {
            VehicleImpl vehicle = VehicleImpl.Builder.newInstance("vehicle1Id" + i)
                    .setType(vehicleType)
                    .setReturnToDepot(false)
                    .setStartLocation(Location.newInstance(Warehouse.location.x, Warehouse.location.y))
                    .build();

            vrpBuilder.addVehicle(vehicle);
        }

        // ORDERS
        for (Order order : data.orders)
            order.set(vrpBuilder);

        // RETURN
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);
        return vrpBuilder.build();
    }

    private static void solve(VehicleRoutingProblem problem) {
        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);
        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

        // Output
        VehicleRoute
        SolutionPrinter.print(Solutions.bestOf(solutions));
//	    new Plotter(problem, Solutions.bestOf(solutions)).plot("output/p08_solution.png", "p08");
    }

	private static Data parse() throws FileNotFoundException {
		Data data = new Data();

		Scanner scan = new Scanner(new File("../in/busy_day.in"));

		data.rows = scan.nextInt();
		data.cols = scan.nextInt();
		data.droneNo = scan.nextInt();
		data.turnNo = scan.nextInt();
		Data.maxPayload = scan.nextInt();
		scan.nextLine();

		data.productNo = scan.nextInt();
		scan.nextLine();

		for (int i = 0; i < data.productNo; i++)
			data.weights.put(i, scan.nextInt());
		scan.nextLine();

		data.warehouseNo = scan.nextInt();
		scan.nextLine();

		for (int i = 0; i < data.warehouseNo; i++) {
			Warehouse cur = new Warehouse(new Coords(scan.nextInt(), scan.nextInt()));
			scan.nextLine();
			for (int j = 0; j < data.productNo; j++)
				cur.inventory.put(j, scan.nextInt());
			scan.nextLine();
			data.warehouses.add(cur);
		}

		data.orderNo = scan.nextInt();
		scan.nextLine();

		for (int i = 0; i < data.orderNo; i++) {
			Order cur = new Order(new Coords(scan.nextInt(), scan.nextInt()));
			scan.nextLine();
			int itemNo = scan.nextInt();
			scan.nextLine();
			for (int j = 0; j < itemNo; j++) {
				int type = scan.nextInt();
				if (cur.products.containsKey(type))
					cur.products.put(type, cur.products.get(type));
				else
					cur.products.put(type, 1);
			}
			scan.nextLine();

			data.orders.add(cur);
		}

		return data;
	}
}


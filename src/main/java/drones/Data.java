package drones;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Orestis Melkonian
 */
public class Data {
    public static int maxPayload;
    public static int rows, cols, droneNo, turnNo, productNo, warehouseNo, orderNo;
    public static Map<Integer, Integer> weights = new HashMap<>();
    public static Map<Integer, Warehouse> warehouses = new HashMap<>();
    public static Map<Integer, Order> orders = new HashMap<>();

    public static void print() {
        System.out.println("========================DATA==============================");
        System.out.println("Size: " + rows + "x" + cols);
        System.out.println("#drones: " + droneNo);
        System.out.println("#maxPayload: " + maxPayload);
        System.out.println("#turns: " + turnNo);
        System.out.println("#products: " + productNo);
        System.out.println("#warehouses: " + warehouseNo);
        System.out.println("#orders: " + orderNo);
        String ret = rows + " " + cols + " " + droneNo + " " + turnNo + " " + maxPayload + " " + productNo + " " + warehouseNo + " " + orderNo + "\n";
        System.out.println(ret);
        System.out.println("==========================================================");
    }
}